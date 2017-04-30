package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/21.
 * <p>
 * 找回密码
 * <p>
 * 根据用户输入的用户名和手机号，发送网络请求，获取到用户密码
 */
public class RecoverActivity extends BaseActivity {

    private static final int RECOVER_FAILED = 0x00;
    private static final int RECOVER_SUCCEED = 0x01;
    private EditText usernameET;
    private EditText phoneET;
    private Button btn_recover;
    private String username;
    private String phone;
    private Thread mThread;
    private static final String URL = "http://118.190.10.181:8080/YDKT/recoverPassword";
    private String mResponse = "";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recoverpass);
        initMenu();
        initView();
        setListener();
        handleMessage();
    }

    /**
     * 消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case RECOVER_FAILED:
                        btn_recover.setClickable(true);
                        showDialog(getResources().getString(R.string.recover_failed));
                        break;
                    case RECOVER_SUCCEED:
                        btn_recover.setClickable(true);
                        Toast.makeText(RecoverActivity.this, "您的密码为：" + mResponse, Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送网络请求，获取用户密码
     */

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", username)
                        .add("phone", phone)
                        .build();
                HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mResponse = response.body().string();
                        if (mResponse.equals("500")) {
                            mHandler.sendEmptyMessage(RECOVER_FAILED);
                        } else {
                            mHandler.sendEmptyMessage(RECOVER_SUCCEED);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 设置监听器
     */
    private void setListener() {
        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameET.getText().toString().trim();
                phone = phoneET.getText().toString().trim();
                if (username.equals("") || username == null) {
                    showDialog(getResources().getString(R.string.username_null));
                } else if (phone.equals("") || phone == null) {
                    showDialog(getResources().getString(R.string.phone_null));
                } else {
                    new Thread(runnable).start();
                    // 设置按钮不可点击
                    btn_recover.setClickable(false);
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        usernameET = (EditText) findViewById(R.id.username);
        phoneET = (EditText) findViewById(R.id.phone);
        btn_recover = (Button) findViewById(R.id.btn_recover);
    }
}
