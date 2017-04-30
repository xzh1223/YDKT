package cn.edu.jssvc.xzh.rebuildclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.activity.ClassContentActivity;
import cn.edu.jssvc.xzh.rebuildclass.activity.ClasstitleActivity;
import cn.edu.jssvc.xzh.rebuildclass.pojo.General;

/**
 * Created by xzh on 2017/2/8.
 *  课程章节适配器
 */

public class ClasstitleAdapter extends RecyclerView.Adapter<ClasstitleAdapter.ViewHolder> {

    private Context mContext;
    private List<General> mClassList;
    private String proj_id;

    /**
     *  定义一个内部类ViewHolder继承自RecyclerView.ViewHolder
     *      构造函数传递最外层布局，然后通过findViewById()获取布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView classImage;
        TextView className;
        ProgressBar progressBar;
        TextView progressText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            classImage = (ImageView) itemView.findViewById(R.id.class_image);
            className = (TextView) itemView.findViewById(R.id.class_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            progressText = (TextView) itemView.findViewById(R.id.progress_text);
        }
    }

    /**
     *  将数据源传进来，并赋值给全局变量
     * @param classList-数据源
     */
    public ClasstitleAdapter(List<General> classList) {
        mClassList = classList;
    }

    /**
     *  创建ViewHolder实例，加载class_title_item布局
     *      将加载的布局传入到构造函数中，返回ViewHolder实例
     *      添加点击功能
     */
    @Override
    public ClasstitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.class_title_item,parent,false);
        final ClasstitleAdapter.ViewHolder holder = new ClasstitleAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                General classtitle = mClassList.get(position);
                ClasstitleActivity mActivity = (ClasstitleActivity) mContext;
                proj_id = mActivity.getDataFromProj();
                Intent intent = new Intent(mContext, ClassContentActivity.class);
                intent.putExtra("current",classtitle.getProgressText());
                intent.putExtra("tit_id",String.valueOf(classtitle.getId()));
                intent.putExtra("proj_id",String.valueOf(proj_id));
                mActivity.startActivity(intent);
                mActivity.finish();

            }
        });
        return holder;
    }

    /**
     *  对当前项的控件进行赋值
     */
    @Override
    public void onBindViewHolder(ClasstitleAdapter.ViewHolder holder, int position) {
        General classtitle = mClassList.get(position);
        holder.className.setText(classtitle.getName());
        Glide.with(mContext).load(classtitle.getImageId()).into(holder.classImage);

        if (classtitle.getProgressText() == 0){
            holder.progressBar.setProgress(5);
            holder.progressText.setText("0%");
        }else {
            holder.progressBar.setProgress(classtitle.getProgressText()*100 / classtitle.getAllProgress());
            holder.progressText.setText((classtitle.getProgressText()*100 / classtitle.getAllProgress()) + "%");
        }
    }

    /**
     * @return 数据源的长度
     */
    @Override
    public int getItemCount() {
        return mClassList.size();
    }
}
