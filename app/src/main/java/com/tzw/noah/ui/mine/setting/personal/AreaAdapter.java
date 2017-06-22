package com.tzw.noah.ui.mine.setting.personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Area;

import java.util.List;

/**
 * Created by yzy on 2017/6/19.
 */

public class AreaAdapter extends BaseAdapter {

    Context context;
    List<Area> items;
    List<Boolean> selected;

    public final static int mode_province = 1;
    public final static int mode_city = 2;
    public final static int mode_twon = 3;

    int MODE;

    public AreaAdapter(Context context, List<Area> items, List<Boolean> selected, int MODE) {
        this.context = context;
        this.items = items;
        this.selected = selected;
        this.MODE = MODE;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.mine_settting_personal_areaprovince_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tv_selected = (TextView) convertView.findViewById(R.id.tv_selected);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Area area = items.get(position);
        holder.tv.setText(area.areaName);
        boolean isSelected = selected.get(position);
        if (MODE == mode_province) {
            if (isSelected)
                holder.tv_selected.setVisibility(View.VISIBLE);
            else
                holder.tv_selected.setVisibility(View.GONE);
        } else {
            holder.tv_selected.setVisibility(View.GONE);
        }
        if (MODE == mode_twon) {
            holder.iv.setVisibility(View.INVISIBLE);
        } else {
            holder.iv.setVisibility(View.VISIBLE);
        }
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
        public TextView tv_selected;
    }

}