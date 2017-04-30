package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
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
 * Created by xzh on 2017/2/18.
 * <p>
 * 修改密码
 * <p>
 * 获取到当前用户id和密码
 * 根据规则判断用户输入的新密码是否有效
 * 发送网络请求将用户id和新密码上传到服务器
 */
public class ResetPassActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText old_pass;
    private EditText new_pass;
    private EditText rebuild_pass;
    private Button btn_reset;
    private String idFromLog;
    private String nameFromLog;
    private String passFromLog;

    private String old_pass_text;
    private String new_pass_text;
    private String rebuild_pass_text;
    // 重置密码地址
    private static final String URL = "http://118.190.10.181:8080/YDKT/updatePassword";

    private String mResponse;
    private String new_password;

    // 用户输入规则判断位
    private boolean oldPass = false;
    private boolean newPass = false;
    private boolean rebuildPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resetpass);
        getDataFromShared();
        initMenu();
        initView();
        setListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        btn_reset.setOnClickListener(this);
        old_pass.setOnFocusChangeListener(this);
        new_pass.setOnFocusChangeListener(this);
        rebuild_pass.setOnFocusChangeListener(this);
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
        nameFromLog = mSharedPreferences.getString("nameFromLog", "");
        passFromLog = mSharedPreferences.getString("passFromLog", "");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        old_pass = (EditText) findViewById(R.id.old_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        rebuild_pass = (EditText) findViewById(R.id.rebuild_pass);
        btn_reset = (Button) findViewById(R.id.btn_reset);
    }

    /**
     * 登录按钮点击
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                if (oldPass && newPass && rebuildPass) {
                    rebuild_pass_text = rebuild_pass.getText().toString().toString().trim();
                    resetPass(rebuild_pass_text);
                    showDialog2(getResources().getString(R.string.reset_succeed));
                } else {
                    showDialog(getResources().getString(R.string.reset_failed));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送网络请求，修改密码
     */
    private void resetPass(String rebuild_pass_text) {
        new_password = rebuild_pass_text;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", idFromLog)
                            .add("new_password", new_password)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            Log.e("---->", mResponse);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置输入框焦点事件
     *
     * @param view
     * @param b
     */
    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {

        } else {
            switch (view.getId()) {
                case R.id.old_pass:
                    old_pass_text = old_pass.getText().toString().trim();
                    if (!old_pass_text.equals(passFromLog)) {
                        showDialog(getResources().getString(R.string.input_error));
                        oldPass = false;
                    } else {
                        oldPass = true;
                    }
                    break;
                case R.id.new_pass:
                    new_pass_text = new_pass.getText().toString().trim();
                    Pattern pattern = Pattern.compile("\\w{6,18}");
                    Matcher matcher = pattern.matcher(new_pass_text);
                    if (!matcher.matches()) {
                        showDialog(getResources().getString(R.string.input_style));
                        newPass = false;
                    } else {
                        newPass = true;
                    }
                    break;
                case R.id.rebuild_pass:
                    rebuild_pass_text = rebuild_pass.getText().toString().trim();
                    new_pass_text = new_pass.getText().toString().trim();
                    if (!rebuild_pass_text.equals(new_pass_text)) {
                        showDialog(getResources().getString(R.string.input_diff));
                        rebuildPass = false;
                    } else {
                        rebuildPass = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
