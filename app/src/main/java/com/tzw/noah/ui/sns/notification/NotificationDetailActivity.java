package com.tzw.noah.ui.sns.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/13.
 */

public class NotificationDetailActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_name)
    TextView tv;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.tv_btn2)
    TextView tv_btn2;
    @BindView(R.id.tv_btn1)
    TextView tv_btn1;
    @BindView(R.id.iv_head)
    SampleImageViewHead iv_head;
    @BindView(R.id.ll)
    LinearLayout ll;

    List<Notification> items = new ArrayList<>();

    NotificationAdapter adapter;

    Context mContext = NotificationDetailActivity.this;
    NotificationDetailActivity instance;
    String Tag = "NotificationListActivity";
//    private AssemblyRecyclerAdapter adapter;

//    int selectPage;
//    Fragment[] fragmentList = null;

    String title = "我的群聊信息通知";
    Notification notification;
    int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_notification_detail);
        ButterKnife.bind(this);
        instance = this;
        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
            notification = (Notification) bu.getSerializable("DATA");
            position = bu.getInt("DATA2");
        }
    }

    private void findview() {

    }

    private void initview() {
//        tv_title.setText(title);
        if (notification.notificationType == 0) {
            if (notification.handleType == 0) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.sourceNickName);
                tv1.setText("邀请您加入: " + notification.groupName);
                tv2.setVisibility(View.GONE);
                tv_add.setVisibility(View.GONE);
                tv_add.setText("同意");
                tv_add.setTextColor(getResources().getColor(R.color.myRed));
                tv_add.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_round));

            } else if (notification.handleType == 1) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.sourceNickName);
                tv1.setText("邀请您加入: " + notification.groupName);
                tv2.setVisibility(View.GONE);
                tv_add.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                tv_add.setText("已拒绝");
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 2) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.sourceNickName);
                tv1.setText("邀请您加入: " + notification.groupName);
                tv2.setVisibility(View.GONE);
                tv_add.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                tv_add.setText("已同意");
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 3) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("申请加入: " + notification.groupName);
                tv2.setText(notification.requestInfo);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                tv_add.setText("同意");
                tv_add.setTextColor(getResources().getColor(R.color.myRed));
                tv_add.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 4) {
                iv_head.displayImage(notification.handleMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已拒绝您加入群");
                tv2.setText("处理者: " + notification.handleNickName);
                tv2.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                tv_add.setText("已拒绝");
                tv_add.setVisibility(View.GONE);
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 5) {
                iv_head.displayImage(notification.handleMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已同意你的申请");
                tv2.setText("处理者: " + notification.handleNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 6) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你移出群");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 7) {
                tv.setText(notification.groupName);
                tv1.setText("你已成功退群");
                tv2.setVisibility(View.GONE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 8) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你设为管理员");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 9) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已取消你的管理员");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 10) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你设为群主");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            }
        } else {
            if (notification.handleType == 0) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.sourceNickName);
                tv1.setText("邀请您加入: " + notification.groupName);
                tv2.setVisibility(View.GONE);
                tv_add.setText("同意");
                tv_add.setVisibility(View.GONE);
                tv_add.setTextColor(getResources().getColor(R.color.myRed));
                tv_add.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 1) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("已拒绝加入: " + notification.groupName);
                tv2.setText("邀请人: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                tv_add.setText("已拒绝");
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 2) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("已同意加入: " + notification.groupName);
                tv2.setText("邀请人: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                tv_add.setText("已同意");
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 3) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("申请加入: " + notification.groupName);
                tv2.setText(notification.requestInfo);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                tv_add.setText("同意");
                tv_add.setTextColor(getResources().getColor(R.color.myRed));
                tv_add.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_round));
            } else if (notification.handleType == 4) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("申请加入:" + notification.groupName + "");
                tv2.setText("处理者: " + notification.handleNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setText("已拒绝");
                ll.setVisibility(View.GONE);
                tv_add.setVisibility(View.VISIBLE);
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 5) {
                iv_head.displayImage(notification.memberHeadPic);
                tv.setText(notification.memberNickName);
                tv1.setText("申请加入:" + notification.groupName + "");
                tv2.setText("处理者: " + notification.handleNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setText("已同意");
                ll.setVisibility(View.GONE);
                tv_add.setVisibility(View.VISIBLE);
                tv_add.setTextColor(getResources().getColor(R.color.textLightGray));
                tv_add.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (notification.handleType == 6) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你移出群");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 7) {
                tv.setText(notification.groupName);
                tv1.setText("你已成功退群");
                tv2.setVisibility(View.GONE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 8) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你设为管理员");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 9) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已取消你的管理员");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            } else if (notification.handleType == 10) {
                iv_head.displayImage(notification.sourceMemberHeadPic);
                tv.setText(notification.groupName);
                tv1.setText("已将你设为群主");
                tv2.setText("处理者: " + notification.sourceNickName);
                tv2.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            }
        }

        
        tv_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.notificationType == 0) {
                    if (notification.handleType == 0) {
                        List<Param> body = new ArrayList<Param>();
                        body.add(new Param("groupId", notification.groupId));
                        body.add(new Param("memberNo", notification.sourceNo));
                        body.add(new Param("status", 1));
//                        body.add(new Param("rejectReason",));
                        new SnsManager(mContext).snsHandleInviteWithGroup(body, new StringDialogCallback(instance) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                toast(getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    toast("已同意邀请");
                                    notification.handleType = 2;
                                    NotificationListActivity.getInstance().updateListItem(position,notification);
                                    finish();
                                } else {
                                    toast(iMsg.getMsg());
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
                        new SnsManager(mContext).snsHandleApplyToGroup(body, new StringDialogCallback(instance) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                toast(getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    toast("已同意申请");
                                    notification.handleType = 5;
                                    NotificationListActivity.getInstance().updateListItem(position,notification);
                                    finish();
                                } else {
                                    toast(iMsg.getMsg());
                                }
                            }
                        });
                    }
                }
            }
        });


        tv_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.notificationType == 0) {
                    if (notification.handleType == 0) {
                        List<Param> body = new ArrayList<Param>();
                        body.add(new Param("groupId", notification.groupId));
                        body.add(new Param("memberNo", notification.sourceNo));
                        body.add(new Param("status", 2));
//                        body.add(new Param("rejectReason",));
                        new SnsManager(mContext).snsHandleInviteWithGroup(body, new StringDialogCallback(instance) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                toast(getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    toast("已拒绝邀请");
                                    notification.handleType = 1;
                                    NotificationListActivity.getInstance().updateListItem(position,notification);
                                    finish();
                                } else {
                                    toast(iMsg.getMsg());
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
                        body.add(new Param("status", 2));
//                        body.add(new Param("rejectReason",));
                        new SnsManager(mContext).snsHandleApplyToGroup(body, new StringDialogCallback(instance) {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                toast(getResources().getString(R.string.internet_fault));
                            }

                            @Override
                            public void onResponse(IMsg iMsg) {
                                if (iMsg.isSucceed()) {
                                    toast("已拒绝申请");
                                    notification.handleType = 4;
                                    NotificationListActivity.getInstance().updateListItem(position,notification);
                                    finish();
                                } else {
                                    toast(iMsg.getMsg());
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
