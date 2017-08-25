package com.tzw.noah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.netease.nim.demo.main.reminder.ReminderItem;
import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.tzw_relative.GobalObserver;
import com.netease.nim.uikit.tzw_relative.Group;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.db.MyField;
import com.tzw.noah.init.NimInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.sns.add.AddActivity;
import com.tzw.noah.ui.sns.discuss.DiscussDetailActivity;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/7/21.
 */

public class GobalObserverImpl implements GobalObserver {
    @Override
    public void onShowUser(final Context context, String acount, int memberNo) {
        //Toast.makeText(context, "onShowUser", Toast.LENGTH_LONG).show();
        int netEaseId = Utils.String2Int(acount);

        if (netEaseId == 0) {
            Toast.makeText(context, "用户id不正确", Toast.LENGTH_LONG).show();
            return;
        }
        final User user = new User();
        user.netEaseId = netEaseId;
        user.memberNo = memberNo;
        //只有nimID, 没有memberNo
        memberNo = 0;
        if (memberNo == 0) {
            new SnsManager(context).snsDetailNimId(user, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            User u = (User) iMsg.Data;
                            NimInit.updateUser(u);
                            Bundle bu = new Bundle();
                            bu.putSerializable("DATA", u);
                            Intent intent = new Intent(context, PersonalActivity.class);
                            intent.putExtras(bu);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, iMsg.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.log("GobalObserverImpl", e);
                    }
                }
            });

        } else {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", user);
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtras(bu);
            context.startActivity(intent);
        }
    }

    @Override
    public void onShowTeam(final Context context, String acount, int groupId, Group group) {
//        Toast.makeText(context, "onShowTeam", Toast.LENGTH_LONG).show();
        final int netEaseGroupId = Utils.String2Int(acount);

        if (netEaseGroupId == 0) {
            Toast.makeText(context, "群组id不正确", Toast.LENGTH_LONG).show();
            return;
        }
        if (group == null) {
            new SnsManager(context).snsGroups(new StringDialogCallback(context) {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(context, context.getResources().getString(R.string.internet_fault), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            List<com.tzw.noah.models.Group> gs = DataCenter.getInstance().getGroupList();
                            com.tzw.noah.models.Group g=null;
                            NimInit.updateGroups(context);
                            for (int i = 0; i < gs.size(); i++) {
                                com.tzw.noah.models.Group curg = gs.get(i);
                                if (curg.netEaseGroupId == netEaseGroupId)
                                {
                                    g = curg;
                                    break;
                                }
                            }
                            if(g==null) {
                                Toast.makeText(context, "您已经不在此群中", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Bundle bu = new Bundle();
                            bu.putSerializable("DATA", g);
                            Intent intent = null;
                            if (g.groupAttribute == com.tzw.noah.models.Group.Type.GROUP)
                                intent = new Intent(context, GroupDetailActivity.class);
                            else
                                intent = new Intent(context, DiscussDetailActivity.class);
                            intent.putExtras(bu);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, iMsg.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.log("GobalObserverImpl", e);
                    }
                }
            });

//            new SnsManager(context).snsGroupDetails(groupId, new StringDialogCallback(context) {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Toast.makeText(context, context.getResources().getString(R.string.internet_fault), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onResponse(IMsg iMsg) {
//                    try {
//                        if (iMsg.isSucceed()) {
//                            com.tzw.noah.models.Group g = DataCenter.getInstance().getGroup();
//                            NimInit.updateGroup(g);
//                            Bundle bu = new Bundle();
//                            bu.putSerializable("DATA", g);
//                            Intent intent = null;
//                            if (g.groupAttribute == com.tzw.noah.models.Group.Type.GROUP)
//                                intent = new Intent(context, GroupDetailActivity.class);
//                            else
//                                intent = new Intent(context, DiscussDetailActivity.class);
//                            intent.putExtras(bu);
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context, iMsg.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Log.log("GobalObserverImpl", e);
//                    }
//                }
//            });
        } else {

            com.tzw.noah.models.Group g = new com.tzw.noah.models.Group();

            g.netEaseGroupId = group.netEaseGroupId;
            g.groupName = group.groupName;
            g.groupIntroduction = group.groupIntroduction;
            g.groupBulletin = group.groupBulletin;
            g.groupHeader = group.groupHeader;
            g.groupTypeId = group.groupTypeId;
            g.groupAttribute = group.groupAttribute;
            g.ifInvited = group.ifInvited;
            g.ifAnonymous = group.ifAnonymous;
            g.ifEnabled = group.ifEnabled;
            g.reviewState = group.reviewState;
            g.reviewRemark = group.reviewRemark;
            g.auditorId = group.auditorId;
            g.reviewTime = group.reviewTime;
            g.createTime = group.createTime;
            g.joinmode = group.joinmode;
            g.ownerNo = group.ownerNo;
            g.myGroupMemberName = group.myGroupMemberName;
            g.myMemberType = group.myMemberType;
            g.memberCount = group.memberCount;
            g.initialGroupName = group.initialGroupName;
            g.groupId = group.groupId;

            Bundle bu = new Bundle();
            bu.putSerializable("DATA", g);
            Intent intent = null;
            if (g.groupAttribute == com.tzw.noah.models.Group.Type.GROUP)
                intent = new Intent(context, GroupDetailActivity.class);
            else
                intent = new Intent(context, DiscussDetailActivity.class);
            intent.putExtras(bu);
            context.startActivity(intent);
        }
    }

    @Override
    public void onItemAddClick(Context context) {
        Intent intent = new Intent(context, AddActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onUnreadNumChanged(Object o) {
        if (MainActivity.getInstance() == null) {
            return;
        } else {
            MainActivity.getInstance().onUnreadNumChanged((ReminderItem) o);
        }
    }
}
