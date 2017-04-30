package cn.edu.jssvc.xzh.rebuildclass.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.adapter.ProjAdapter;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xzh on 2017/1/16.
 *  课程界面
 *
 *      发送网络请求，读取课程名称列表
 */
public class ProjectFragment extends Fragment {

    private static final int UPDATE = 0x00;
    // 存放从服务器获取的数据数据
    private General[] projs = {};
    private List<General> projList = new ArrayList<>();
    private ProjAdapter adapter;
    // 科目列表的服务器请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/readProj";
    // 科目对应图片所在位置的网络请求地址
    private String IMG_URL = "http://118.190.10.181:8080/YDKT/";
    private Handler mHandler;

    /**
     * 加载对应的布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);

    }

    /**
     * 相当于Activity的onCreate()方法加载控件和操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        handleMessage();
        sendRequestWithOkHttp();
    }

    /**
     *  消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case UPDATE:
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送网络请求
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(URL)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSON(responseData);
                    mHandler.sendEmptyMessage(UPDATE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析从服务器获取的JSON格式数据并存放到Fruit数组中
     */
    private void parseJSON(String jsonData) {
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            projs = new General[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String pro_name = jsonObject.getString("pro_name");
                String pro_image = jsonObject.getString("pro_image");

                General f = new General(id,pro_name,IMG_URL+pro_image);
                projs[i] = f;
                projList.add(projs[i]);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        projList.clear();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        // 设置两列布局
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProjAdapter(getActivity(),projList);
        recyclerView.setAdapter(adapter);
    }

}
