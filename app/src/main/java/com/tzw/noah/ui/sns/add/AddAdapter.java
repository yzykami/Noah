package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;
import com.tzw.noah.ui.MyBaseActivity;

import java.util.List;

/**
 * Created by yzy on 2017/6/29.
 */

public class AddAdapter extends BaseAdapter {

    Context context;
    List<User> items;
    MyBaseActivity myBaseActivity;
//    List<Boolean> selected;

    public AddAdapter(Context context, List<User> items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_add_item, null);
            holder = new ViewHolder();
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User snsPerson = items.get(position);
        holder.tv.setText(snsPerson.memberNickName);


        if (position == 0) {
            holder.tag.setText(snsPerson.nameFirstChar);
            holder.tag.setVisibility(View.VISIBLE);
        } else {
            holder.tag.setVisibility(View.GONE);
        }
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snsPerson.type == SnsPerson.Type.Group) {
                    myBaseActivity.startActivity(GroupApplyActivity.class);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView tv;
        public TextView tv_add;
        public View view;
        public TextView tag;
    }
}