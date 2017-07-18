package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
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
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/4.
 */

public class GroupManagerActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_joinmode)
    TextView tv_joinmode;

    @BindView(R.id.iv_anonymous)
    ImageView iv_anonymous;
    @BindView(R.id.iv_findbyid)
    ImageView iv_findbyid;
    @BindView(R.id.iv_invited)
    ImageView iv_invited;

    List<GroupMember> items;
    Context mContext = GroupManagerActivity.this;
    String Tag = "GroupManagerActivity";

    Group group;
    Bundle bu;

    boolean isAnonymous = false;
    boolean isFindbyid = false;
    boolean isInvited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_manager);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
        isAnonymous = group.ifAnonymous == 0 ? true : false;
//        isFindbyid =group.ifInvited==0?true:false;
        isInvited = group.ifInvited == 0 ? true : false;

    }

    private void findview() {

    }

    private void initview() {
        tv_num.setText("");
        setBackground(iv_anonymous, isAnonymous);
        setBackground(iv_findbyid, isFindbyid);
        setBackground(iv_invited, isInvited);
        List<String> items_joinmode = new ArrayList<>();
        items_joinmode.add("不用验证");
        items_joinmode.add("需要验证");
        items_joinmode.add("不允许任何人加入");
        if (group.joinmode < 3) {
            tv_joinmode.setText(items_joinmode.get(group.joinmode));
        }
    }

    private void setBackground(ImageView iv, boolean isChecked) {
        if (isChecked) {
            iv.setImageResource(R.drawable.btn1_selected);
        } else {
            iv.setImageResource(R.drawable.btn1_unselect);
        }
    }

    public void handle_edit(View view) {
        if (group.myMemberType == Group.MemberType.MANAGER || group.myMemberType == Group.MemberType.OWNER) {
            startActivity(GroupEditActivity.class, bu);
        }
    }

    public void handle_setadmin(View view) {
        startActivity(GroupSetAdminActivity.class, bu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<GroupMember>) iMsg.Data;
                        else
                            items = GroupMember.loadManager(iMsg);
                        if (items == null)
                            items = new ArrayList<GroupMember>();
                        tv_num.setText(items.size() + "/10");
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    public void handle_slient(View view) {

    }

    public void handle_anonymous(View view) {

        isAnonymous = !isAnonymous;
        setBackground(iv_anonymous, isAnonymous);
        /*

        List<Param> body = new ArrayList<>();
        boolean temp_isAnonymous = !isAnonymous;
        body.add(new Param("ifInvited", group.ifInvited));
        body.add(new Param("ifAnonymous", temp_isAnonymous == true ? 0 : 2));
        body.add(new Param("joinmode", group.joinmode));
        new SnsManager(mContext).snsSettingOfGroup(group.groupId, body, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        isAnonymous = !isAnonymous;
                        setBackground(iv_anonymous, isAnonymous);
                        group.ifAnonymous = isAnonymous == true ? 0 : 2;
                        toast("群匿名聊天方式修改成功");
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
*/
    }

    public void handle_joinmode(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditJoinModeActivity.class, bu);
    }

    public void handle_findbyid(View view) {
        isFindbyid = !isFindbyid;
        setBackground(iv_findbyid, isFindbyid);
    }

    public void handle_invited(View view) {
        List<Param> body = new ArrayList<>();
        boolean temp_isInvited = !isInvited;
        body.add(new Param("ifInvited", temp_isInvited == true ? 0 : 1));
//        body.add(new Param("ifAnonymous", group.ifAnonymous));
        body.add(new Param("joinmode", group.joinmode));
        new SnsManager(mContext).snsSettingOfGroup(group.groupId, body, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        isInvited = !isInvited;
                        setBackground(iv_invited, isInvited);
                        group.ifInvited = isInvited == true ? 0 : 1;

                        toast("群邀请方式修改成功");
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    public void handle_transfer(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupTransferOwnerActivity.class, bu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100)
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
                setResult(200);
                finish();
            }
    }

    public void handle_back() {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        Intent intent = new Intent();
        intent.putExtras(bu);
        setResult(100, intent);
        finish();
    }
}
