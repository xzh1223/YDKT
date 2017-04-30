package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ForumContentAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/2/12.
 * <p>
 * 论坛具体内容以及评论界面
 */

public class ForumContentActivity extends BaseActivity {

    private List<Forum> forumList = new ArrayList<>();
    private List<Forum> hList = new ArrayList<>();
    // 读取评论列表的服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readDiscussList";
    // 读取评论列表的服务器请求地址
    private String URL2 = "http://118.190.10.181:8080/YDKT/insertDiscussList";
    // 读取用户头像所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/";
    // 读取评论成功和失败的标志位
    private static final int READ_SUCCESS_TEXT = 200;
    private static final int READ_FAILED_TEXT = 500;
    // 发送评论的成功或者失败的标志位
    private static final int POST_SUCCESS_TEXT = 2001;
    private static final int POST_FAILED_TEXT = 5001;
    private RecyclerView recyclerView;
    private ForumContentAdapter adapter;
    // 下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    private Forum[] forums;
    // 基本控件
    ImageView forum_user_img;
    TextView forum_username, forum_time, forum_title, forum_content, forum_times;
    // 从Forum得到的相关数据
    String idFromForum, usernameFromForum, imageFromForum, timeFromForum,
            titleFromForum, contentFromForum, timesFromForum;
    // 获取输入的评论内容
    private String discussText;
    // 获取登录用户的id
    private String idFromLog;

