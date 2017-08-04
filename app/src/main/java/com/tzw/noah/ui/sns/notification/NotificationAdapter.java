package com.tzw.noah.ui.sns.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Notification;
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
 * Created by yzy on 2017/6/29.
 */

public class NotificationAdapter extends BaseAdapter {

    Context context;
    List<Notification> items;
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

    public NotificationAdapter(Context context, List<Notification> items) {
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
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tag = (TextView) convertView.findViewById(R.id.tag);
            holder.iv_head = (SampleImageViewHead) convertView.findViewById(R.id.iv_head);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Notification notification = items.get(position);

        holder.tv.setText(notification.sourceNickName);

        holder.tv2.setText(notification.handleInfo);
        if (notification.notificationType == 0) {
            if (notification.handleType == 0) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.sourceNickName);
                holder.tv1.setText("邀请您加入: " + notification.groupName);
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setText("同意");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.myRed));
                holder.tv_add.setBackgroundDrawable(myBaseActivity.getResources().getDrawable(R.drawable.bg_red_border_round));
                holder.tv_add.setVisibility(View.VISIBLE);
            } else if (notification.handleType == 1) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.sourceNickName);
                holder.tv1.setText("邀请您加入: " + notification.groupName);
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setText("已拒绝");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 2) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.sourceNickName);
                holder.tv1.setText("邀请您加入: " + notification.groupName);
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setText("已同意");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 3) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("申请加入: " + notification.groupName);
                holder.tv2.setText(notification.requestInfo);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setText("同意");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.myRed));
                holder.tv_add.setBackgroundDrawable(myBaseActivity.getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 4) {
                holder.iv_head.displayImage(notification.handleMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已拒绝您加入群");
                holder.tv2.setText("处理者: " + notification.handleNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setText("已拒绝");
                holder.tv_add.setVisibility(View.GONE);
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 5) {
                holder.iv_head.displayImage(notification.handleMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已同意你的申请");
                holder.tv2.setText("处理者: " + notification.handleNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 6) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你移出群");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 7) {
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("你已成功退群");
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 8) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你设为管理员");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 9) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已取消你的管理员");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 10) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你设为群主");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            }
        } else {
            if (notification.handleType == 0) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.sourceNickName);
                holder.tv1.setText("邀请您加入: " + notification.groupName);
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setText("同意");
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.myRed));
                holder.tv_add.setBackgroundDrawable(myBaseActivity.getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 1) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("已拒绝加入: " + notification.groupName);
                holder.tv2.setText("邀请人: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
                holder.tv_add.setText("已拒绝");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 2) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("已同意加入: " + notification.groupName);
                holder.tv2.setText("邀请人: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
                holder.tv_add.setText("已同意");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 3) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("申请加入: " + notification.groupName);
                holder.tv2.setText(notification.requestInfo);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setText("同意");
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.myRed));
                holder.tv_add.setBackgroundDrawable(myBaseActivity.getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 4) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("申请加入:" + notification.groupName + "");
                holder.tv2.setText("处理者: " + notification.handleNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setText("已拒绝");
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 5) {
                holder.iv_head.displayImage(notification.memberHeadPic);
                holder.tv.setText(notification.memberNickName);
                holder.tv1.setText("申请加入:" + notification.groupName + "");
                holder.tv2.setText("处理者: " + notification.handleNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setText("已同意");
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_add.setTextColor(myBaseActivity.getResources().getColor(R.color.textLightGray));
                holder.tv_add.setBackgroundColor(myBaseActivity.getResources().getColor(R.color.white));
            } else if (notification.handleType == 6) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你移出群");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 7) {
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("你已成功退群");
                holder.tv2.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 8) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你设为管理员");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 9) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已取消你的管理员");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else if (notification.handleType == 10) {
                holder.iv_head.displayImage(notification.sourceMemberHeadPic);
                holder.tv.setText(notification.groupName);
                holder.tv1.setText("已将你设为群主");
                holder.tv2.setText("处理者: " + notification.sourceNickName);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            }
        }

        holder.tag.setVisibility(View.GONE);
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.notificationType == 0) {
                    if (notification.handleType == 0) {
                        List<Param> body = new ArrayList<Param>();
                        body.add(new Param("groupId", notification.groupId));
                        body.add(new Param("memberNo", notification.sourceNo));
                        body.add(new Param("status", 1));
//                        body.add(new Param("rejectReason",));
                        new SnsManager(context).snsHandleInviteWithGroup(body, new StringDialogCallback(myBaseActivity) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                myBaseActivity.toast(myBaseActivity.getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    myBaseActivity.toast("已同意邀请");
                                    notification.handleType = 2;
                                    updateItem(position,notification);
                                } else {
                                    myBaseActivity.toast(iMsg.getMsg());
                                }
                            }
                        });
                    } else if (notification.handleType == 3) {

                    }
                }
                if (notification.notificationType == 1) {
                    if (notification.handleType == 0) {

                    } else if (notification.handleType == 3) {
                        List<Param> body = new ArrayList<Param>();
                        body.add(new Param("groupId", notification.groupId));
                        body.add(new Param("memberNo", notification.memberNo));
                        body.add(new Param("status", 1));
//                        body.add(new Param("rejectReason",));
                        new SnsManager(context).snsHandleApplyToGroup(body, new StringDialogCallback(myBaseActivity) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                myBaseActivity.toast(myBaseActivity.getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    myBaseActivity.toast("已同意申请");
                                    notification.handleType = 5;
                                    updateItem(position,notification);
                                } else {
                                    myBaseActivity.toast(iMsg.getMsg());
                                }
                            }
                        });
                    }
                }
//                if (notification.notificationType == User.Type.Person) {
//                    new SnsManager(context).snsAttention(notification.memberNo, new StringDialogCallback(myBaseActivity) {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            myBaseActivity.toast(myBaseActivity.getResources().getString(R.string.internet_fault));
//                        }
//
//                        @Override
//                        public void onResponse(IMsg iMsg) {
//                            if (iMsg.isSucceed()) {
//                                myBaseActivity.toast("关注成功");
//                                removeItem(position);
//                            } else {
//                                myBaseActivity.toast(iMsg.getMsg());
//                            }
//                        }
//                    });
//                } else {
//                    myBaseActivity.startActivity(GroupApplyActivity.class);
//                }
            }
        });

        return convertView;
    }

    private void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, Notification item) {
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