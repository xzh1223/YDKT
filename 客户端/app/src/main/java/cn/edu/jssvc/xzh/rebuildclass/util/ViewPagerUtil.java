package cn.edu.jssvc.xzh.rebuildclass.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.edu.jssvc.xzh.rebuildclass.R;

/**
 *
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 *
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 * @author xzh
 *
 *
 *
 */

@SuppressLint("HandlerLeak")
public class ViewPagerUtil extends FrameLayout {

	// 轮播图图片数量

	@SuppressWarnings("unused")
	private final static int IMAGE_COUNT = 3;

	// 自动轮播的时间间隔

	@SuppressWarnings("unused")
	private final static int TIME_INTERVAL = 5;

	// 自动轮播启用开关

	private final static boolean isAutoPlay = true;

	// 自定义轮播图的资源ID

	private int[] imagesResIds;

	// 放轮播图片的ImageView 的list

	private List<ImageView> imageViewsList;

	// 放圆点的View的list

	private List<View> dotViewsList;

	private ViewPager viewPager;

	// 当前轮播页

	private int currentItem = 0;

	// 定时任务

	private ScheduledExecutorService scheduledExecutorService;

	// Handler

	private Handler handler = new Handler() {

		@Override

		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			viewPager.setCurrentItem(currentItem);

		}

	};

	public ViewPagerUtil(Context context) {

		this(context, null);

	}

	public ViewPagerUtil(Context context, AttributeSet attrs) {

		this(context, attrs, 0);

	}

	public ViewPagerUtil(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		initData();

		initUI(context);

		if (isAutoPlay) {

			startPlay();

		}

	}

	/**
	 *
	 * 开始轮播图切换
	 *
	 */

	private void startPlay() {

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 3, TimeUnit.SECONDS);

	}

	/**
	 *
	 * 初始化相关Data
	 *
	 */

	private void initData() {

		imagesResIds = new int[] {

				R.mipmap.img1,

				R.mipmap.img2,

				R.mipmap.img3

		};

		imageViewsList = new ArrayList<ImageView>();

		dotViewsList = new ArrayList<View>();

	}

	/**
	 *
	 * 初始化Views等UI
	 *
	 */

	@SuppressWarnings("deprecation")
	private void initUI(Context context) {

		LayoutInflater.from(context).inflate(R.layout.dot, this, true);

		for (int imageID : imagesResIds) {

			ImageView view = new ImageView(context);

			view.setImageResource(imageID);

			view.setScaleType(ScaleType.FIT_XY);

			imageViewsList.add(view);

		}

		dotViewsList.add(findViewById(R.id.v_dot1));

		dotViewsList.add(findViewById(R.id.v_dot2));

		dotViewsList.add(findViewById(R.id.v_dot3));

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		viewPager.setFocusable(true);

		viewPager.setAdapter(new MyPagerAdapter());

		viewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	/**
	 *
	 * 填充ViewPager的页面适配器
	 *
	 * @author xzh
	 *
	 */

	private class MyPagerAdapter extends PagerAdapter {

		@Override

		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(imageViewsList.get(position));

		}

		@Override

		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView(imageViewsList.get(position));

			return imageViewsList.get(position);

		}

		@Override

		public int getCount() {

			return imageViewsList.size();

		}

		@Override

		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

	}

	/**
	 *
	 * ViewPager的监听器
	 *
	 * 当ViewPager中页面的状态发生改变时调用
	 *
	 * @author caizhiming
	 *
	 */

	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override

		public void onPageScrollStateChanged(int arg0) {

			switch (arg0) {

				case 1:// 手势滑动，空闲中

					isAutoPlay = false;

					break;

				case 2:// 界面切换中

					isAutoPlay = true;

					break;

				case 0:// 滑动结束，即切换完毕或者加载完毕

					// 当前为最后一张，此时从右向左滑，则切换到第一张

					if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {

						viewPager.setCurrentItem(0);

					}

					// 当前为第一张，此时从左向右滑，则切换到最后一张

					else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {

						viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);

					}

					break;

			}

		}

		@Override

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override

		public void onPageSelected(int pos) {

			currentItem = pos;

			for (int i = 0; i < dotViewsList.size(); i++) {

				if (i == pos) {

					((View) dotViewsList.get(pos)).setBackgroundResource(R.mipmap.dot_black);

				} else {

					((View) dotViewsList.get(i)).setBackgroundResource(R.mipmap.dot_white);

				}

			}

		}

	}

	/**
	 *
	 * 执行轮播图切换任务
	 *
	 * @author caizhiming
	 *
	 */

	private class SlideShowTask implements Runnable {

		@Override

		public void run() {

			synchronized (viewPager) {

				currentItem = (currentItem + 1) % imageViewsList.size();

				handler.obtainMessage().sendToTarget();

			}

		}

	}

}