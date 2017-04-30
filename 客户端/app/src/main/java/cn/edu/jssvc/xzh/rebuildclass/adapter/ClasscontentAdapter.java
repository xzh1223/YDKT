package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;

/**
 * Created by xzh on 2017/2/9.
 *  课程内容的Viewpager适配器
 */

public class ClasscontentAdapter extends PagerAdapter {

    private Context mContext;
    private List<General> mList;
    private LayoutInflater mInflater;

    /**
     * 构造器
     *
     * @param mContext 上下文
     * @param mList    数据源
     */
    public ClasscontentAdapter(Context mContext, List<General> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals((View) object);
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
     * 自定义视图
     *
     * @see android.support.v4.view.PagerAdapter#instantiateItem(
     *android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = (View) mInflater.inflate(R.layout.class_content_item, container,
                false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_view);
        General g = mList.get(position);
        Glide.with(mContext).load(g.getImageId()).into(imageView);
        ((ViewPager) container).addView(view, 0);
        return view;
    }
}
