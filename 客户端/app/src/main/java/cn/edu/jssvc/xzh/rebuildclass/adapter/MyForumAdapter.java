package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.activity.ForumContentActivity;
import cn.edu.jssvc.xzh.rebuildclass.activity.MyForumActivity;
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;

/**
 * Created by Rachel on 2017/3/23.
 * 与我相关/我的帖子列表适配器
 */

public class MyForumAdapter  extends  RecyclerView.Adapter<MyForumAdapter.ViewHolder>   {
    private Context mContext;
    private List<Forum> mMyForumList;
    private boolean isLongClick = false;
 /*   private  final MyForumAdapter.ViewHolder holder;*/


    /**
     *  定义一个内部类ViewHolder继承自RecyclerView.ViewHolder
     *      构造函数传递最外层布局，然后通过findViewById()获取布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder  {
        CardView cardView;
        ImageView forum_user_img;   // 头像
        TextView forum_username;    // 用户名
        TextView forum_time;    // 时间
        TextView forum_title;   // 标题
        TextView forum_content;     // 内容
        TextView forum_times;   // 次数
        TextView forum_month;   // 月
        TextView forum_day;   //天
        LinearLayout picture;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            forum_user_img = (ImageView) itemView.findViewById(R.id.forum_user_img);
            forum_username = (TextView) itemView.findViewById(R.id.fourum_username);
            forum_time = (TextView) itemView.findViewById(R.id.forum_time);
            forum_title = (TextView) itemView.findViewById(R.id.forum_title);
            forum_content = (TextView) itemView.findViewById(R.id.forum_content);
            forum_times = (TextView) itemView.findViewById(R.id.forum_times);
            forum_day = (TextView) itemView.findViewById(R.id.forum_day);
            forum_month = (TextView) itemView.findViewById(R.id.forum_month);
            picture = (LinearLayout) itemView.findViewById(R.id.picture);

        }
    }

    /**
     *  将数据源传进来，并赋值给全局变量
     * @param myforumList-数据源
     */
    public MyForumAdapter(List<Forum> myforumList) {
        mMyForumList = myforumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.myforum_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Forum forum = mMyForumList.get(position);
       /* Glide.with(mContext).load(forum.getImageId()).into(holder.forum_user_img);
        holder.forum_username.setText(forum.getUsername());*/
        String[] strings1= forum.getTime().split("\\-");
        String  strings2=strings1[2];
        String[] strings3= strings2.split("\\ ");
        holder.forum_day.setText(strings3[0]);
        holder.forum_time.setText(strings3[1]);
        holder.forum_title.setText(forum.getTitle());
        holder.forum_content.setText(forum.getContent());
        holder.forum_times.setText(String.valueOf(forum.getTimes())+"人浏览过");
        if (forum.getF_img().equals("") || forum.getF_img() == null){

            holder.picture.setVisibility(View.GONE);
        }else {
            holder.picture.setVisibility(View.VISIBLE);
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext).load(forum.getF_img()).into(imageView);
            imageView.setMaxHeight(300);
            imageView.setMaxWidth(300);
            holder.picture.addView(imageView);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLongClick) {//发生长点击事件

                } else {//没有发生长点击事件
                    Intent intent = new Intent(mContext, ForumContentActivity.class);
                    intent.putExtra("idFromForum", String.valueOf(forum.getId()));
                    intent.putExtra("usernameFromForum", forum.getUsername());
                    intent.putExtra("imageFromForum", forum.getImageId());
                    intent.putExtra("titleFromForum", forum.getTitle());
                    intent.putExtra("contentFromForum", forum.getContent());
                    intent.putExtra("timeFromForum", forum.getTime());
                    intent.putExtra("timesFromForum", String.valueOf(forum.getTimes()));
                    if (!forum.getF_img().equals("")) {
                        intent.putExtra("fImageFromForum", forum.getF_img());
                    }else {
                        intent.putExtra("fImageFromForum","0");
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongClick = true;
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("删除，是否继续？")
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

                                Log.i("mingzi",forum.getTitle());
                                int tiezi_id = forum.getId();
                                Log.i("shanchutezuid", String.valueOf(tiezi_id));
                                MyForumActivity mActivity = (MyForumActivity) mContext;
                                mActivity.deleteMyTezi(tiezi_id);
                                isLongClick = false;
                            }
                        })
                        .show();
                return false;
            }
        });

        if (strings1[1].equals("01")){
            holder.forum_month.setText("一月");}
        else if(strings1[1].equals("02")){
            holder.forum_month.setText("二月");}
        else if(strings1[1].equals("03")){
            holder.forum_month.setText("三月");}
        else if(strings1[1].equals("04")){
            holder.forum_month.setText("四月");}
        else if(strings1[1].equals("05")){
            holder.forum_month.setText("五月");}
        else if(strings1[1].equals("06")){
            holder.forum_month.setText("六月");}
        else if(strings1[1].equals("07")){
            holder.forum_month.setText("七月");}
        else if(strings1[1].equals("08")){
            holder.forum_month.setText("八月");}
        else if(strings1[1].equals("09")){
            holder.forum_month.setText("九月");}
        else if(strings1[1].equals("10")){
            holder.forum_month.setText("十月");}
        else if(strings1[1].equals("11")){
            holder.forum_month.setText("十一月");}
        else if(strings1[1].equals("12")){
            holder.forum_month.setText("十二月");}



   /*   *//*  forum.getTime(): 2017-03-24 16:02:00 *//*
        Log.i("-年2017--",strings1[0]);//年
        Log.i("=月03==",strings1[1]);//月
        Log.i("=24 16:02:00=",strings1[2]);
        String  strings2=strings1[2];
        String[] strings3= strings2.split("\\ ");
        Log.i("--日24 -",strings3[0]);//日
        Log.i("=时间16:02:00==",strings3[1]);//时间
   *//*     Date date = new Date();
            System.out.println(date.getTime());*//*
   *//* }*/
    }
    /**
     * 对话框
     */
    private void showdialog() {

    }
    @Override
    public int getItemCount() {
        return mMyForumList.size();
    }


}

