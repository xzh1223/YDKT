package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.activity.ClasstitleActivity;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;

/**
 * Created by xzh on 2017/2/7.
 *  课程名称适配器
 */

public class ProjAdapter extends RecyclerView.Adapter<ProjAdapter.ViewHolder> {

    private Context mContext;
    private List<General> mProjList;

    /**
     *  定义一个内部类ViewHolder继承自RecyclerView.ViewHolder
     *      构造函数传递最外层布局，然后通过findViewById()获取布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView projImage;
        TextView projName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            projImage = (ImageView) itemView.findViewById(R.id.proj_image);
            projName = (TextView) itemView.findViewById(R.id.proj_name);
        }
    }

    /**
     *  将数据源传进来，并赋值给全局变量
     * @param projList-数据源
     */
    public ProjAdapter(Context mContext,List<General> projList) {
        this.mContext = mContext;
        this.mProjList = projList;
    }

    /**
     *  创建ViewHolder实例，加载proj_item布局
     *      将加载的布局传入到构造函数中，返回ViewHolder实例
     *      添加点击功能，点击View跳转到对应的界面，并传递id
     */
    @Override
    public ProjAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.proj_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                General proj = mProjList.get(position);
                Intent intent = new Intent(mContext,ClasstitleActivity.class);
                intent.putExtra("proj_id",String.valueOf(proj.getId()));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    /**
     *  对当前项的控件进行赋值
     */
    @Override
    public void onBindViewHolder(ProjAdapter.ViewHolder holder, int position) {
        General proj = mProjList.get(position);
        holder.projName.setText(proj.getName());
        Glide.with(mContext).load(proj.getImageId()).into(holder.projImage);
    }

    /**
     * @return 数据源的长度
     */
    @Override
    public int getItemCount() {
        return mProjList.size();
    }
}
