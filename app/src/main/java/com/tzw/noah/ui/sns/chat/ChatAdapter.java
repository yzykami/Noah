package com.tzw.noah.ui.sns.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsMessage;

import java.util.List;

import me.xiaopan.sketch.process.CircleImageProcessor;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import me.xiaopan.sketch.shaper.ImageShaper;
import me.xiaopan.sketchsample.widget.SampleImageView;

/**
 * Created by yzy on 2017/6/29.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    List<SnsMessage> items;
//    List<Boolean> selected;

    public ChatAdapter(Context context, List<SnsMessage> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null && items.size() > 0)
            return items.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final SnsMessage snsMessage = items.get(position);

        if (convertView == null) {
            if (snsMessage.isMyself) {
                convertView = LayoutInflater.from(context).inflate(R.layout.sns_dialog_me_item, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.sns_dialog_other_item, null);
            }
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.msg_content = (TextView) convertView.findViewById(R.id.msg_content);
            holder.iv_head = (SampleImageView) convertView.findViewById(R.id.iv_head);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_name.setText(snsMessage.name);
        holder.msg_content.setText(snsMessage.content);

//        holder.iv_head.getOptions().setImageProcessor(CircleImageProcessor.getInstance());
        holder.iv_head.getOptions().setImageShaper(new CircleImageShaper());
        holder.iv_head.setPage(SampleImageView.Page.PHOTO_LIST);
        holder.iv_head.displayImage(snsMessage.imageUrl);


        boolean isSame = false;
        String tag = "#";

        String ping = snsMessage.sendTime;
        String ping2 = "#";

        if (position != 0) {
            ping2 = items.get(position - 1).sendTime;
            isSame = ping.equals(ping2);
        } else {

        }
        if (isSame) {
            holder.tv_time.setVisibility(View.GONE);
        } else {
            tag = ping;
            holder.tv_time.setText(tag);
            holder.tv_time.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView tv_name;
        public View view;
        public TextView tv_time;
        public SampleImageView iv_head;
        public TextView msg_content;
    }
}