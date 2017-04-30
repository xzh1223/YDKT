package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import cn.edu.jssvc.xzh.rebuildclass.activity.MyClassActivity;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;

/**
 * Created by xzh on 2017/3/16.
 * <p>
 * 我的课程适配器
 */

public class MyClassAdapter extends RecyclerView.Adapter<MyClassAdapter.ViewHolder> {

    private Context mContext;
    private List<General> mList;
    private boolean isLongClick = false;
    private ViewHolder holder;

    public MyClassAdapter(Context mContext, List<General> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView projImage;
        TextView projName;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            projName = (TextView) itemView.findViewById(R.id.proj_name);
            projImage = (ImageView) itemView.findViewById(R.id.proj_image);
        }
    }

    @Override
    public MyClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myclass_item, parent, false);
        holder = new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(MyClassAdapter.ViewHolder holder, int position) {
        final General general = mList.get(position);
        Glide.with(mContext).load(general.getImageId()).into(holder.projImage);
        holder.projName.setText(general.getName());

        // 点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLongClick) {//发生长点击事件

                } else {//没有发生长点击事件
                    Intent intent = new Intent(mContext, ClasstitleActivity.class);
                    intent.putExtra("proj_id", String.valueOf(general.getId()));
                    mContext.startActivity(intent);
                }
            }
        });

        // 长按删除事件
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isLongClick = true;
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("即将取消收藏，是否继续？")
                        .setCancelable(false)
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isLongClick = false;
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int proj_id = general.getId();
                                MyClassActivity mActivity = (MyClassActivity) mContext;
                                mActivity.deleteMyClass(proj_id);
                                isLongClick = false;
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
