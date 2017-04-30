package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.MyExpandableListViewAdapter;

/**
 * Created by xzh on 2017/3/18.
 * <p>
 * 帮助指南
 */
public class HelpActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioButton tv_class;
    private RadioButton tv_exercise;
    private RadioButton tv_forum;
    private RadioButton tv_password;
    private RadioButton tv_login;
    private RadioButton tv_update;
    private RadioGroup radioGroup;
    private String[] data2;
    private String[] data3;
    private String[] data4;
    private String[] data5;
    private String[] data6;
    private String[] data1;

    private RadioButton radioButton;

    private ExpandableListView expandableListView;

    private List<String> group_list;

    private List<String> item_list;

    private MyExpandableListViewAdapter adapter;
    private String[] data6_1;
    private String[] data5_1;
    private String[] data4_1;
    private String[] data3_1;
    private String[] data2_1;
    private String[] data1_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);
        initMenu();
        initData();
        initView();
        setListener();
        intoData(data1, data1_1);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        data1 = getResources().getStringArray(R.array.class_item);
        data2 = getResources().getStringArray(R.array.exer_item);
        data3 = getResources().getStringArray(R.array.forum_item);
        data4 = getResources().getStringArray(R.array.password_item);
        data5 = getResources().getStringArray(R.array.login_item);
        data6 = getResources().getStringArray(R.array.other_item);

        data1_1 = getResources().getStringArray(R.array.class_item_ok);
        data2_1 = getResources().getStringArray(R.array.exer_item_ok);
        data3_1 = getResources().getStringArray(R.array.forum_item_ok);
        data4_1 = getResources().getStringArray(R.array.password_item_ok);
        data5_1 = getResources().getStringArray(R.array.login_item_ok);
        data6_1 = getResources().getStringArray(R.array.other_item_ok);

        group_list = new ArrayList<String>();
        item_list = new ArrayList<String>();

    }

    private void intoData(String[] data, String[] data_1) {
        group_list.clear();
        item_list.clear();

        for (int i = 0; i < data.length; i++) {
            group_list.add(data[i]);
        }
        for (int i=0;i<data_1.length;i++){
            item_list.add(data_1[i]);
        }

        adapter = new MyExpandableListViewAdapter(HelpActivity.this, group_list, item_list);

        expandableListView.setAdapter(adapter);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        tv_class = (RadioButton) findViewById(R.id.tv_class);
        tv_exercise = (RadioButton) findViewById(R.id.tv_exercise);
        tv_forum = (RadioButton) findViewById(R.id.tv_forum);
        tv_password = (RadioButton) findViewById(R.id.tv_password);
        tv_login = (RadioButton) findViewById(R.id.tv_login);
        tv_update = (RadioButton) findViewById(R.id.tv_update);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        expandableListView = (ExpandableListView) findViewById(R.id.right_listview);
        expandableListView.setGroupIndicator(null);

        tv_class.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        if (radioButton.getText().toString().equals("课程")) {
            intoData(data1, data1_1);
        } else if (radioButton.getText().toString().equals("练习")) {
            intoData(data2, data2_1);
        } else if (radioButton.getText().toString().equals("论坛")) {
            intoData(data3, data3_1);
        } else if (radioButton.getText().toString().equals("密码")) {
            intoData(data4, data4_1);
        } else if (radioButton.getText().toString().equals("登录")) {
            intoData(data5, data5_1);
        } else if (radioButton.getText().toString().equals("其他")) {
            intoData(data6, data6_1);
        }
    }
}