    private boolean isLoading = false;
    private ProgressDialog loaddialog;
    private String fImageFromForum = "";
    private LinearLayout picture;

    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READ_SUCCESS_TEXT:
                    String responseData = msg.obj.toString();
                    parseJSON(responseData);
                    initDiscuss();
                    break;
                case POST_SUCCESS_TEXT:
                    Toast.makeText(ForumContentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    //评论后刷新当前页面
                    refreshCurrent();
                    break;
                case POST_FAILED_TEXT:
                    Toast.makeText(ForumContentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    //评论后刷新当前页面
                    refreshCurrent();
                    break;
                default:
                    break;
            }
        }
    };

    //评论后刷新当前页面
    private void refreshCurrent() {
        //评论后刷新当前页面
        hList.clear();
        forumList.clear();
        sendRequestWithOkHttp();
    }

    /**
     * 初始化适配器
     */
    private void initDiscuss() {

        hList.clear();
        for (int i = 0; i < forums.length; i++) {
            forumList.add(forums[i]);
        }

        if (forumList.size() >= 10) {
            for (int i = 0; i < 10; i++) {
                hList.add(forumList.get(i));
            }

        } else {
            for (int i = 0; i < forumList.size(); i++) {
                hList.add(forumList.get(i));
            }
        }
        adapter = new ForumContentAdapter(hList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshForums();
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshForums() {

        hList.clear();
        forumList.clear();
        sendRequestWithOkHttp();
        stopRefresh();
    }

    private void stopRefresh() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forum_content);
        getData();
        initMenu();
        initView();
        sendRequestWithOkHttp();
        setFAB();
        setRefresh();

    }

    /**
     * 设置悬浮评论按钮
     */
    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDiscussWithDialog(ForumContentActivity.this);
            }
        });
    }

    /**
     * 悬浮窗口评论界面
     */
    private void postDiscussWithDialog(Context context) {
        // 悬浮窗加载布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_post_discuss, null);

        final EditText edit_content = (EditText) view.findViewById(R.id.edit_content);
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setView(view)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        discussText = edit_content.getText().toString();
                        if (!discussText.equals("") && discussText != null) {
                            sendRequestWithOkHttp2();
                        } else {
                            Toast.makeText(ForumContentActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    /**
     * 发布评论的网络请求
     */
    private void sendRequestWithOkHttp2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("f_id", idFromForum)
                            .add("user_id", idFromLog)
                            .add("d_content", discussText)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL2, requestBody, new Callback() {

                        /**
                         * 请求失败
                         */
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        /**
                         * 请求成功
                         */
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resp = response.body().string();
                            Message message = new Message();
                            if (resp.equals("500")) {
                                message.what = POST_FAILED_TEXT;
                            } else if (resp.equals("200")) {
                                message.what = POST_SUCCESS_TEXT;
                            }
                            handler.sendMessage(message);// 将message发送到Handler
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取评论的网络请求
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("f_id", idFromForum)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {

                        /**
                         * 请求失败
                         */
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        /**
                         * 请求成功
                         */
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resp = response.body().string();
                            Message message = new Message();
                            if (resp.equals("[]")) {
                                message.what = READ_FAILED_TEXT;
                            } else {
                                message.what = READ_SUCCESS_TEXT;
                                message.obj = resp;
                            }
                            handler.sendMessage(message);// 将message发送到Handler
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析从服务器获取的JSON格式数据并存放到Fruit数组中
     * {"f_times":1,"f_title":"this is title","user_id":1,"f_time":"2017-02-10 20:43:00","id":1,
     * "f_content":"this is content","user_img":"img_proj/img.png","username":"admin"}
     */
    private void parseJSON(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            forums = new Forum[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String user_img = jsonObject.getString("user_img");
                String username = jsonObject.getString("username");
                String d_time = jsonObject.getString("d_time");
                String d_content = jsonObject.getString("d_content");
                Forum f = new Forum(id, IMG_URL + user_img, username, d_time, d_content);
                forums[i] = f;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取从Forum传递的数据
     */
    private void getData() {
        idFromForum = getIntent().getStringExtra("idFromForum");
        usernameFromForum = getIntent().getStringExtra("usernameFromForum");
        imageFromForum = getIntent().getStringExtra("imageFromForum");
        timeFromForum = getIntent().getStringExtra("timeFromForum");
        titleFromForum = getIntent().getStringExtra("titleFromForum");
        contentFromForum = getIntent().getStringExtra("contentFromForum");
        timesFromForum = getIntent().getStringExtra("timesFromForum");
        fImageFromForum = getIntent().getStringExtra("fImageFromForum");

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
    }

    /**
     * 初始化控件并添加数据到对应控件中
     */
    private void initView() {
        forum_user_img = (ImageView) findViewById(R.id.forum_user_img);
        forum_username = (TextView) findViewById(R.id.fourum_username);
        forum_time = (TextView) findViewById(R.id.forum_time);
        forum_title = (TextView) findViewById(R.id.forum_title);
        forum_content = (TextView) findViewById(R.id.forum_content);
        forum_times = (TextView) findViewById(R.id.forum_times);
        Glide.with(this).load(imageFromForum).into(forum_user_img);
        picture = (LinearLayout) findViewById(R.id.picture);
        forum_username.setText(usernameFromForum);
        forum_time.setText(timeFromForum);
        forum_title.setText(titleFromForum);
        forum_content.setText(contentFromForum);
        forum_times.setText(timesFromForum + "人浏览过");
        // 动态添加图片
        if (fImageFromForum.equals("0")) {
            picture.setVisibility(View.GONE);
        } else {
            picture.setVisibility(View.VISIBLE);
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(fImageFromForum).into(imageView);
            picture.addView(imageView);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 设置单列布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向右滚动,上
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast && !isLoading) {
                        //加载更多功能的代码
                        loaddialog = new ProgressDialog(ForumContentActivity.this);
                        loaddialog.setTitle(getResources().getString(R.string.tip));
                        loaddialog.setMessage("正在加载，请稍后...");
                        loaddialog.show();
                        isLoading = true;
                        if (hList.size() == forumList.size()) {
                            loaddialog.dismiss();
                            isLoading = false;
                            Toast.makeText(ForumContentActivity.this, "已滑动到最底端", Toast.LENGTH_SHORT).show();
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData();
                            }
                        }, 1000);

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dy > 0) {//dy > 0 时为向上滚动  dy < 0 时为向下滚动.dx>0向右滚动
                    isSlidingToLast = true;
                } else if (dy < 0) {
                    isSlidingToLast = false;
                }
            }
        });
    }

    /**
     * 设置标题栏返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateLoadingStatus() {
        loaddialog.dismiss();
        isLoading = false;
    }

    /**
     * 模拟加载数据
     */
    private void loadData() {
        int count = adapter.getItemCount();
        try {
            if (hList.size() < forumList.size()) {
                if ((forumList.size() - hList.size()) > 10) {
                    for (int i = count; i < count + 10; i++) {
                        updateLoadingStatus();
                        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                        adapter.addNewData(forumList.get(i));
                    }
                } else {
                    for (int i = count; i < forumList.size(); i++) {
                        updateLoadingStatus();
                        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                        adapter.addNewData(forumList.get(i));

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
