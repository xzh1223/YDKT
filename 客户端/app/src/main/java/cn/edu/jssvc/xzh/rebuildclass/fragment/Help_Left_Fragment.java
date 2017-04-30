package cn.edu.jssvc.xzh.rebuildclass.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.jssvc.xzh.rebuildclass.R;

/**
 * Created by xzh on 2017/3/18.
 *
 *      帮助指南侧边按钮
 */

public class Help_Left_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_left_fragment,container,false);
        return view;
    }
}
