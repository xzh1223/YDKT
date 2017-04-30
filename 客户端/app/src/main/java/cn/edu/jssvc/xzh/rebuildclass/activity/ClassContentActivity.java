package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ClasscontentAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/2/9.
 * 课程内容界面
 * 这里和前面的几个界面差不多
 * 注意网络请求部分：从服务器中获取的数据格式为Json数据
 * 例如：[{"c_content":"img_proj/img.png,img_proj/img.png,img_proj/img.png,img_proj/img.png","id":1}]
 * 首先通过json数据解析获取到c_content对应的数据，然后通过“，”分割符进行切割，获取到独立的一条一条记录，
 * 然后将获取到的数据存放到General实体类中，再在适配器中通过Glide加载图片
 */

public class ClassContentActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private String UPDATE_PROGRESS_URL = "http://118.190.10.181:8080/YDKT/updateProg";
    private String tit_id;
    private List<General> viewList = new ArrayList<>();
    private ClasscontentAdapter mAdapter;
    private ViewPager viewPager;
    private General[] classcontents;
    // 服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readClassContent";
    // 图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/img/img_proj/";
    private static final int SUCCESS_TEXT = 200;
    private static final int FAILED_TEXT = 500;
    // 总页数/总进度
    private int allProg = 0;
    // 当前页/当前进度
    private int currProg = 1;
    private TextView currProgText;
    private TextView allProgText;
    private String idFromLog;
    private String mResponse = "";
    private SharedPreferences mSharedPreferences;
    private boolean flag = false;
    private int current;
    private String proj_id;

    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_TEXT:
                    String responseData = msg.obj.toString();
                    parseJSON(responseData);
                    setData();
                    setViewPager();
                    mAdapter.notifyDataSetChanged();
                    break;
                case FAILED_TEXT:
                    showDialog(getResources().getString(R.string.null_data));
                default:
                    break;
            }
        }
    };
    private double nLenStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 横屏显示
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.layout_class_content);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 获取从上一个界面传递的id
        getDataFromIntent();
        setURL();
        getDataFromShared();
        initMenu();
        initView();
        sendRequestWithOkHttp();

    }

    /**
     *  设置URL网络图片地址
     */
    private void setURL() {
        switch (proj_id){
            case "1":
                IMG_URL = IMG_URL+"android/";
                break;
            case "2":
                IMG_URL = IMG_URL+"c/";
                break;
            case "3":
                IMG_URL = IMG_URL+"jsp/";
                break;
            case "4":
                IMG_URL = IMG_URL+"empl/";
                break;
            case "5":
                IMG_URL = IMG_URL+"intr/";
                break;
            case "6":
                IMG_URL = IMG_URL+"elec/";
                break;
            case "7":
                IMG_URL = IMG_URL+"sql/";
                break;
            case "8":
                IMG_URL = IMG_URL+"java/";
                break;
        }
    }

    /**
     * 获取从上一个界面传递的数据
     */
    private void getDataFromIntent() {
        tit_id = getIntent().getStringExtra("tit_id");
        current = getIntent().getIntExtra("current", 0);
        proj_id = getIntent().getStringExtra("proj_id");
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        idFromLog = mSharedPreferences.getString("idFromLog", "");
    }

    /**
     * 发送网络请求
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("tit_id", tit_id)
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
                                message.what = FAILED_TEXT;
                            } else {
                                message.what = SUCCESS_TEXT;
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
     * 为ViewPager设置相应的参数
     */
    private void setViewPager() {
        mAdapter = new ClasscontentAdapter(ClassContentActivity.this, viewList);

        viewPager.setAdapter(mAdapter);

        // 设置页面切换
        viewPager.addOnPageChangeListener(this);
        // 设置默认页
        viewPager.setCurrentItem(current-1);
    }

    /**
     * 初始化控件
     * virepager和当前进度、总进度
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        currProgText = (TextView) findViewById(R.id.currProg);
        allProgText = (TextView) findViewById(R.id.allProg);
    }

    /**
     * 解析从服务器获取的JSON格式数据并存放到Fruit数组中
     */
    private void parseJSON(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            int id = jsonObject.getInt("id");
            String c_content = jsonObject.getString("c_content");
            // 字符串分割
            String[] params = c_content.split(",");
            // 将图片数量保存起来
            allProg = params.length;

            classcontents = new General[params.length];
            for (int i = 0; i < params.length; i++) {
                General g = new General(id, IMG_URL + params[i]);
                classcontents[i] = g;
                viewList.add(classcontents[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击返回按钮
     * 默认按钮的id为android.R.id.home
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                intentUI();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currProg = position + 1;
        setData();
        sendRequestOnProg();
    }

    /**
     * 发送当前进度到服务器
     */
    private void sendRequestOnProg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("title_id", tit_id)
                            .add("user_id", idFromLog)
                            .add("curPro", String.valueOf(currProg))
                            .add("allPro", String.valueOf(allProg))
                            .build();
                    HttpUtil.sendOkHttpRequest(UPDATE_PROGRESS_URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            Log.e("----->", mResponse);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置进度条数据
     */
    private void setData() {
        currProgText.setText("" + currProg);
        allProgText.setText("" + allProg);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                flag = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                flag = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (viewPager.getCurrentItem() == viewPager.getAdapter()
                        .getCount() - 1 && !flag) {
                    showDialog(getResources().getString(R.string.is_final));
                }
                flag = true;
                break;
        }
    }

    @Override
    public void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intentUI();
                    }
                })
                .show();
    }

    /**
     * 重置返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intentUI();
        }
        return false;
    }

    //  跳转页面
    private void intentUI() {
        Intent intent = new Intent(ClassContentActivity.this, ClasstitleActivity.class);
        intent.putExtra("proj_id", proj_id);
        startActivity(intent);
        finish();
    }

}
