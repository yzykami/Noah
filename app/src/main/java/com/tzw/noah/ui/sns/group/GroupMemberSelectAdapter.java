package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.GroupMember;

import java.util.List;

import me.xiaopan.sketchsample.widget.SampleImageViewHead;

/**
 * Created by yzy on 2017/6/19.
 */

public class GroupMemberSelectAdapter extends BaseAdapter {

    Context context;
    List<GroupMember> items;
    List<Boolean> selected;

    public GroupMemberSelectAdapter(Context context, List<GroupMember> items, List<Boolean> selected) {
        this.context = context;
        this.items = items;
        this.selected = selected;
    }

    @Override
    public int getCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null&&items.size()>0)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_select_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.iv_head = (SampleImageViewHead) convertView.findViewById(R.id.iv_head);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.view=convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GroupMember snsPerson=items.get(position);
        String nickname = snsPerson.getMemberName();
        holder.tv.setText(nickname);
        boolean isSelected = selected.get(position);
        if (isSelected)
            holder.iv.setVisibility(View.VISIBLE);
        else
            holder.iv.setVisibility(View.GONE);
        holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_user_default);
        holder.iv_head.getOptions().setErrorImage(R.drawable.sns_user_default);
        holder.iv_head.displayImage(snsPerson.memberHeadPic);
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        return convertView;
    }

    static class ViewHolder {
        public TextView tv;
        public ImageView iv;
        public View view;
        public SampleImageViewHead iv_head;
    }

}