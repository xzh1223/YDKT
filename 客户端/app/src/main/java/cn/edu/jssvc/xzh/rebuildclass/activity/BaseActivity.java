package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.util.GlideCacheUtil;

/**
 * 基类
 */
public class BaseActivity extends AppCompatActivity {

    public SharedPreferences mSharedPreferences;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow();
        // 设置App全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkedNetwork(this);
        clearGlide();
    }

    /**
     * 检查网络状态
     */
    private void checkedNetwork(Context context) {
        try {
            ConnectivityManager conn = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == conn || null == conn.getActiveNetworkInfo()) {
                showDialog(getResources().getString(R.string.network_inavaliable));
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 添加响应返回事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 提示对话框
     */
    public void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.ok), null)
                .show();
    }

    /**
     * 带按钮功能的提示对话框
     *
     * @param message
     */
    public void showDialog2(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip))
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * 设置标题栏和返回按钮
     */
    public void initMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                finish();
                break;
            default:
        }
        return true;
    }

    /**
     *  清楚Glide数据
     */
    public void clearGlide() {
        GlideCacheUtil.getInstance().clearImageDiskCache(this);
        GlideCacheUtil.getInstance().clearImageMemoryCache(this);
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }

    /**
     * 显示ProgressDialog进程提示框
     */
    public void showProgressDialog(String message) {
        // 提示框
        dialog = new ProgressDialog(this);
        dialog.setTitle(R.string.tip);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     *  关闭进程对话框
     */
    public void closeProgressDialog() {
        dialog.dismiss();
    }

}
