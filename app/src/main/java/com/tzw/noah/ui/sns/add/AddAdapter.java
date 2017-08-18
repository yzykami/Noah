package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
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

public class AddAdapter extends BaseAdapter {

    Context context;
    List<User> items;
    MyBaseActivity myBaseActivity;
//    List<Boolean> selected;

    OnAddClickListener mOnAddClickListener;

    public interface OnAddClickListener {
        public void onAddClick(View v, int position);
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        mOnAddClickListener = listener;
    }

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

        final User user = items.get(position);

//        if (user.type == User.Type.Person) {
//            holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_group_default);
//            holder.iv_head.getOptions().setErrorImage(R.drawable.sns_group_default);
//        } else {
        holder.iv_head.getOptions().setLoadingImage(R.drawable.sns_user_default);
        holder.iv_head.getOptions().setErrorImage(R.drawable.sns_user_default);
        holder.iv_head.displayImage(user.memberHeadPic);
//        }

        holder.tv.setText(user.getName());
        holder.tv_sign.setText(user.memberIntroduce);


//        if (position == 0) {
//            holder.tag.setText(user.nameFirstChar);
//            holder.tag.setVisibility(View.VISIBLE);
//        } else {
//            holder.tag.setVisibility(View.GONE);
//        }
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

        if (user.isAttention) {
            holder.tv_add.setText("已关注");
            holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.bg_light));
            holder.tv_add.setBackgroundResource(R.drawable.bg_gray_border);
        } else {
            holder.tv_add.setText("关注");
            holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.myRed));
            holder.tv_add.setBackgroundResource(R.drawable.bg_red_border_round);
        }

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mOnAddClickListener != null)
//                    mOnAddClickListener.onAddClick(v, position);

                if (user.isAttention) {
                    return;
                }
                if (user.type == User.Type.Person) {
                    new SnsManager(context).snsAttention(user, new StringDialogCallback(myBaseActivity) {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            myBaseActivity.toast(myBaseActivity.getResources().getString(R.string.internet_fault));
                        }

                        @Override
                        public void onResponse(IMsg iMsg) {
                            if (iMsg.isSucceed()) {
                                myBaseActivity.toast("关注成功");
                                removeItem(position);
                            } else {
                                myBaseActivity.toast(iMsg.getMsg());
                            }
                        }
                    });
                } else {
                    myBaseActivity.startActivity(GroupApplyActivity.class);
                }
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