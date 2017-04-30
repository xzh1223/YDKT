package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/18.
 * <p>
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    private static final int SUBMIT_SUCCEED = 0x00;
    private static final int SUBMIT_FAILED = 0x01;
    private EditText et_opinion;
    private EditText et_connection;
    private Button btn_sendoption;
    private String mOpinion = "";
    private String mConnection = "";
    private Thread mThread;
    private static final String URL = "http://118.190.10.181:8080/YDKT/feedback";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);
        initMenu();
        initView();
        setListener();
        setRequest();
        handleMessage();
    }

    /**
     *  消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case SUBMIT_SUCCEED:
                        showDialog2(getResources().getString(R.string.submit_succeed));
                        break;
                    case SUBMIT_FAILED:
                        showDialog(getResources().getString(R.string.submit_failed));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送网络请求，提交意见
     */
    private void setRequest() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("feedbackinf", mOpinion)
                            .add("phone", mConnection)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String mResponse = response.body().string();
                            if (mResponse.equals("200")) {
                                mHandler.sendEmptyMessage(SUBMIT_SUCCEED);
                            }else {
                                mHandler.sendEmptyMessage(SUBMIT_FAILED);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 提交意见
        btn_sendoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpinion = et_opinion.getText().toString().trim();
                mConnection = et_connection.getText().toString().trim();
                mThread.start();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_opinion = (EditText) findViewById(R.id.et_opinion);
        et_connection = (EditText) findViewById(R.id.et_connection);
        btn_sendoption = (Button) findViewById(R.id.btn_sendoption);
    }
}
