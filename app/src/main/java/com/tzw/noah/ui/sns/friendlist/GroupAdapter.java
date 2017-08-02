package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.User;

import java.util.List;

import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

/**
 * Created by yzy on 2017/6/29.
 */

public class GroupAdapter extends BaseAdapter {

    Context context;
    List<Group> items;
//    List<Boolean> selected;

    public GroupAdapter(Context context, List<Group> items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_friend_item, parent, false);
            holder = new ViewHolder();
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.iv_head = (SampleImageViewHead) convertView.findViewById(R.id.iv_head);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Group group = items.get(position);
        if (group.groupName.isEmpty())
            holder.tv.setText(group.initialGroupName);
        else
            holder.tv.setText(group.groupName);
        holder.tv_sign.setText(group.groupIntroduction);

        if(group.groupAttribute==Group.Type.GROUP)
        {
            holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_group_default);
            holder.iv_head.getOptions().setErrorImage(R.drawable.sns_group_default);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_head.getLayoutParams();
            layoutParams.width = (int) (context.getResources().getDimension(R.dimen.groupHead) + 0.5f);
            layoutParams.height = (int) (context.getResources().getDimension(R.dimen.groupHead) + 0.5f);
            holder.iv_head.setLayoutParams(layoutParams);
        }
        else
        {
            holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_discuss_default);
            holder.iv_head.getOptions().setErrorImage(R.drawable.sns_discuss_default);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_head.getLayoutParams();
            layoutParams.width = (int) (context.getResources().getDimension(R.dimen.head) + 0.5f);
            layoutParams.height = (int) (context.getResources().getDimension(R.dimen.head) + 0.5f);
            holder.iv_head.setLayoutParams(layoutParams);
        }

        holder.iv_head.displayImage(group.groupHeader);

        boolean isSame = false;
        String tag = "";

        String ping = group.tag;
        String ping2 = "";

        if (position != 0) {
            ping2 = items.get(position - 1).tag;
            isSame = ping.equals(ping2);
        } else {

        }
        if (isSame) {
            holder.tag.setVisibility(View.GONE);
        } else {
            tag = ping;
            holder.tag.setText(tag);
            holder.tag.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView tv;
        public View view;
        public TextView tag;
        public TextView tv_sign;
        public SampleImageViewHead iv_head;
    }
}