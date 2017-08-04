package com.tzw.noah.ui.sns.relationrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Notification;
import com.tzw.noah.models.RelationRecord;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/4.
 */

public class RelationRecordAdapter extends BaseAdapter {

    Context context;
    List<RelationRecord> items;
    MyBaseActivity myBaseActivity;
//    List<Boolean> selected;

    int viewer;

    OnAddClickListener mOnAddClickListener;

    public interface OnAddClickListener {
        public void onAddClick(View v, int position);
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        mOnAddClickListener = listener;
    }

    public RelationRecordAdapter(Context context, List<RelationRecord> items) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sns_notification_item, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
            holder.tv_add.setVisibility(View.GONE);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.iv_head = (SampleImageViewHead) convertView.findViewById(R.id.iv_head);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RelationRecord record = items.get(position);

        holder.tv.setText(record.memberNickName);
        holder.iv_head.displayImage(record.memberHeadPic);

        // 0: 关注 , 1: 取消关注 , 2: 移除粉丝 , 3: 加入黑名单 , 4: 移除黑名单 , 5: 加为好友 , 6: 移除好友
        if (record.setAction == 0) {
            holder.tv1.setText("关注了我");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 1) {
            holder.tv1.setText("取消了关注我");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 2) {
            holder.tv1.setText("移除了我的粉丝");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 3) {
            holder.tv1.setText("把我加入了黑名单");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 4) {
            holder.tv1.setText("把我移除了黑名单");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 5) {
            holder.tv1.setText("成为了我的好友");
            holder.tv2.setVisibility(View.GONE);
        } else if (record.setAction == 6) {
            holder.tv1.setText("解除了和我的好友关系");
            holder.tv2.setVisibility(View.GONE);
        }

        holder.tag.setVisibility(View.GONE);
        return convertView;
    }

    private void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, RelationRecord item) {
        items.set(position, item);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tv;
        public TextView tv_add;
        public View view;
        public TextView tv1;
        public TextView tv2;
        public SampleImageViewHead iv_head;
        public TextView tag;
    }
}