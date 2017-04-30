package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.jssvc.xzh.rebuildclass.R;

/**
 * Created by xzh on 2017/3/17.
 *
 *  系统设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_update;
    private TextView tv_guide;
    private TextView tv_feedback;
    private TextView tv_about;
    private Button btn_esc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        initMenu();
        initView();
        setListener();
    }

    /**
     *  设置监听事件
     */
    private void setListener() {
        tv_update.setOnClickListener(this);
        tv_guide.setOnClickListener(this);
        tv_feedback.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        btn_esc.setOnClickListener(this);
    }

    /**
     *  初始化控件
     */
    private void initView() {
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_guide = (TextView) findViewById(R.id.tv_guide);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        tv_about = (TextView) findViewById(R.id.tv_about);
        btn_esc = (Button) findViewById(R.id.btn_esc);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                showDialog("已是最新版本");
                break;
            case R.id.tv_guide:
                Intent helpIntent = new Intent(SettingActivity.this,HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.tv_feedback:
                Intent feedbackIntent = new Intent(SettingActivity.this,FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.tv_about:
                Intent aboutIntent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.btn_esc:
                showDialogWithESC(getResources().getString(R.string.is_esc));
                break;
        }
    }

    public void showDialogWithESC(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setMessage(message)
                .setNegativeButton(getResources().getString(R.string.no), null)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}
