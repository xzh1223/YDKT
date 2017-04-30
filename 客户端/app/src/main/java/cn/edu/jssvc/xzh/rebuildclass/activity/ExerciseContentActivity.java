package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.pojo.Exercise;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/3/20.
 * <p>
 * 练习界面
 * <p>
 * 1、初始化控件
 * 2、点击按钮发送网络请求获取到数据，并将计数器+1，同时设置相关控件的属性
 * 3、将数据存放到控件中
 * 4、设置RadioGroup监听器获取当前选中项
 * 5、设置button点击事件进行判断用户选择项是否与答案相同
 */
public class ExerciseContentActivity extends BaseActivity {

    private static final int SETDATA = 0x00;
    private static final int NODATA = 0x01;
    private static final int SERVICE_FAILED = 0x02;
    private String exer_id = "";
    private TextView question;
    private RadioButton Achoose;
    private RadioButton Bchoose;
    private RadioButton Cchoose;
    private RadioButton Dchoose;
    private RadioGroup radioGroup;
    private EditText et_fill;
    private Button btn_q;
    private Button next;
    private static final String URL = "http://118.190.10.181:8080/YDKT/readExercise";
    private Thread mThread;
    private int count = 0;
    private String mResponse = "";
    private Exercise exercise;
    private Handler mHandler;
    private RadioButton radioButton;
    private TextView currProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exer_content);
        initMenu();
        initView();
        getDataFromIntent();
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
                    case SETDATA:
                        next.setClickable(true);
                        setData();
                        getCheckedFromRadioGroup();
                        break;
                    case NODATA:
                        showDialog2(getResources().getString(R.string.null_data));
                        break;
                    case SERVICE_FAILED:
                        showDialog(getResources().getString(R.string.service_gone));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 添加数据到控件中
     */
    private void setData() {
        question.setText(exercise.getQuestion());
        Achoose.setText(exercise.getAchoose());
        Bchoose.setText(exercise.getBchoose());
        Cchoose.setText(exercise.getCchoose());
        Dchoose.setText(exercise.getDchoose());
    }

    /**
     * 获取当前选中的状态
     */
    private void getCheckedFromRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, int i) {
                // 设置检查按钮显示并设置点击事件
                btn_q.setVisibility(View.VISIBLE);
                btn_q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                        if (radioButton.getText().toString().trim().equals(exercise.getAnswer())) {
                            Toast.makeText(ExerciseContentActivity.this, "回答正确！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ExerciseContentActivity.this, "回答错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 设置点击监听事件
     */
    private void setListener() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count < 30) {
                    next.setText("下一题");
                    next.setClickable(false);
                    // 清空选中数据,这两句顺序不能调换
                    radioGroup.clearCheck();
                    btn_q.setVisibility(View.INVISIBLE);

                    if (mThread != null) {
                        if (exercise == null) {
                            mHandler.sendEmptyMessage(NODATA);
                        }
                    }
                    // 发送网络请求
                    setRequest();
                    mThread.start();
                    // 设置当前进度
                    count++;
                    currProg.setText(count + "");
                } else {
                    showDialog2(getResources().getString(R.string.isfinal));
                    next.setText("已完成");
                }
            }
        });
    }

    /**
     * 开启线程，发送网络请求
     */
    private void setRequest() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("exer_id", exer_id)
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.sendEmptyMessage(SERVICE_FAILED);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mResponse = response.body().string();
                            try {
                                if (mResponse.equals("[]")) {
                                    mHandler.sendEmptyMessage(NODATA);
                                } else {
                                    parseJSON();
                                    mHandler.sendEmptyMessage(SETDATA);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
     * 解析JSON数据
     */
    private void parseJSON() throws JSONException {
        JSONArray jsonArray = new JSONArray(mResponse);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            exercise = new Exercise(jsonObject.getInt("id"), jsonObject.getString("quest"),
                    jsonObject.getString("a_choose"), jsonObject.getString("b_choose"),
                    jsonObject.getString("c_choose"), jsonObject.getString("d_choose"),
                    jsonObject.getString("answer"));
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        question = (TextView) findViewById(R.id.question);      // 问题
        Achoose = (RadioButton) findViewById(R.id.Achoose);     // 选项A
        Bchoose = (RadioButton) findViewById(R.id.Bchoose);     // 选项B
        Cchoose = (RadioButton) findViewById(R.id.Cchoose);     // 选项C
        Dchoose = (RadioButton) findViewById(R.id.Dchoose);     // 选项D
        et_fill = (EditText) findViewById(R.id.et_fill);        // 填空
        btn_q = (Button) findViewById(R.id.btn_q);              //  检查
        next = (Button) findViewById(R.id.next);                //  下一题
        currProg = (TextView) findViewById(R.id.currProg);      // 当前进度
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        et_fill.setVisibility(View.INVISIBLE);
        btn_q.setVisibility(View.GONE);
    }

    /**
     * 从Intent获取数据
     */
    private void getDataFromIntent() {
        exer_id = getIntent().getStringExtra("exer_id");
    }
}
