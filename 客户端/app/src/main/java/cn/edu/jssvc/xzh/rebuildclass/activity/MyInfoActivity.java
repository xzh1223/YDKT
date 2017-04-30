package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/2/15.
 *  用户信息查看更新界面
 *      发送网络请求读取到用户信息并添加到控件中
 *      点击提交按钮，将控件中的数据读取出来，并通过网络请求发送到服务器
 */

public class MyInfoActivity extends BaseActivity {

    // 服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readUserInfo";
    // 服务器请求地址
    private String URL_POST = "http://118.190.10.181:8080/YDKT/insertUserInfo";
    private String idFromLog;
    private String nameFromLog;
    private static final int SUCCESS_TEXT = 200;
    private static final int SUCCESS_POST_TEXT = 2001;
    private static final int FAILED_POST_TEXT = 5001;
    private General[] myInfo;
    private TextView myinfo_name;
    private EditText myinfo_sex;
    private EditText myinfo_age;
    private EditText myinfo_profession;
    private EditText myinfo_phone;
    private EditText myinfo_email;
    private EditText myinfo_constellation;
    private EditText myinfo_home;
    private EditText myinfo_description;
    private String sex,phone,email,description;
    private String age;
    private String profession;
    private String constellation;
    private String home;

    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_TEXT:
                    String responseData = msg.obj.toString();
                    parseJSON(responseData);
                    setView();
                    break;
                case SUCCESS_POST_TEXT:
                    showDialog2(getResources().getString(R.string.update_succeed));
                    break;
                case FAILED_POST_TEXT:
                    showDialog(getResources().getString(R.string.update_failed));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     *  将用户信息数据添加到控件中
     */
    private void setView() {
        myinfo_sex.setText(myInfo[0].getSex());
        myinfo_age.setText(String.valueOf(myInfo[0].getAge()));
        myinfo_profession.setText(myInfo[0].getProfession());
        myinfo_phone.setText(myInfo[0].getPhone());
        myinfo_email.setText(myInfo[0].getEamil());
        myinfo_constellation.setText(myInfo[0].getConstellation());
        myinfo_home.setText(myInfo[0].getHome());
        myinfo_description.setText(myInfo[0].getDescription());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myinfo);
        initMenu();
        getDataFromShared();
        initView();
        sendRequestWithOkHttp();
        postMyInfo();
    }

    /**
     *  保存用户信息到数据库
     */
    private void postMyInfo() {
        findViewById(R.id.myinfo_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromView();
                sendRequestWithOkHttp2();
            }
        });
    }

    /**
     *  更新用户信息的网络请求
     */
    private void sendRequestWithOkHttp2() {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", idFromLog)
                    .add("i_sex",sex)
                    .add("i_age",age)
                    .add("i_profession",profession)
                    .add("i_phone",phone)
                    .add("i_email",email)
                    .add("i_constellation",constellation)
                    .add("i_home",home)
                    .add("i_description",description)
                    .build();
            HttpUtil.sendOkHttpRequest(URL_POST, requestBody, new Callback() {

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
                    if (resp.equals("200")) {
                        message.what = SUCCESS_POST_TEXT;
                    }else if (resp.equals("500")) {
                        message.what = FAILED_POST_TEXT;
                    }
                    handler.sendMessage(message);// 将message发送到Handler
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取控件中的数据
     */
    private void getDataFromView() {
        sex = myinfo_sex.getText().toString();
        age = myinfo_age.getText().toString();
        profession = myinfo_profession.getText().toString();
        phone = myinfo_phone.getText().toString();
        email = myinfo_email.getText().toString();
        constellation = myinfo_constellation.getText().toString();
        home = myinfo_home.getText().toString();
       description = myinfo_description.getText().toString();
    }

    /**
     *  初始化控件
     */
    private void initView() {
        myinfo_name = (TextView) findViewById(R.id.myinfo_name);
        myinfo_sex = (EditText) findViewById(R.id.myinfo_sex);
        myinfo_age = (EditText) findViewById(R.id.myinfo_age);
        myinfo_profession = (EditText) findViewById(R.id.myinfo_profession);
        myinfo_phone = (EditText) findViewById(R.id.myinfo_phone);
        myinfo_email = (EditText) findViewById(R.id.myinfo_email);
        myinfo_constellation = (EditText) findViewById(R.id.myinfo_constellation);
        myinfo_home = (EditText) findViewById(R.id.myinfo_home);
        myinfo_description = (EditText) findViewById(R.id.myinfo_description);
        myinfo_name.setText(nameFromLog);
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
        nameFromLog = mSharedPreferences.getString("nameFromLog", "");
    }

    /**
     * 解析从服务器获取的JSON格式数据并存放到Fruit数组中
     */
    private void parseJSON(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            myInfo = new General[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sex = jsonObject.getString("sex");
                int age = jsonObject.getInt("age");
                String profession = jsonObject.getString("profession");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String constellation = jsonObject.getString("constellation");
                String home = jsonObject.getString("home");
                String description = jsonObject.getString("description");
                General g = new General(sex, age, profession, phone, email, constellation, home, description);
                myInfo[i] = g;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取用户信息的网络请求
     */
    private void sendRequestWithOkHttp() {

        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", idFromLog)
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
                    message.what = SUCCESS_TEXT;
                    message.obj = resp;
                    handler.sendMessage(message);// 将message发送到Handler
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
