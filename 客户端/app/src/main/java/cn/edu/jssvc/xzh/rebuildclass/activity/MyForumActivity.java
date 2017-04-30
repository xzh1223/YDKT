package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.MyForumAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;
import cn.edu.jssvc.xzh.rebuildclass.util.GlideCacheUtil;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/21.
 * <p>
 * 与我相关/我的帖子
 */
public class MyForumActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Forum> myforumList = new ArrayList<>();
    private MyForumAdapter myadapter;
    // 论坛列表的服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readMyForum";
    // 从我的收藏中取消
    private static final String DELETE_URL = "http://118.190.10.181:8080/YDKT/deleteMyForum";
    /* private String URL = "http://192.168.1.101:8080/YDKT/readForumList";*/
    //http://192.168.1.101:8080/YDKT/readMyForum?user_id=3
    // 图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/";

    private static final int SUCCESS_TEXT = 200;

    private Forum[] forums = {};
    private String idFromLog = "";
    private String id = "";
    private String mResponse = "";
    private static final int DELETE_SUCCEED = 0x02;
    private static final int DELETE_FAILED = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myforum);
        getSharedPreferences();
        initMenu();
        clearGlide();
        sendRequestWithOkHttp();
        initView();
    }

    /**
     * 清楚Glide数据
     */
    public void clearGlide() {
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        GlideCacheUtil.getInstance().clearImageDiskCache(this);
        GlideCacheUtil.getInstance().clearImageMemoryCache(this);
    }

    private void getSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
    }

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
                    initMyForum();
                    break;
                case DELETE_FAILED:
                    deleteResult("删除失败");
                    break;
                case DELETE_SUCCEED:
                    deleteResult("删除成功");
                    break;
                default:
                    break;
            }
        }
    };

    /*    //评论后刷新当前页面
        private void refreshCurrent(){
            //评论后刷新当前页面


            myforumList.clear();
            sendRequestWithOkHttp();
        }*/
    public void deleteResult(String string) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setMessage(string)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refreshCurrent();
                    }
                })
                .show();

    }

    //删除帖子后刷新当前页面
    private void refreshCurrent() {
     /*   hList.clear();*/
        myforumList.clear();
        sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new MyThread()).start();
    }

    /**
     * 初始化数据
     */
    private void initMyForum() {
        myadapter = new MyForumAdapter(myforumList);
        recyclerView.setAdapter(myadapter);

        myforumList.clear();
        for (int i = 0; i < forums.length; i++) {
            myforumList.add(forums[i]);
        }

    }

    /**
     * 初始化布局
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.forum_recycler_view);
        // 设置单列布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 创建网络请求线程
     */
    public class MyThread implements Runnable {
        @Override
        public void run() {
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", idFromLog)
                    .build();
            HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Message message = new Message();
                    message.obj = responseData;
                    message.what = SUCCESS_TEXT;
                    handler.sendMessage(message);
                }

            });
        }
    }

    /* *
     *  从与我相关中删除帖子
     */

    public void deleteMyTezi(int tezi_id) {
        id = String.valueOf(tezi_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", idFromLog)
                            .add("id", id)
                            .build();
                    HttpUtil.sendOkHttpRequest(DELETE_URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            if (mResponse.equals("200")) {
                                Log.i("删除成功", "222");
                                handler.sendEmptyMessage(DELETE_SUCCEED);
                            } else {
                                handler.sendEmptyMessage(DELETE_FAILED);
                            }
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
