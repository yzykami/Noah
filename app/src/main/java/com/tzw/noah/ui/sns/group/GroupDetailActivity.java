package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.init.NimInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.BottomPopupWindow;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.discuss.DiscussDetailActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.ListenedScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/3.
 */

public class GroupDetailActivity extends MyBaseActivity implements BottomPopupWindow.OnItemClickListener, ListenedScrollView.OnScrollListener {
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.rl_manager)
    RelativeLayout rl_manager;
    @BindView(R.id.ll_member)
    LinearLayout ll_member;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_id)
    TextView tv_group_id;
    @BindView(R.id.tv_group_name1)
    TextView tv_group_name1;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_bulletin)
    TextView tv_bulletin;
    @BindView(R.id.iv_top)
    ImageView iv_top;
    @BindView(R.id.iv_slient)
    ImageView iv_slient;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.iv_bg)
    SampleImageView iv_bg;
    @BindView(R.id.sl_root)
    ListenedScrollView sl_root;
    @BindView(R.id.tv_title)
    TextView tv_title;

    boolean isIvTop = false;
    boolean isIvSlient = false;


    Context mContext = GroupDetailActivity.this;

    List<GroupMember> items;
    String Tag = "GroupDetailActivity";

    Group group;

    boolean isFirstLoad = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_detail);
        ButterKnife.bind(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
        initdata();
        findview();
        initview();
//        getMemberList();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
    }

    private void findview() {
        ll_member.removeAllViews();
    }

    private void initview() {
        tv_group_name.setText(group.groupName);
        tv_group_name1.setText(group.groupName);
        tv_group_id.setText("群号: " + group.groupId);
        tv_count.setText(group.memberCount + "人");
        String nickname = "未设置";
        if (!group.myGroupMemberName.isEmpty()) {
            nickname = group.myGroupMemberName;
        }
        tv_nickname.setText(nickname);
        String bulletin = "未设置";
        if (!group.groupBulletin.isEmpty()) {
            bulletin = group.groupBulletin;
        }
        tv_bulletin.setText(bulletin);
        tv_introduce.setText(group.groupIntroduction);

        if (group.myMemberType == Group.MemberType.MEMBER)
            rl_manager.setVisibility(View.GONE);
        setBackground(iv_top, isIvTop);
        setBackground(iv_slient, isIvSlient);

        iv_bg.getOptions().setErrorImage(R.drawable.sns_group_bg);
        iv_bg.getOptions().setLoadingImage(R.drawable.sns_group_bg);
        iv_bg.displayImage(group.groupHeader);

        sl_root.setOnScrollListener(this);
    }

    private View getMemberView(final GroupMember gm) {

        float span = getResources().getDimension(R.dimen.bj);

        float sw = Utils.getSrceenWidth();

        int itemSize = (int) ((sw - 7 * span) / 6);

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.sns_group_member_item, null);

        SampleImageViewHead iv = (SampleImageViewHead) rl.findViewById(R.id.iv_head);
        TextView tv_name = (TextView) rl.findViewById(R.id.tv_name);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();

        int nn = (int) span;

        layoutParams.width = itemSize;
        layoutParams.height = itemSize;
        layoutParams.setMargins(nn, 0, 0, nn / 2);
        iv.setLayoutParams(layoutParams);
        if (gm.memberNo == -1) {
            iv.getOptions().setLoadingImage(R.drawable.sns_add_person);
            iv.getOptions().setErrorImage(R.drawable.sns_add_person);
        }
        iv.displayImage(gm.memberHeadPic);

        tv_name.setText(gm.getMemberName());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gm.memberNo == -1) {
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", (ArrayList) items);
                    bu.putSerializable("DATA2", group);
                    startActivity(GroupAddMemberActivity.class, bu);
                } else {
                    User user = new User();
                    user.memberNo = gm.memberNo;
                    user.memberNickName = gm.getMemberName();
                    user.memberHeadPic = gm.memberHeadPic;
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", user);
                    startActivity(PersonalActivity.class, bu);
                }
            }
        });

        return rl;
    }


    public void handle_memberlist(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivity(GroupMemberListActivity.class, bu);
    }

    public void handle_more(View view) {
        if (group.myMemberType == Group.MemberType.OWNER) {
            BottomPopupWindow.create(this, this).addItem("转让该群").addItem("解散该群", R.color.myRed).show();//.addItem("分享群").addItem("举报")
        } else
            BottomPopupWindow.create(this, this).addItem("退出该群", R.color.myRed).show();//.addItem("分享群").addItem("举报")
    }

    @Override
    public void OnItemClick(int position, String title) {
        if (title.equals("分享群")) {

        } else if (title.equals("举报")) {

        } else if (title.equals("退出该群")) {
            new SnsManager(mContext).snsQuit(group.groupId, new StringDialogCallback(this) {
                @Override
                public void onFailure(Call call, IOException e) {
                    toast(getResources().getString(R.string.internet_fault));
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            toast("退出成功");
                            finish();
                        } else {
                            toast(iMsg.getMsg());
                        }
                    } catch (Exception e) {
                        Log.log(Tag, e);
                    }
                }
            });

        } else if (title.equals("解散该群")) {
            new SnsManager(mContext).snsDismiss(group.groupId, new StringDialogCallback(this) {
                @Override
                public void onFailure(Call call, IOException e) {
                    toast(getResources().getString(R.string.internet_fault));
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            toast("群解散成功");
                            finish();
                        } else {
                            toast(iMsg.getMsg());
                        }
                    } catch (Exception e) {
                        Log.log(Tag, e);
                    }
                }
            });
        } else if (title.equals("转让该群")) {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", group);
            startActivityForResult(100, GroupTransferOwnerActivity.class, bu);
        } else if (title.equals("")) {

        }
    }

    public void getMemberList() {

        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        items = DataCenter.getInstance().getGroupMemberList();
                        group.memberCount = items.size();
                        tv_count.setText(group.memberCount + "人");
                        ll_member.removeAllViews();
                        for (int i = 0; i < 5 && i < items.size(); i++) {
//                            items.get(i).memberHeadPic = "drawable://" + R.drawable.sns_user_default;
                            ll_member.addView(getMemberView(items.get(i)));
                        }
                        GroupMember gm = new GroupMember();
                        gm.memberNo = -1;
                        gm.memberHeadPic = "drawable://" + R.drawable.sns_add_person;
                        ll_member.addView(getMemberView(gm));
//                            gm.memberHeadPic = "drawable://" + R.drawable.sns_delete_person;
//                            ll_member.addView(getMemberView(gm));
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void setBackground(ImageView iv, boolean isChecked) {

        if (isChecked) {
            iv.setImageResource(R.drawable.btn1_selected);
        } else {
            iv.setImageResource(R.drawable.btn1_unselect);
        }
    }

    public void handle_top(View view) {
        isIvTop = !isIvTop;
        setBackground(iv_top, isIvTop);
    }

    public void handle_slient(View view) {
        isIvSlient = !isIvSlient;
        setBackground(iv_slient, isIvSlient);
    }

    public void handle_edit_groupname(View view) {
        if (group.myMemberType == Group.MemberType.MANAGER || group.myMemberType == Group.MemberType.OWNER) {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", group);
            startActivityForResult(100, GroupEditNameActivity.class, bu);
        }
    }

    public void handle_edit_introduce(View view) {
        if (group.myMemberType == Group.MemberType.MEMBER)
            return;
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditIntroduceActivity.class, bu);
    }

    public void handle_edit_bulletin(View view) {
        if (group.myMemberType == Group.MemberType.MEMBER)
            return;
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditBulletinActivity.class, bu);
    }

    public void handle_edit_groupmembername(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditMemberNameActivity.class, bu);
    }

    public void handle_manager(View view) {
        if (group.myMemberType == Group.MemberType.MEMBER)
            return;
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupManagerActivity.class, bu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == 100) {
                if (data != null) {
                    Bundle bu = data.getExtras();
                    if (bu != null) {
                        group = (Group) bu.getSerializable("DATA");
                        initview();
                    }
                }
            }
            //转让群
            else if (resultCode == 200) {
                finish();
            }
        } else if (requestCode == 101) {
            if (resultCode == 100) {
                getMemberList();
            }
        }
    }

    public void handle_send(View view) {
        NimUIKit.startTeamSession(mContext, group.netEaseGroupId + "");
    }

    @Override
    public void onBottomArrived() {

    }

    @Override
    public void onScrollStateChanged(ListenedScrollView view, int scrollState) {
//        android.util.Log.d("aaa", view.getScrollY() + " " + scrollState);
        if (scrollState == ListenedScrollView.OnScrollListener.SCROLL_STATE_IDLE) {
            int oldt = view.getScrollY();
            int rl_top_y = (int) (rl_top.getY() + rl_top.getHeight());
            int tv_group_name_y = (int) (((View) tv_group_name.getParent()).getTop());// + tv_group_name.getHeight()
            int tv_group_name_h = tv_group_name.getHeight();

            int baseAlpha = 60;
            int alpha = 0;

            if (oldt >= tv_group_name_y - rl_top_y) {

                alpha = Math.min(255, (int) (Math.abs(oldt - tv_group_name_y + rl_top_y) * (255 - baseAlpha) / (tv_group_name_h) + baseAlpha));
                rl_top.setBackgroundColor(getResources().getColor(R.color.myRed));
                rl_top.getBackground().setAlpha(alpha);
                if (alpha >= 255)
                    tv_title.setText(group.groupName);
                else
                    tv_title.setText("");
            } else {
                rl_top.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        int rl_top_y = (int) (rl_top.getY() + rl_top.getHeight());
        int tv_group_name_y = (int) (((View) tv_group_name.getParent()).getTop());// + tv_group_name.getHeight()
        int tv_group_name_h = tv_group_name.getHeight();

        int baseAlpha = 60;
        int alpha = 0;

        if (oldt >= tv_group_name_y - rl_top_y) {

            alpha = Math.min(255, (int) (Math.abs(oldt - tv_group_name_y + rl_top_y) * (255 - baseAlpha) / (tv_group_name_h) + baseAlpha));
            rl_top.setBackgroundColor(getResources().getColor(R.color.myRed));
            rl_top.getBackground().setAlpha(alpha);
            if (alpha >= 255)
                tv_title.setText(group.groupName);
            else
                tv_title.setText("");
        } else {
            rl_top.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isFirstLoad)
//            isFirstLoad = false;
//        else {
            refetchGroup();
//        }
    }

    private void refetchGroup() {
        new SnsManager(mContext).snsGroupDetails(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.internet_fault), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        Group g = DataCenter.getInstance().getGroup();
                        group = g;
                        initview();
                    } else {
                        Toast.makeText(mContext, iMsg.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.log("GobalObserverImpl", e);
                }
            }
        });
        getMemberList();
    }
}
