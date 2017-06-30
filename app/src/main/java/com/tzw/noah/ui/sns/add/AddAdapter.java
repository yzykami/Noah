package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsPerson;

import java.util.List;

/**
 * Created by yzy on 2017/6/29.
 */

public class AddAdapter extends BaseAdapter {

    Context context;
    List<SnsPerson> items;
//    List<Boolean> selected;

    public AddAdapter(Context context, List<SnsPerson> items) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_add_item, null);
            holder = new ViewHolder();
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SnsPerson snsPerson = items.get(position);
        holder.tv.setText(snsPerson.name);


        if (position ==0) {
            holder.tag.setText(snsPerson.shortCut);
            holder.tag.setVisibility(View.VISIBLE);
        } else {
            holder.tag.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView tv;
        public View view;
        public TextView tag;
    }
}