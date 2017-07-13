package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/29.
 */

public class FriendAdapter extends BaseAdapter {

    Context context;
    List<User> items;
//    List<Boolean> selected;

    public FriendAdapter(Context context, List<User> items) {
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
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = items.get(position);
        holder.tv.setText(user.getName());
        holder.tv_sign.setText(user.memberIntroduce);

        boolean isSame = false;
        String tag = "#";

        String ping = user.nameFirstChar;
        String ping2 = "#";

        if (position != 0) {
            ping2 = items.get(position - 1).nameFirstChar;
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
    }
}