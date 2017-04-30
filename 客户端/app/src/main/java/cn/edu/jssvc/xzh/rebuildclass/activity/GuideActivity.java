package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ViewPagerAdapter;

/**
 * Created by xzh on 2017/1/16.
 * <p>
 *  引导界面
 * <p>
 *  使用ViewPager 加载三张图片以及指示器
 *  添加屏幕切换监听器和适配器：
 *      监听器：监听用户手势，指示器的切换
 *      适配器：加载视图，添加按钮以及点击跳转界面
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private List<Integer> mPictureList;
    private LinearLayout mPointSetLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);
        initData();
        initView();
        initPoint();
        setViewPager();
    }

    /**
     * 初始化数据添加图片资源
     */
    private void initData() {
        mPictureList = new ArrayList<Integer>();
        mPictureList.add(R.mipmap.img1);
        mPictureList.add(R.mipmap.img2);
        mPictureList.add(R.mipmap.img3);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPointSetLayout = (LinearLayout) findViewById(R.id.point_show_layout);
        mViewPager = (ViewPager) findViewById(R.id.picture_show_vp);
    }

    /**
     * 初始化界面下面指示器的图片
     */
    private void initPoint() {
        // 清除所有的视图
        mPointSetLayout.removeAllViews();

        for (int i = 0; i < mPictureList.size(); i++) {
            ImageView mImageView = new ImageView(this);
            if (i == 0) {
                mImageView
                        .setBackgroundResource(R.mipmap.dot_black);
            } else {
                mImageView
                        .setBackgroundResource(R.mipmap.dot_white);
            }

            mImageView
                    .setLayoutParams(new LayoutParams(20, 20, Gravity.CENTER));
            LayoutParams mParams = new LayoutParams(new ViewGroup.LayoutParams(
                    50, 50));
            mParams.leftMargin = 50;
            mParams.rightMargin = 50;
            mPointSetLayout.addView(mImageView, mParams);
        }
    }

    /**
     * 为ViewPager设置相应的参数
     */
    private void setViewPager() {
        mAdapter = new ViewPagerAdapter(this, mPictureList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 滚动状态发生变化时调用
     *
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     *
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页滚动时调用(不论是人为的触摸滚动还是程序自己执行的滚动)
     *
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     *
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新页面选中时调用
     *
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     *
     */
    @Override
    public void onPageSelected(int arg0) {
        selectPointShow(arg0);
    }

    /**
     * 选择显示的点
     *
     * @param index 传入选择点亮指示器的序号
     */
    private void selectPointShow(int index) {
        for (int i = 0; i < mPointSetLayout.getChildCount(); i++) {
            ImageView mImageView = (ImageView) mPointSetLayout.getChildAt(i);
            if (i == index) {
                mImageView
                        .setBackgroundResource(R.mipmap.dot_black);
            } else {
                mImageView
                        .setBackgroundResource(R.mipmap.dot_white);
            }
        }
    }

}
