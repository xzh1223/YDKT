package cn.edu.jssvc.xzh.rebuildclass.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.GridViewAdapter;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ViewPager2Adapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xzh on 2017/1/16.
 *
 *  练习界面
 *
 *      1、添加viewPager
 *      2、发送网络请求，读取练习列表数据
 */
public class ExerciseFragment extends Fragment {
    private static final int UPDATE = 0x00;
    private ViewPager viewPager;
    private List<Integer> mList;
    private ViewPager2Adapter mAdapter;
    private LinearLayout linear_point;
    private GridView gridView;
    private List<General> mGridList = new ArrayList<General>();
    private GridViewAdapter mGridViewAdapter;
    private String mResponseData = "";
    private Thread mThread;
    private Handler mHandler;
    private String IP = "http://118.190.10.181:8080/YDKT/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    /**
     * 相当于Activity的onCreate()方法加载控件和操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRequest();
        handleMessage();
        initView();
//        initDate();
//        initPoint();
        setViewPager();
        mThread.start();
    }

    /**
     * 消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case UPDATE:
                        mGridViewAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送网络请求
     */
    private void setRequest() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(IP + "readExerName")
                            .build();
                    Response response = client.newCall(request).execute();
                    mResponseData = response.body().string();
                    parseJSON();
                    mHandler.sendEmptyMessage(UPDATE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析JSON格式数据
     */
    private void parseJSON() throws JSONException {
        JSONArray jsonArray = new JSONArray(mResponseData);
        General[] generals = new General[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            General general = new General(jsonObject.getInt("id"),
                    jsonObject.getString("e_name"),
                    IP + jsonObject.getString("e_image"));
            generals[i] = general;
            mGridList.add(generals[i]);
        }
    }

    /**
     * 初始化图片指示器
     */
//    private void initPoint() {
//        for (int i = 0; i < mList.size(); i++) {
//            ImageView imageView = new ImageView(getActivity());
//            if (i == 0) {
//                imageView.setImageResource(R.mipmap.dot_black);
//            } else {
//                imageView.setImageResource(R.mipmap.dot_white);
//            }
//            linear_point.addView(imageView);
//        }
//    }

    /**
     * 设置ViewPager的适配器和监听器
     * 设置GridView的适配器和监听器
     */
    private void setViewPager() {
//        mAdapter = new ViewPager2Adapter(getActivity(), mList);
//        viewPager.setAdapter(mAdapter);
//        viewPager.addOnPageChangeListener(this);
//        viewPager.setCurrentItem(0);

        mGridList.clear();
        mGridViewAdapter = new GridViewAdapter(getActivity(), mGridList);
        gridView.setAdapter(mGridViewAdapter);
    }

    /**
     * 初始化数据
     */
//    private void initDate() {
//        mList = new ArrayList<Integer>();
//        mList.add(R.mipmap.img1);
//        mList.add(R.mipmap.img2);
//        mList.add(R.mipmap.img3);
//    }

    /**
     * 初始化控件
     */
    private void initView() {
        // ViewPager
//        viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
//        linear_point = (LinearLayout) getView().findViewById(R.id.linear_point);

        // GridView
        gridView = (GridView) getView().findViewById(R.id.gridview);
    }

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        for (int i = 0; i < linear_point.getChildCount(); i++) {
//            ImageView imageView = (ImageView) linear_point.getChildAt(i);
//            if (i == position) {
//                imageView.setImageResource(R.mipmap.dot_black);
//            } else {
//                imageView.setImageResource(R.mipmap.dot_white);
//            }
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
}
