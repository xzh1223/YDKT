package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
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
import cn.edu.jssvc.xzh.rebuildclass.pojo.Forum;

/**
 * Created by xzh on 2017/2/12.
 *
 *  帖子内容以及评论列表适配器
 */

public class ForumContentAdapter extends RecyclerView.Adapter<ForumContentAdapter.ViewHolder> {

    private Context mContext;
    private List<Forum> mDiscussList;

    /**
     *  定义一个内部类ViewHolder继承自RecyclerView.ViewHolder
     *      构造函数传递最外层布局，然后通过findViewById()获取布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView discuss_user_img;   // 头像
        TextView discuss_username;    // 用户名
        TextView discuss_time;    // 时间
        TextView discuss_content;     // 内容

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            discuss_user_img = (ImageView) itemView.findViewById(R.id.discuss_user_img);
            discuss_username = (TextView) itemView.findViewById(R.id.discuss_username);
            discuss_time = (TextView) itemView.findViewById(R.id.discuss_time);
            discuss_content = (TextView) itemView.findViewById(R.id.discuss_content);
        }
    }

    /**
     *  将数据源传进来，并赋值给全局变量
     * @param discussList-数据源
     */
    public ForumContentAdapter(List<Forum> discussList) {
        mDiscussList = discussList;
    }

    @Override
    public ForumContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.forum_content_item,parent,false);
        final ForumContentAdapter.ViewHolder holder = new ForumContentAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForumContentAdapter.ViewHolder holder, int position) {
        Forum forum = mDiscussList.get(position);
        Glide.with(mContext).load(forum.getImageId()).into(holder.discuss_user_img);
        holder.discuss_username.setText(forum.getUsername());
        holder.discuss_time.setText(forum.getTime());
        holder.discuss_content.setText(forum.getContent());
    }
    public void addNewData(Forum DiscussList){
        mDiscussList.add(DiscussList);
    }
    @Override
    public int getItemCount() {
        return mDiscussList.size();
    }
}
