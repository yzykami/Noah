package com.tzw.noah.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.utils.NetWorkUtils;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/7/10.
 */

public class SnsManager {
    //添加关注
    //sns/attention
    Context mContext;
    SnsDBManager snsDBManager;

    private static Handler mdelivery;

    public SnsManager(Context mContext) {
        this.mContext = mContext;
        snsDBManager = new SnsDBManager(mContext);
        mdelivery = new Handler(Looper.getMainLooper());
    }


    private IMsg createImsg() {
        IMsg imsg = new IMsg();
        imsg.setSucceed(true);
        return imsg;
    }

    //添加关注
    public void snsAttention(final User user, final Callback callback) {
        NetHelper.getInstance().snsAttention(user.memberNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        List<User> followList = DataCenter.getInstance().getFollowList();
                        List<User> fansList = DataCenter.getInstance().getFansList();
                        List<User> friendList = DataCenter.getInstance().getFriendList();

                        user.isAttention = true;
                        followList.add(user);
                        DataCenter.getInstance().setFollowList(followList);
                        snsDBManager.UpdateFollowList(followList);

                        for (int i = 0; i < fansList.size(); i++) {
                            if (fansList.get(i).memberNo == user.memberNo) {
                                friendList.add(user);
                                DataCenter.getInstance().setFriendList(friendList);
                                snsDBManager.UpdateFriendList(friendList);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {

                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }

    //取消关注
    //sns/unfollow/{memberNo}
    public void snsUnfollow(final int memberNo, final Callback callback) {
        NetHelper.getInstance().snsUnfollow(memberNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        List<User> followList = DataCenter.getInstance().getFollowList();
                        List<User> friendList = DataCenter.getInstance().getFriendList();

                        for (int i = 0; i < followList.size(); i++) {
                            if (followList.get(i).memberNo == memberNo) {
                                followList.remove(i);
                                DataCenter.getInstance().setFollowList(followList);
                                snsDBManager.UpdateFollowList(followList);
                                break;
                            }
                        }
                        for (int i = 0; i < friendList.size(); i++) {
                            if (friendList.get(i).memberNo == memberNo) {
                                friendList.remove(i);
                                DataCenter.getInstance().setFriendList(friendList);
                                snsDBManager.UpdateFriendList(friendList);
                                break;
                            }
                        }

                    }
                } catch (Exception e) {

                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }


    //移除粉丝
    //sns/removeFans/{memberNo}
    public void snsRemoveFans(final int memberNo, final Callback callback) {
        NetHelper.getInstance().snsRemoveFans(memberNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        List<User> fansList = DataCenter.getInstance().getFansList();
                        List<User> friendList = DataCenter.getInstance().getFriendList();

                        for (int i = 0; i < fansList.size(); i++) {
                            if (fansList.get(i).memberNo == memberNo) {
                                fansList.remove(i);
                                DataCenter.getInstance().setFansList(fansList);
                                snsDBManager.UpdateFansList(fansList);
                                break;
                            }
                        }
                        for (int i = 0; i < friendList.size(); i++) {
                            if (friendList.get(i).memberNo == memberNo) {
                                friendList.remove(i);
                                DataCenter.getInstance().setFriendList(friendList);
                                snsDBManager.UpdateFriendList(friendList);
                                break;
                            }
                        }

                    }
                } catch (Exception e) {

                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }

    //添加黑名单
    //sns/blacklist
    public void snsBlacklist(final User user, final Callback callback) {
        NetHelper.getInstance().snsBlacklist(user.memberNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        List<User> blackList = DataCenter.getInstance().getBlackList();
                        List<User> friendList = DataCenter.getInstance().getFriendList();

                        user.isBlacklist = true;
                        blackList.add(user);
                        DataCenter.getInstance().setBlackList(blackList);
                        snsDBManager.UpdateBlacklist(blackList);

                        for (int i = 0; i < friendList.size(); i++) {
                            if (friendList.get(i).memberNo == user.memberNo) {
                                friendList.remove(i);
                                DataCenter.getInstance().setFriendList(friendList);
                                snsDBManager.UpdateFriendList(friendList);
                                break;
                            }
                        }

                    }
                } catch (Exception e) {

                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }

    //移除黑名单
    //sns/removeBlacklist/{memberNo}
    public void snsRemoveBlacklist(final User user, final Callback callback) {
        NetHelper.getInstance().snsRemoveBlacklist(user.memberNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        List<User> blackList = DataCenter.getInstance().getBlackList();
                        List<User> friendList = DataCenter.getInstance().getFriendList();
                        List<User> followList = DataCenter.getInstance().getFollowList();
                        List<User> fansList = DataCenter.getInstance().getFansList();

                        user.isBlacklist = false;
                        for (int i = 0; i < blackList.size(); i++) {
                            if (blackList.get(i).memberNo == user.memberNo) {
                                blackList.remove(i);
                                DataCenter.getInstance().setBlackList(blackList);
                                snsDBManager.UpdateBlacklist(blackList);
                                break;
                            }
                        }

                        if (user.isFans && user.isAttention) {
                            friendList.add(user);
                            DataCenter.getInstance().setFriendList(friendList);
                            snsDBManager.UpdateFriendList(friendList);
                            followList.add(user);
                            DataCenter.getInstance().setFollowList(followList);
                            snsDBManager.UpdateFollowList(followList);
                            fansList.add(user);
                            DataCenter.getInstance().setFansList(fansList);
                            snsDBManager.UpdateFansList(fansList);
                        }

                    }
                } catch (Exception e) {
                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }

    //获取我的好友列表
    //sns/friends
    public void snsFriends(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsFriends(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<User> userList = User.loadFriendList(iMsg);
                            iMsg.Data = userList;
                            DataCenter.getInstance().setFriendList((List<User>) iMsg.Data);
                            snsDBManager.UpdateFriendList(userList);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsFriendList();
                        DataCenter.getInstance().setFriendList((List<User>) iMsg.Data);
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }

    //获取我的关注列表
    //sns/concern
    public void snsFollow(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsConcern(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<User> userList = User.loadFollowList(iMsg);
                            iMsg.Data = userList;
                            DataCenter.getInstance().setFollowList((List<User>) iMsg.Data);
                            snsDBManager.UpdateFollowList(userList);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsFollowList();
                        DataCenter.getInstance().setFollowList((List<User>) iMsg.Data);
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }

    //获取我的粉丝列表
    //sns/fans
    public void snsFans(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsFans(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<User> userList = User.loadFansList(iMsg);
                            iMsg.Data = userList;
                            DataCenter.getInstance().setFansList((List<User>) iMsg.Data);
                            snsDBManager.UpdateFansList(userList);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsFansList();
                        DataCenter.getInstance().setFansList((List<User>) iMsg.Data);
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }

    //获取我的黑名单列表
    //sns/blacks
    public void snsBlacks(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsBlacks(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<User> userList = User.loadBlackList(iMsg);
                            iMsg.Data = userList;
                            DataCenter.getInstance().setBlackList((List<User>) iMsg.Data);
                            snsDBManager.UpdateBlacklist(userList);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsBlacklist();
                        DataCenter.getInstance().setBlackList((List<User>) iMsg.Data);
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }

    //获取我的好友,关注,粉丝,黑名单列表
    //sns/myList
    public void snsMyList(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsMyList(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<User> friendList = User.loadMyList_Friend(iMsg);
                            List<User> followList = User.loadMyList_Follow(iMsg);
                            List<User> fansList = User.loadMyList_Fans(iMsg);
                            List<User> blackList = User.loadMyList_Black(iMsg);
                            DataCenter.getInstance().setFriendList(friendList);
                            DataCenter.getInstance().setFollowList(followList);
                            DataCenter.getInstance().setFansList(fansList);
                            DataCenter.getInstance().setBlackList(blackList);
                            snsDBManager.UpdateFriendList(friendList);
                            snsDBManager.UpdateFollowList(followList);
                            snsDBManager.UpdateFansList(fansList);
                            snsDBManager.UpdateBlacklist(blackList);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsFriendList();
                        List<User> friendList = snsDBManager.getSnsFriendList();
                        List<User> followList = snsDBManager.getSnsFollowList();
                        List<User> fansList = snsDBManager.getSnsFansList();
                        List<User> blackList = snsDBManager.getSnsBlacklist();

                        DataCenter.getInstance().setFriendList(friendList);
                        DataCenter.getInstance().setFollowList(followList);
                        DataCenter.getInstance().setFansList(fansList);
                        DataCenter.getInstance().setBlackList(blackList);

                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
//        NetHelper.getInstance().snsMyList(callback);
    }

    //获取附近的人,不用做缓存
    public void snsNearby(Callback callback) {
        NetHelper.getInstance().snsNearby(callback);
    }

    //获取推荐的人,不用做缓存
    public void snsRecommendUser(Callback callback) {
        NetHelper.getInstance().snsRecommendUser(callback);
    }

    public boolean isInBlacklist(int id) {
        return snsDBManager.isInBlacklist(id);
    }

    public void snsInfo(final User user, final Callback callback) {
//        if (NetWorkUtils.isNetworkAvailable(mContext))
        List<Param> body = new ArrayList<>();
        body.add(new Param("remarkName", user.remarkName));
        body.add(new Param("ifStar", user.ifStar));
        body.add(new Param("ifSeeHim", user.ifSeeHim));
        body.add(new Param("ifSeeMe", user.ifSeeMe));
        NetHelper.getInstance().snsInfo(user.memberNo, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onAfter();
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    //保存到本地数据库
                    if (iMsg.isSucceed()) {
                        snsDBManager.updateUser(user);

                    }
                } catch (Exception e) {

                }
                callback.onAfter();
                callback.onResponse(iMsg);
            }
        });
    }

    public void snsDetail(final User user, final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsDetails2(user.memberNo, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            User u = User.load(iMsg);
                            snsDBManager.updateUser(u);
                            iMsg.Data = u;
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getUserDetail(user.memberNo);
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }


    ///////////////////////////////////////////////////////////////////
    //////////////////////// 群组,多人会话接口  /////////////////////////////////
    ///////////////////////////////////////////////////////////////////


    //创建多人会话
    //sns/createDiscuss
    public void snsCreateDiscuss(List<String> ids, Callback callback) {
        NetHelper.getInstance().snsCreateDiscuss(ids, callback);
    }

    //添加多人会话成员
    //sns/AddUsersToDiscuss
    public void snsAddUsersToDiscuss(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsAddUsersToDiscuss(groupId, ids, callback);
    }

    // 移除多人会话成员
    //sns/KickUsersFromDiscuss
    public void snsKickUsersFromDiscuss(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsKickUsersFromDiscuss(groupId, ids, callback);
    }

    //修改多人会话资料
    //sns/discussInfo
    public void snsDiscussInfo(int groupId, List<Param> body, Callback callback) {
        NetHelper.getInstance().snsDiscussInfo(groupId, body, callback);
    }

    // 更新昵称多人会话
    //sns/updateNickToDiscuss
    public void snsUpdateNickToDiscuss(int groupId, String name, Callback callback) {
        NetHelper.getInstance().snsUpdateNickToDiscuss(groupId, name, callback);

    }

    //获取我的多人会话和群列表
    //sns/groups
    public void snsGroups(final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsGroups(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<Group> groupList = Group.loadGroupList(iMsg);
                            List<Group> discussList = Group.loadDiscussList(iMsg);
                            DataCenter.getInstance().setGroupList(groupList);
                            DataCenter.getInstance().setDiscussList(discussList);
                            List<Group> list = new ArrayList<>();
                            Utils.listAdd(list, groupList);
                            Utils.listAdd(list, discussList);
                            snsDBManager.UpdateGroupList(list);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();

                        List<Group> groupList = snsDBManager.getGroupList();
                        List<Group> discussList = snsDBManager.getDiscussList();
                        DataCenter.getInstance().setGroupList(groupList);
                        DataCenter.getInstance().setDiscussList(discussList);

                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }

    //获取我的多人会话和群列表
    //sns/snsGroupDetails
    public void snsGroupDetails(final int groupId, final Callback callback) {
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsGroupDetails(groupId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            Group group = Group.load(iMsg);
                            List<Group> list = new ArrayList<Group>();
                            list.add(group);
                            snsDBManager.UpdateGroupList(list);
                            DataCenter.getInstance().setGroup(group);
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();

                        Group group = snsDBManager.getGroup(groupId);
                        DataCenter.getInstance().setGroup(group);

                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }
    }


    //获取我的多人会话和群组信息通知
    //sns/groupNotification
    public void snsGroupNotification(Callback callback) {
        NetHelper.getInstance().snsGroupNotification(callback);
    }

    //获取我的多人会话和群组信息通知
    //sns/relationRecords
    public void snsRelationRecords(Callback callback) {
        NetHelper.getInstance().snsRelationRecords(callback);
    }

    //主动退出群（多人会话、群组）
    //sns/quit/
    public void snsQuit(int groupId, Callback callback) {
        NetHelper.getInstance().snsQuit(groupId, callback);
    }

    //移交群主（多人会话、群组）
    //sns/transfer/
    public void snsTransfer(int groupId, int memberNo, Callback callback) {
        NetHelper.getInstance().snsTransfer(groupId, memberNo, callback);
    }

    //主动退出群（多人会话、群组）
    //sns/dismiss/
    public void snsDismiss(int groupId, Callback callback) {
        NetHelper.getInstance().snsDismiss(groupId, callback);
    }

    //获取群成员（多人会话、群组）
    //sns/getMembers
    public void snsGetMembers(final int groupId, final Callback callback) {

        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsGetMembers(groupId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        if (iMsg.isSucceed()) {
                            List<GroupMember> ownerList = GroupMember.loadOwner(iMsg);
                            List<GroupMember> managerList = GroupMember.loadManager(iMsg);
                            List<GroupMember> memberList = GroupMember.loadMemberRObj(iMsg);
                            DataCenter.getInstance().setOwnerList(ownerList);
                            DataCenter.getInstance().setManagerList(managerList);
                            DataCenter.getInstance().setMemberList(memberList);
                            List<GroupMember> list = new ArrayList<>();
                            Utils.listAdd(list, ownerList);
                            Utils.listAdd(list, managerList);
                            Utils.listAdd(list, memberList);
                            DataCenter.getInstance().setGroupMemberList(list);

                            snsDBManager.UpdateGroupMemberlist(list);

                            iMsg.Data = list;
                        }
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        final IMsg iMsg = createImsg();

                        List<GroupMember> ownerList = snsDBManager.getOwnerList(groupId);
                        List<GroupMember> managerList = snsDBManager.getManagerList(groupId);
                        List<GroupMember> memberList = snsDBManager.getMemberList(groupId);

                        DataCenter.getInstance().setOwnerList(ownerList);
                        DataCenter.getInstance().setManagerList(managerList);
                        DataCenter.getInstance().setMemberList(memberList);

                        List<GroupMember> list = new ArrayList<>();
                        Utils.listAdd(list, ownerList);
                        Utils.listAdd(list, managerList);
                        Utils.listAdd(list, memberList);

                        DataCenter.getInstance().setGroupMemberList(list);

                        iMsg.Data = list;

                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                }
            });
        }

//        NetHelper.getInstance().snsGetMembers(groupId, callback);
    }

    //获取群组的类别
    //sns/groupType
    public void snsGroupType(Callback callback) {
        NetHelper.getInstance().snsGroupType(callback);
    }

    //创建群组
    //sns/createGroup
    public void snsCreateGroup(List<Param> body, Callback callback) {
        NetHelper.getInstance().snsCreateGroup(body, callback);
    }

    //邀请加群
    //sns/addUsersToGroup
    public void snsAddUsersToGroup(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsAddUsersToGroup(groupId, ids, callback);
    }

    // 移除群成员
    //sns/kickUsersFromGroup
    public void snsKickUsersFromGroup(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsKickUsersFromGroup(groupId, ids, callback);
    }

    // 修改群昵称
    //sns/updateNickToGroup
    public void snsUpdateNickToGroup(int groupId, List<Param> body, Callback callback) {
        NetHelper.getInstance().snsUpdateNickToGroup(groupId, body, callback);
    }

    //群组:添加管理员
    //sns/addManagersToGroup
    public void snsAddManagersToGroup(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsAddManagersToGroup(groupId, ids, callback);
    }

    // 移除群管理员
    //sns/removeManagersFromGroup
    public void snsRemoveManagersFromGroup(int groupId, List<String> ids, Callback callback) {
        NetHelper.getInstance().snsRemoveManagersFromGroup(groupId, ids, callback);
    }

    // 更新群资料
    //sns/updateGroupInfo
    public void snsUpdateGroupInfo(int groupId, List<Param> body, Callback callback) {
        NetHelper.getInstance().snsUpdateGroupInfo(groupId, body, callback);
    }

    // 申请加群
    //sns/applyToGroup
    public void snsApplyToGroup(int groupId, String msg, Callback callback) {
        NetHelper.getInstance().snsApplyToGroup(groupId, msg, callback);
    }

    //审批会员申请入群的请求
    //sns/handleApplyToGroup
    public void snsHandleApplyToGroup(List<Param> body, Callback callback) {
        NetHelper.getInstance().snsHandleApplyToGroup(body, callback);
    }

    //是否同意邀请入群
    //sns/handleInviteWithGroup
    public void snsHandleInviteWithGroup(List<Param> body, Callback callback) {
        NetHelper.getInstance().snsHandleInviteWithGroup(body, callback);
    }

    //群组设置(是否允许成员邀请、是否允许匿名聊天、加群验证方式)
    //sns/settingOfGroup
    public void snsSettingOfGroup(int groupId, List<Param> body, Callback callback) {
        NetHelper.getInstance().snsSettingOfGroup(groupId, body, callback);
    }

    //获取推荐的群
    public void snsRecommendGroup(Callback callback) {
        NetHelper.getInstance().snsRecommendGroup(callback);
    }
}
