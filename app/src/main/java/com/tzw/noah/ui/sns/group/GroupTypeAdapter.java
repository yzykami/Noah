package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.GroupType;
import com.tzw.noah.models.User;

import java.util.List;

/**
 * Created by yzy on 2017/7/14.
 */

public class GroupTypeAdapter extends BaseAdapter {

    Context context;
    List<GroupType> items;

    public GroupTypeAdapter(Context context, List<GroupType> items) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_grouptype_select_item, parent, false);
            holder = new ViewHolder();
//            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
//            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GroupType groupType = items.get(position);
        holder.tv.setText(groupType.groupTypeName);
//        holder.tv_sign.setText(groupType.memberIntroduce);

        return convertView;
    }

    static class ViewHolder {
        public TextView tv;
        public View view;
        public TextView tag;
        public TextView tv_sign;
    }
}