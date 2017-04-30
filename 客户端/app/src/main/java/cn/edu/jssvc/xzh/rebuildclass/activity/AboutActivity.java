package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;

import cn.edu.jssvc.xzh.rebuildclass.R;

/**
 * Created by xzh on 2017/3/18.
 *
 *  关于软件
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        initMenu();
    }
}
