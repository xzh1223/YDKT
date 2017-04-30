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

/**
 * Created by xzh on 2017/2/19.
 *
 *  ExerciseFragment界面的ViewPager适配器
 */

public class ViewPager2Adapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mList;
    private LayoutInflater mInflater;

    /**
     *  构造函数
     * @param mContext
     * @param mList
     */
    public ViewPager2Adapter(Context mContext, List<Integer> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *  获取数据长度
     * @return
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     *  判断对象是否相同
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals((View)object);
    }

    /**
     *  销毁视图
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     *  自定义视图
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.exercise_viewpager_item,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        Glide.with(mContext).load(mList.get(position)).into(imageView);
        ((ViewPager)container).addView(view,0);
        return view;
    }
}
