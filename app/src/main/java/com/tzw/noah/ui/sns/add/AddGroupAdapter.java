package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;
import java.util.List;

import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/29.
 */

public class AddGroupAdapter extends BaseAdapter {

    Context context;
    List<Group> items;
    MyBaseActivity myBaseActivity;
//    List<Boolean> selected;

    OnAddClickListener mOnAddClickListener;

    public interface OnAddClickListener {
        public void onAddClick(View v, int position);
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        mOnAddClickListener = listener;
    }

    public AddGroupAdapter(Context context, List<Group> items) {
        this.context = context;
        myBaseActivity = (MyBaseActivity) context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_add_item, null);
            holder = new ViewHolder();
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.iv_head = (SampleImageViewHead) convertView.findViewById(R.id.iv_head);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Group group = items.get(position);

        holder.tv_add.setText("申请");
        holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_group_default);
        holder.iv_head.getOptions().setErrorImage(R.drawable.sns_group_default);
        holder.iv_head.displayImage(group.groupHeader);

        holder.tv.setText(group.groupName);
        holder.tv_sign.setText(group.groupIntroduction);

        boolean isSame = true;

        if (isSame) {
            holder.tag.setVisibility(View.GONE);
        } else {
            holder.tag.setText("");
            holder.tag.setVisibility(View.VISIBLE);
        }

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bu = new Bundle();
                bu.putSerializable("DATA", group);
                myBaseActivity.startActivity(GroupApplyActivity.class, bu);
            }
        });

        return convertView;
    }

    private void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tv;
        public TextView tv_add;
        public View view;
        public TextView tag;
        public TextView tv_sign;
        public SampleImageViewHead iv_head;
    }
}