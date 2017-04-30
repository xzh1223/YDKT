package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.edu.jssvc.xzh.rebuildclass.R;

/**
 * Created by xzh on 2017/1/16.
 * <p>
 * <p>Splash启动界面控制类
 * <p>
 * <p>通过SharePreference存储方式存储标志位，根据标志位判断是否是第一次进入系统：
 * 是：进入到GuideActivity，并设置标志位
 * 否：进入到MainActivity
 */

public class SplashActivity extends BaseActivity {

    private static final String FILE_NAME = "first";
    private SharedPreferences mPrefrence;
    private Handler mHandler;
    private static final int ENTER_HOME = 0x00;
    private static final int ENTER_SPLASH = 0x01;
    private int waitTime = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        getPreferences();
        handleMessage();
        into();
    }

    /**
     * 得到SharedPreferences
     */
    private void getPreferences() {
        mPrefrence = getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
	
    /**
     * 消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == ENTER_HOME) {
                    // 进入主页面
                    Intent mHomeIntent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(mHomeIntent);
                } else {
                    // 将App启动的值设为false
                    SharedPreferences.Editor mEditor = mPrefrence.edit();
                    mEditor.putBoolean("isFirst", false);
                    mEditor.commit();
                    // 进入引导界面
                    Intent mSplshIntent = new Intent(SplashActivity.this,
                            GuideActivity.class);
                    startActivity(mSplshIntent);
                }
                finish();
                return false;
            }
        });
    }

    /**
     * 控制跳转
     */
    private void into() {
        if (isFirstRun()) {
            mHandler.sendEmptyMessageDelayed(ENTER_SPLASH, waitTime);
        } else {
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, waitTime);
        }
    }

    /**
     * 判断程序是否是第一次运行
     *
     * @return 是否第一次运行
     */
    private boolean isFirstRun() {
        return mPrefrence.getBoolean("isFirst", true);
    }

}

