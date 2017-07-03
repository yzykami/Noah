package com.tzw.noah.ui.sns.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsMessage;

import java.util.Comparator;
import java.util.List;

import me.xiaopan.sketch.shaper.CircleImageShaper;
import me.xiaopan.sketchsample.widget.SampleImageView;

/**
 * Created by yzy on 2017/7/1.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int viewTypeMyself = 1;
    int viewTypeOther = 2;

    List<SnsMessage> items;
    Context mContext;

    public ChatRecyclerAdapter(Context context, List<SnsMessage> items) {
        mContext = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == viewTypeMyself) {
            return new MeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.sns_dialog_me_item, null));
        } else if (viewType == viewTypeOther) {
            return new MeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.sns_dialog_other_item, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SnsMessage snsMessage=items.get(position);
        boolean isShowTime = false;
        String tag = "#";

        String time1 = snsMessage.sendTime;
        String time2 = "";

        if (position != 0) {
            time2 = items.get(position - 1).sendTime;
            isShowTime = time1.equals(time2);
        }
        
        if (holder instanceof MeViewHolder) {
            holder=(MeViewHolder)holder;
            ((MeViewHolder) holder).tv_name.setText(snsMessage.name);
            ((MeViewHolder) holder).msg_content.setText(snsMessage.content);
            ((MeViewHolder) holder).iv_head.getOptions().setImageShaper(new CircleImageShaper());
            ((MeViewHolder)holder).iv_head.setPage(SampleImageView.Page.PHOTO_LIST);
            ((MeViewHolder) holder).iv_head.displayImage(snsMessage.imageUrl);
            if(isShowTime)
                ((MeViewHolder) holder).tv_time.setVisibility(View.GONE);
        } else if (holder instanceof OtherViewHolder) {
            ((OtherViewHolder) holder).tv_name.setText(snsMessage.name);
            ((OtherViewHolder) holder).msg_content.setText(snsMessage.content);
            ((OtherViewHolder) holder).iv_head.getOptions().setImageShaper(new CircleImageShaper());
            ((OtherViewHolder)holder).iv_head.setPage(SampleImageView.Page.PHOTO_LIST);
            ((OtherViewHolder) holder).iv_head.displayImage(snsMessage.imageUrl);
            if(isShowTime)
                ((OtherViewHolder) holder).tv_time.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (items != null && items.size() > 0) {
            return items.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (items != null && items.size() > 0) {
            if (items.get(position).isMyself)
                return viewTypeMyself;
            else
                return viewTypeOther;
        }
        return 0;
    }

    interface Holder
    {
        public void setTextView( String content);
        public void setImageView( String url);
        public void setTime(boolean isShow,String content);
    }

    public class MeViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_time;
        private final TextView tv_name;
        private final TextView msg_content;
        private final SampleImageView iv_head;

        public MeViewHolder(View itemView) {
            super(itemView);

            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            msg_content = (TextView) itemView.findViewById(R.id.msg_content);
            iv_head = (SampleImageView) itemView.findViewById(R.id.iv_head);
        }
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_time;
        private final TextView tv_name;
        private final TextView msg_content;
        private final SampleImageView iv_head;

        public OtherViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            msg_content = (TextView) itemView.findViewById(R.id.msg_content);
            iv_head = (SampleImageView) itemView.findViewById(R.id.iv_head);
        }
    }
}
