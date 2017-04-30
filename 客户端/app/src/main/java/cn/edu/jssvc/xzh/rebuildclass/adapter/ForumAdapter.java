package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;


/**
 * Created by xzh on 2017/2/10.
 *
 *  论坛列表适配器
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder>  {

    private Context mContext;
    private List<Forum> mForumList;

    /**
     *  定义一个内部类ViewHolder继承自RecyclerView.ViewHolder
     *      构造函数传递最外层布局，然后通过findViewById()获取布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView forum_user_img;   // 头像
        TextView forum_username;    // 用户名
        TextView forum_time;    // 时间
        TextView forum_title;   // 标题
        TextView forum_content;     // 内容
        TextView forum_times;   // 次数
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
            picture = (LinearLayout) itemView.findViewById(R.id.picture);
        }
    }

    /**
     *  将数据源传进来，并赋值给全局变量
     * @param forumList-数据源
     */
    public ForumAdapter(List<Forum> forumList) {
        mForumList = forumList;
    }

    @Override
    public ForumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.forum_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Forum forum = mForumList.get(position);
                Intent intent = new Intent(mContext, ForumContentActivity.class);
                intent.putExtra("idFromForum",String.valueOf(forum.getId()));
                intent.putExtra("usernameFromForum",forum.getUsername());
                intent.putExtra("imageFromForum",forum.getImageId());
                intent.putExtra("titleFromForum",forum.getTitle());
                intent.putExtra("contentFromForum",forum.getContent());
                intent.putExtra("timeFromForum",forum.getTime());
                intent.putExtra("timesFromForum",String.valueOf(forum.getTimes()));
                if (!forum.getF_img().equals("")) {
                    intent.putExtra("fImageFromForum", forum.getF_img());
                }else {
                    intent.putExtra("fImageFromForum","0");
                }
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ForumAdapter.ViewHolder holder, int position) {
        Forum forum = mForumList.get(position);
        Glide.with(mContext).load(forum.getImageId()).into(holder.forum_user_img);
        holder.forum_username.setText(forum.getUsername());
        holder.forum_time.setText(forum.getTime());
        holder.forum_title.setText(forum.getTitle());
        holder.forum_content.setText(forum.getContent());
        holder.forum_times.setText(String.valueOf(forum.getTimes())+"人评论过");
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
    }
    public void addNewData(Forum DiscussList){
        mForumList.add(DiscussList);
    }
    @Override
    public int getItemCount() {
        return mForumList.size();
    }
}
