package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/19.
 * <p>
 * 用户注册
 */
public class RegisterActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private static final int REGISTER_SUCCEED = 0x00;
    private static final int REGISTER_FAILED = 0x01;
    private EditText et_username;
    private EditText et_pass;
    private EditText et_rebuild_pass;
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_code;
    private Button btn_register;
    private boolean bUsername = false;
    private boolean bPassword = false;
    private boolean bRebuildPass = false;
    private boolean bPhone = false;
    private boolean bCode = false;
    private String username = "";
    private String password = "";
    private String phone = "";
    private static final String URL = "http://118.190.10.181:8080/YDKT/register";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initMenu();
        initView();
        handleMessage();
        setListener();
        setCode();
    }

    /**
     *  消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case REGISTER_SUCCEED:
                        showDialog2(getResources().getString(R.string.register_success));
                        break;
                    case REGISTER_FAILED:
                        showDialog(getResources().getString(R.string.register_failed));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取随机数,设置验证码
     */
    private void setCode() {
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        int c = random.nextInt(10);
        int d = random.nextInt(10);
        String code = String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d);
        tv_code.setText(code);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 设置焦点事件
        et_username.setOnFocusChangeListener(this);
        et_pass.setOnFocusChangeListener(this);
        et_rebuild_pass.setOnFocusChangeListener(this);
        et_phone.setOnFocusChangeListener(this);
        et_code.setOnFocusChangeListener(this);

        // 设置点击事件
        tv_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);    // 用户名
        et_pass = (EditText) findViewById(R.id.et_pass);        // 密码
        et_rebuild_pass = (EditText) findViewById(R.id.et_rebuild_pass);    // 确认密码
        et_phone = (EditText) findViewById(R.id.et_phone);      // 手机号
        et_code = (EditText) findViewById(R.id.et_code);        // 验证码输入框
        tv_code = (TextView) findViewById(R.id.tv_code);        // 验证码生成
        btn_register = (Button) findViewById(R.id.btn_register);    // 注册按钮
    }

    /**
     * 焦点事件
     *
     * @param view
     * @param b
     */
    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            // 获取焦点
        } else { //  失去焦点
            switch (view.getId()) {
                // 用户名
                case R.id.et_username:
                    String user_name = et_username.getText().toString().trim();
                    if (user_name.length() < 2 || user_name.length() > 12) {
                        showDialog(getResources().getString(R.string.username_error));
                        bUsername = false;
                    } else {
                        bUsername = true;
                    }
                    break;
                // 密码
                case R.id.et_pass:
                    String password = et_pass.getText().toString().trim();
                    Pattern pattern = Pattern.compile("\\w{6,18}");
                    Matcher matcher = pattern.matcher(password);
                    if (!matcher.matches()) {
                        showDialog(getResources().getString(R.string.input_style));
                        bPassword = false;
                    } else {
                        bPassword = true;
                    }
                    break;
                // 确认密码
                case R.id.et_rebuild_pass:
                    String rebuild_pass = et_rebuild_pass.getText().toString().trim();
                    String password1 = et_pass.getText().toString().trim();
                    if (!rebuild_pass.equals(password1)) {
                        showDialog(getResources().getString(R.string.input_diff));
                        bRebuildPass = false;
                    } else {
                        bRebuildPass = true;
                    }
                    break;
                // 手机号
                case R.id.et_phone:
                    String phone = et_phone.getText().toString().trim();
                    Pattern pattern1 = Pattern.compile("[1]\\d{10}");
                    Matcher matcher1 = pattern1.matcher(phone);
                    if (!matcher1.matches()) {
                        showDialog(getResources().getString(R.string.phone_error));
                        bPhone = false;
                    } else {
                        bPhone = true;
                    }
                    break;
                // 验证码
                case R.id.et_code:
                    String code = et_code.getText().toString().trim();
                    if (!tv_code.getText().equals(code)) {
                        showDialog(getResources().getString(R.string.code_error));
                        bCode = false;
                    } else {
                        bCode = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 验证码切换
            case R.id.tv_code:
                setCode();
                break;
            // 注册
            case R.id.btn_register:
                btn_register.setFocusable(true);
                btn_register.setFocusableInTouchMode(true);
                btn_register.requestFocus();
                if (bUsername && bPassword && bRebuildPass && bPhone && bCode) {
                    username = et_username.getText().toString().trim();
                    password = et_pass.getText().toString().trim();
                    phone = et_phone.getText().toString().trim();
                    doRegister();
                }else {
                    showDialog(getResources().getString(R.string.reset_failed));
                }
                break;
        }
    }

    /**
     * 发送网络请求，注册
     */
    private void doRegister() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username",username)
                            .add("password",password)
                            .add("phone",phone)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("---->","FAILED");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String mResponse = response.body().string();
                            if (mResponse.equals("200")) {
                                mHandler.sendEmptyMessage(REGISTER_SUCCEED);
                            }else {
                                mHandler.sendEmptyMessage(REGISTER_FAILED);
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
