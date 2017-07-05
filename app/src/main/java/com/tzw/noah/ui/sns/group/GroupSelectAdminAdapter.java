package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsPerson;

import java.util.List;

/**
 * Created by yzy on 2017/6/19.
 */

public class GroupSelectAdminAdapter extends BaseAdapter {

    Context context;
    List<SnsPerson> items;
    List<Boolean> selected;

    public GroupSelectAdminAdapter(Context context, List<SnsPerson> items, List<Boolean> selected) {
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
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.view=convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SnsPerson snsPerson=items.get(position);
        String nickname = snsPerson.name;
        holder.tv.setText(nickname);
        boolean isSelected = selected.get(position);
        if (isSelected)
            holder.iv.setVisibility(View.VISIBLE);
        else
            holder.iv.setVisibility(View.GONE);

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
    }

}