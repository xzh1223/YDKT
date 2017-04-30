package cn.edu.jssvc.xzh.rebuildclass.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.activity.PostForumActivity;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ForumAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;
import cn.edu.jssvc.xzh.rebuildclass.util.GlideCacheUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xzh on 2017/1/16.
 * <p>
 * 论坛界面
 * 发送网络请求读取论坛帖子列表
 * 添加下拉刷新功能
 * 设置悬浮按钮，点击跳转发帖界面
 */
public class ForumFragment extends Fragment {
    private RecyclerView recyclerView;

    // 下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Forum> hList = new ArrayList<>();
    private List<Forum> forumList = new ArrayList<>();
    private ForumAdapter adapter;
    // 论坛列表的服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readForumList";
    // 图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/";

    private static final int SUCCESS_TEXT = 200;

    private Forum[] forums = {};

    private boolean isLoading = false;
    private ProgressDialog loaddialog;


    /**
     * 消息处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_TEXT:
                    String responseData = msg.obj.toString();
                    parseJSON(responseData);
                    initForum();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    /**
     * 相当于Activity的onCreate()方法加载控件和操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clearGlide();
        sendRequestWithOkHttp();
        initView();
        setRefresh();
        setFAB();

    }

    /**
     * 清楚Glide数据
     */
    public void clearGlide() {
        GlideCacheUtil.getInstance().clearImageAllCache(getActivity());
        GlideCacheUtil.getInstance().clearImageDiskCache(getActivity());
        GlideCacheUtil.getInstance().clearImageMemoryCache(getActivity());
    }

    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostForumActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
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

    private void sendRequestWithOkHttp() {
        new Thread(new MyThread()).start();
    }

    /**
     * 初始化数据
     */
    private void initForum() {

        hList.clear();
        forumList.clear();
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
        adapter = new ForumAdapter(hList);
        recyclerView.setAdapter(adapter);

    }

    //发帖之后刷新当前页面
    @Override
    public void onStart() {
        super.onStart();
        //评论后刷新当前页面
        clearGlide();
        hList.clear();
        forumList.clear();
        sendRequestWithOkHttp();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.forum_recycler_view);
        // 设置单列布局
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
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
                        loaddialog = new ProgressDialog(getActivity());
                        loaddialog.setTitle(getResources().getString(R.string.tip));
                        loaddialog.setMessage("正在加载，请稍后...");
                        loaddialog.show();
                        isLoading = true;
                        if (hList.size() == forumList.size()) {
                            loaddialog.dismiss();
                            isLoading = false;
                            Toast.makeText(getActivity(), "已滑动到最底端", Toast.LENGTH_SHORT).show();
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

    /**
     * 创建网络请求线程
     */
    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URL)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                Message message = new Message();
                message.obj = responseData;
                message.what = SUCCESS_TEXT;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                String f_time = jsonObject.getString("f_time");
                String f_title = jsonObject.getString("f_title");
                String f_content = jsonObject.getString("f_content");
                int f_times = jsonObject.getInt("f_times");
                String f_img = jsonObject.getString("f_img");
                if (f_img.equals("")) {
                    Forum f = new Forum(id, IMG_URL + user_img, username, f_time, f_title, f_content, f_times);
                    forums[i] = f;
                } else {
                    Forum f = new Forum(id, IMG_URL + user_img, username, f_time, f_title, f_content, f_times, IMG_URL + f_img);
                    forums[i] = f;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
