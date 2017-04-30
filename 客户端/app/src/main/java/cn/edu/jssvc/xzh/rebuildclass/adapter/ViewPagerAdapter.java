package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.activity.GuideActivity;
import cn.edu.jssvc.xzh.rebuildclass.activity.LoginActivity;

/**
 * Created by xzh on 2017/1/16.
 *
 * Guide引导界面的ViewPager适配器
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mList;
    private LayoutInflater mInflater;

    /**
     * 构造器
     *
     * @param mContext 上下文
     * @param mList    数据源
     */
    public ViewPagerAdapter(Context mContext, List<Integer> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 数据源长度
     *
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 判断两个对象是否相同
     *
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(
     *android.view.View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0.equals((View) arg1);
    }

    /**
     * 销毁视图
     *
     * @see android.support.v4.view.PagerAdapter#destroyItem(
     *android.view.ViewGroup, int, java.lang.Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     * 获取Item的位置
     *
     * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 自定义视图
     *
     * @see android.support.v4.view.PagerAdapter#instantiateItem(
     *android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = (View) mInflater.inflate(R.layout.guide_item, container,
                false);
        RelativeLayout mLayout = (RelativeLayout) view
                .findViewById(R.id.picture);
        Button btn_enter = (Button) view.findViewById(R.id.btn_enter);
        mLayout.setBackgroundResource(mList.get(position));

        // 设置跳转按钮
        if (position == 2) {
            btn_enter.setVisibility(View.VISIBLE);
        } else {
            btn_enter.setVisibility(View.GONE);
        }
        // 跳转到主界面
        btn_enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GuideActivity mActivity = (GuideActivity) mContext;
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                mActivity.finish();
            }
        });
        ((ViewPager) container).addView(view, 0);
        return view;
    }

}