package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ClasstitleAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/2/8.
 * 课程标题界面
 * 使用OkHttp发送网络请求之后，将获取的数据存放到General中并通过适配器进行加载显示、操作
 */
public class ClasstitleActivity extends BaseActivity {
    private static final int ADD_INTO_MYCLASS_FAILED = 0x00;
    private static final int ADD_INTO_MYCLASS_SUCCEED = 0x01;
    private static final int UPDATE = 0x02;
    private static final int NO_DATE = 0x05;
    // id，根据id去查询科目对应的课程名
    private String proj_id;
    // 添加到我的课程按钮
    private TextView add_into_myclass;
    private List<General> classList = new ArrayList<>();
    private ClasstitleAdapter adapter;
    // 读取课程章节名
    private String URL = "http://118.190.10.181:8080/YDKT/readClassTitle";
    // 添加我的课程
    private String URL_ADD_MYCLASS = "http://118.190.10.181:8080/YDKT/saveMyProject";
    // 科目对应图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/img/";
    // 是否已经添加到我的课程的识别码
    private boolean isAdded = false;
    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_INTO_MYCLASS_FAILED:
                    showDialog(getResources().getString(R.string.collection_failed));
                    break;
                case ADD_INTO_MYCLASS_SUCCEED:
                    showDialog(getResources().getString(R.string.collection_succeed));
                    add_into_myclass.setText(getResources().getString(R.string.collectioned));
                    add_into_myclass.setClickable(false);
                    isAdded = true;
                    break;
                case UPDATE:
                    adapter.notifyDataSetChanged();
                    setListener();
                    break;
                case NO_DATE:
                    showDialog2(getResources().getString(R.string.null_data));
                    break;
                default:
                    break;
            }
        }
    };

    private RecyclerView recyclerView;
    private String idFromLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_class_title);

        // 获取从上一个界面通过Intent传递的数据
        proj_id = getDataFromProj();
        getDataFromShared();
        initMenu();
        sendRequestWithOkHttp();
        initView();
        initClasss();
        setListener();
    }

    public String getDataFromProj() {
        return getIntent().getStringExtra("proj_id");
    }

    /**
     * 设置添加到我的课程的点击事件
     * 判断是否已经将课程添加到我的课程
     * 1、是：将按钮文字转变为“已收藏”，并设置文字不可点击
     * 2、否：按钮文字转变为“收藏”，点击发送网络请求将用户ID和课程ID发送到服务器，
     * 并将文字转变为“已收藏”，并设置不可点击
     */
    private void setListener() {
        if (isAdded) {
            add_into_myclass.setText(getResources().getString(R.string.collectioned));
            add_into_myclass.setClickable(false);
        } else {
            add_into_myclass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequest();
                }
            });
        }
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
    }

    /**
     * 发送网络请求
     * 添加到我的课程数据表中
     */
    private void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("proj_id", proj_id)
                            .add("user_id", idFromLog)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL_ADD_MYCLASS, requestBody, new Callback() {

                        // 请求失败
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        // 请求成功
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resp = response.body().string();
                            Log.e("------>", resp);
                            if (resp.equals("500")) {
                                handler.sendEmptyMessage(ADD_INTO_MYCLASS_FAILED);
                            } else if (resp.equals("200")) {
                                handler.sendEmptyMessage(ADD_INTO_MYCLASS_SUCCEED);
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
     * 初始化布局
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 设置单列布局
        GridLayoutManager layoutManager = new GridLayoutManager(ClasstitleActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        // 添加到我的课程
        add_into_myclass = (TextView) findViewById(R.id.add_into_myclass);
    }

    /**
     * 发送网络请求，获取到课程章节列表
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("proj_id", proj_id)
                            .add("user_id", idFromLog)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {

                        // 请求失败
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        // 请求成功
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resp = response.body().string();
                            parseJSON(resp);
                            handler.sendEmptyMessage(UPDATE);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置recyclerView适配器
     */
    private void initClasss() {
        adapter = new ClasstitleAdapter(classList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 解析从服务器获取的JSON格式数据并存放到Fruit数组中
     */
    private void parseJSON(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            // 获取收藏标记位，并进行判断
            int flag = jsonObject.getInt("flag");
            // 获取课程数据
            String data = jsonObject.getString("data");
            if (data.equals("[]")) {
                handler.sendEmptyMessage(NO_DATE);
            }
            JSONArray jsonArr = new JSONArray(data);
            General[] generals = new General[jsonArr.length()];
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                if (flag == 200) {
                    isAdded = true;
                    General general = new General(jsonObj.getInt("id"),
                            jsonObj.getString("c_name"), IMG_URL + jsonObj.getString("c_img"),
                            jsonObj.getInt("curPro"), jsonObj.getInt("allPro"));
                    generals[i] = general;
                } else {
                    isAdded = false;
                    General general = new General(jsonObj.getInt("id"),
                            jsonObj.getString("c_name"), IMG_URL + jsonObj.getString("c_img"));
                    generals[i] = general;
                }
//                Log.e("------->",generals[i].getProgressText()+"");
                classList.add(generals[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
