package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.MyClassAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/14.
 * <p>
 * 我的课程界面
 * <p>
 * 发送网络请求到服务器获取到我的课程表中的数据并显示RecyclerView中
 * 点击可跳转到课程内容界面
 * 长按弹出取消我的课程提示框，点击确定发送网络请求删除数据库中对应项的数据实现取消我的课程收藏
 */
public class MyClassActivity extends BaseActivity {
    private static final int NO_DATE = 0x00;
    private static final int UPDATE = 0x01;
    private static final int DELETE_SUCCEED = 0x02;
    private static final int DELETE_FAILED = 0x03;
    private RecyclerView recyclerView;
    private Thread mThread;
    private String idFromLog;
    // 读取我的收藏
    private static final String URL = "http://118.190.10.181:8080/YDKT/readMyClass";
    // 从我的收藏中取消
    private static final String DELETE_URL = "http://118.190.10.181:8080/YDKT/deleteMyClass";
    // 图片地址
    private static final String IMG_URL = "http://118.190.10.181:8080/YDKT/";
    private String mResponse = "";
    private List<General> mList = new ArrayList<General>();
    private Handler mHandler;
    private MyClassAdapter mAdapter;
    private String proj_id = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myclass);
        initToolbar();
        handleMessage();
        setRequest();
        initView();
        getDataFromShared();
        mThread.start();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case NO_DATE:
                        showDialog(getResources().getString(R.string.null_data));
                        break;
                    case UPDATE:
                        mAdapter.notifyDataSetChanged();
                        break;
                    case DELETE_FAILED:
                        showDialog2(getResources().getString(R.string.cancell_failed));
                        break;
                    case DELETE_SUCCEED:
                        showDialog(getResources().getString(R.string.cancelled));
                        //删除成功之后刷新数据
                        mList.clear();

                        new Thread(mThread).start();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
    }

    /**
     * 发送网络请求，获取我的课程
     */
    public void setRequest() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", idFromLog)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            Log.e("---->", mResponse);
                            if (mResponse.equals("[]") || mResponse.equals("") || mResponse == null) {
                                mHandler.sendEmptyMessage(NO_DATE);
                            } else {
                                try {
                                    parseJSON();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            mHandler.sendEmptyMessage(UPDATE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析JSON格式数据
     */
    private void parseJSON() throws JSONException {
        JSONArray jsonArray = new JSONArray(mResponse);
        General[] generals = new General[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            General general = new General(jsonObject.getInt("id"),
                    jsonObject.getString("pro_name"), IMG_URL + jsonObject.getString("pro_image"));
            generals[i] = general;
            mList.add(generals[i]);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list_myclass);
        // 设置两列布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyClassAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 点击返回按钮
     * 默认按钮的id为android.R.id.home
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    /**
     * 从我的课程中取消我的收藏
     */
    public void deleteMyClass(int id) {
        proj_id = String.valueOf(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", idFromLog)
                            .add("proj_id", proj_id)
                            .build();
                    HttpUtil.sendOkHttpRequest(DELETE_URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            if (mResponse.equals("200")) {
                                mHandler.sendEmptyMessage(DELETE_SUCCEED);
                            } else {
                                mHandler.sendEmptyMessage(DELETE_FAILED);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
