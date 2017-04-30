package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by xzh on 2017/1/16.
 * 登录界面
 * 使用OkHttp用于网络请求，注意添加okhttp依赖包
 *
 *  （接口）服务器端返回的状态码：   0  - 请求失败
 *                                  200 - 登录成功
 *                                  500 - 用户名或密码错误
 */
public class LoginActivity extends BaseActivity {
    private EditText usernameET;
    private EditText passwordET;
    private CheckBox rememberPass;
    private TextView register;
    private SharedPreferences pref;
    private String username;
    private String password;
    //登录按钮
    private Button btn_login;
    private LoginActivity context = LoginActivity.this;
    // 登录请求服务器地址
    private String URL = "http://118.190.10.181:8080/YDKT/login";
    // 图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/";
    // 创建登录等待框
    private ProgressDialog dialog;
    // 创建登录失败提示框
    private AlertDialog.Builder alertDialog;
    // 创建用户名或密码错误提示框
    private AlertDialog.Builder NOPMAlertDialog;
    // 登录请求失败状态码
    private static final int FAILED_TEXT = 0;
    // 登录成功状态码
    private static final int LOGIN_FLAG_SUCCESS = 200;
    // 用户名或密码错误状态码
    private static final int LOGIN_FLAG_FAILED = 500;
    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FAILED_TEXT:
                    alertDialog.show();
                    break;
                case LOGIN_FLAG_SUCCESS:
                    String responseData = msg.obj.toString();
                    parseJSON(responseData);
                    savedUsers();

                    Intent MainIntent = new Intent(context, MainActivity.class);
                    startActivity(MainIntent);
                    dialog.dismiss();
                    finish();
                    break;
                case LOGIN_FLAG_FAILED:
                    NOPMAlertDialog.show();
                    break;
                default:
                    break;
            }
        }
    };
    private TextView recoverTV;

    /**
     *  保存用户数据到Share
     */
    private void savedUsers() {
        int idFromLog = general[0].getId();
        String nameFromLog = general[0].getName();
        String imageFromLog = general[0].getImageId();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("idFromLog",String.valueOf(idFromLog));
        editor.putString("nameFromLog",nameFromLog);
        editor.putString("imageFromLog",imageFromLog);
        editor.putString("passFromLog",passwordET.getText().toString().trim());

        if (rememberPass.isChecked()) {
            editor.putBoolean("isRemembered", true);
        }else {
            editor.putBoolean("isRemembered", false);
        }
        editor.commit();
    }

    private General[] general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
        setRemember();
        setListener();
        dialogLogin();
        failedDialog();
        nameOrPassMissDialog();

    }

    /**
     * 设置记住用户名和密码
     */
    private void setRemember() {
        if (isRemembered()) {
            String nameFromLog = mSharedPreferences.getString("nameFromLog","");
            String passFromLog = mSharedPreferences.getString("passFromLog","");
            usernameET.setText(nameFromLog);
            passwordET.setText(passFromLog);
            rememberPass.setChecked(true);
        }
    }

    /**
     *  获取记住密码的状态
     */
    private boolean isRemembered() {
        return mSharedPreferences.getBoolean("isRemembered",false);
    }

    /**
     * 初始化数据
     * 获取输入的用户名和密码
     */
    private void initData() {
        username = usernameET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        Log.e("nameAndPass",username+"---"+password);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        register = (TextView) findViewById(R.id.register);
        recoverTV = (TextView) findViewById(R.id.tv_recover);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });
        // 登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                doLogin();

            }
        });
        // 忘记密码
        recoverTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recIntent = new Intent(LoginActivity.this,RecoverActivity.class);
                startActivity(recIntent);
            }
        });
    }

    /**
     * 登录进程提示框
     */
    private void dialogLogin() {
        // 提示框
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.tip);
        dialog.setMessage("正在登陆，请稍后...");
        dialog.setCancelable(true);
    }

    /**
     * 请求登录
     * 使用okHttp，设置请求参数，
     */
    private void doLogin() {
        //  显示提示弹窗
        dialog.show();
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
            HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {

                /**
                 * 请求失败
                 */
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = FAILED_TEXT;
                    handler.sendMessage(message);// 将message发送到Handler
                }

                /**
                 * 请求成功
                 */
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    Message message = new Message();
                    if (resp.equals("500")) {
                        message.what = LOGIN_FLAG_FAILED;
                    } else {
                        message.what = LOGIN_FLAG_SUCCESS;
                        message.obj = resp;
                    }
                    handler.sendMessage(message);// 将message发送到Handler
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  解析JSON数据
     * @param jsonData
     */
    private void parseJSON(String jsonData) {
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            general = new General[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String user_img = jsonObject.getString("user_img");
                String username = jsonObject.getString("username");
                General g = new General(id,username,IMG_URL+user_img);
                general[i] = g;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求失败提示框
     */
    private void failedDialog() {
        dialog.dismiss();
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.tip);
        alertDialog.setMessage(R.string.login_failed);
        alertDialog.setCancelable(false);
        //  取消按钮
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        //  确认按钮
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doLogin();
            }
        });
    }

    /**
     * 用户名密码错误提示框
     */
    private void nameOrPassMissDialog() {
        dialog.dismiss();
        NOPMAlertDialog = new AlertDialog.Builder(context);
        NOPMAlertDialog.setTitle(R.string.tip);
        NOPMAlertDialog.setMessage(R.string.name_pass_miss);
        NOPMAlertDialog.setCancelable(false);

        //  确认按钮
        NOPMAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
    }
}
