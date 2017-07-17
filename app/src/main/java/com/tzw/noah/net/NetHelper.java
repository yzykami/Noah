package com.tzw.noah.net;

import android.content.Context;

import com.tzw.noah.AppContext;
import com.tzw.noah.utils.DeviceUuidFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/14.
 */

public class NetHelper {

    public static NetHelper getInstance() {
        return new NetHelper();
    }

    //base/time
    public IMsg getBaseTime() {
        String method = "base/time";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/deviceID
    public IMsg getBaseDeviceID() {
        String method = "base/deviceID";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/config
    public IMsg getBaseConfig() {
        String method = "base/config";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/area
    public IMsg getBaseArea() {
        String method = "base/area";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/appcache
    public IMsg getBaseAppCache() {
        String method = "base/appCache";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/allAppCache
    public IMsg getBaseAllAppCache() {
        String method = "base/allAppCache";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/dictType
    public IMsg getBaseDictType() {
        String method = "base/dictType";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/dict
    public IMsg getBaseDict() {
        String method = "base/dict";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    public IMsg setDeviceId(String deviceId) {
        String method = "base/deviceNo";
        List<Param> body = new ArrayList<>();
        body.add(new Param("clientCode", deviceId));
        IMsg imsg = new WIRequest().Post(method, body);
        return imsg;
    }

    //检查设备ID是否已经上传到服务器，已上传返回true，未上传则更新，更新成功返回true，网络或者其他问题返回false
    public IMsg checkDeviceId() {
        Context context = AppContext.getContext();
        IMsg iMsg = new IMsg();
        iMsg.setSucceed(true);
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        if (!deviceUuidFactory.isIsupdated()) {
            iMsg = setDeviceId(deviceUuidFactory.getDeviceUuidString());
            if (iMsg.isSucceed())
                deviceUuidFactory.setUpdate();
            return iMsg;
        }
        return iMsg;
    }

    //注册
    //member/register
    public void memberRegister(List<Param> body, Callback callback) {
        String method = "member/register";
        String bodyName = "registerSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //登录
    //member/Login
    public IMsg memberLogin(List<Param> body) {
        IMsg iMsg = checkDeviceId();
        if (iMsg.isSucceed()) {
            String method = "member/login";
            String bodyName = "loginSObj";
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(AppContext.getContext());
            body.add(new Param("clientCode", deviceUuidFactory.getDeviceUuidString()));
            iMsg = new WIRequest().Post(method, body, bodyName);
            return iMsg;
        } else {
            return iMsg;
        }
    }

    //获取loginkey
    //member/loginKey
    public IMsg memberLoginKey(List<Param> body) {
        String method = "member/loginKey";
        String bodyName = "";
        IMsg iMsg = new WIRequest().Post(method, body, bodyName);
        return iMsg;
    }

    //注销
    public void memberLogout(List<Param> body, Callback callback) {
        String method = "member/logout";
        String bodyName = "";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //获取系统推荐昵称
    //member/nicknames
    public void memberNicknames(Callback callback) {
        String method = "member/nicknames";
        String bodyName = "";
        new WIRequest().Get(method, callback);
    }

    //获取系统推荐昵称
    //operation/feedback
    public void operationFeedback(List<Param> body, Callback callback) {
        String method = "operation/feedback";
        String bodyName = "feedbackSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //user/details
    public IMsg getUserDetails() {
        String method = "user/details";
        IMsg iMsg = new WIRequest().Post(method, null);
        return iMsg;
    }

    //user/details
    public void setUserInfo(List<Param> body, Callback callback) {
        String method = "user/info";
        String bodyName = "infoSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //user/password
    public void setUserPassword(List<Param> body, Callback callback) {
        String method = "user/password";
        String bodyName = "passwordSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //user/device
    public void getUserDevice(List<Param> body, Callback callback) {
        String method = "user/device";
        new WIRequest().Post(method, body, callback);
    }

    /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////            SNS                ///////////////////
    //添加关注
    //sns/attention
    public void snsAttention(int memberNo, Callback callback) {

        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNo", memberNo));
        String method = "sns/attention";
        new WIRequest().Post(method, body, callback);
    }

    //取消关注
    //sns/unfollow/{memberNo}
    public void snsUnfollow(int memberNo, Callback callback) {
        String method = "sns/unfollow/" + memberNo;
        new WIRequest().Delete(method, callback);
    }

    //移除粉丝
    //sns/removeFans/{memberNo}
    public void snsRemoveFans(int memberNo, Callback callback) {
        String method = "sns/removeFans/" + memberNo;
        new WIRequest().Delete(method, callback);
    }

    //添加黑名单
    //sns/blacklist
    public void snsBlacklist(int memberNo, Callback callback) {
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNo", memberNo));
        String method = "sns/blacklist";
        new WIRequest().Post(method, body, callback);
    }

    //移除黑名单
    //sns/removeBlacklist/{memberNo}
    public void snsRemoveBlacklist(int memberNo, Callback callback) {
        String method = "sns/removeBlacklist/" + memberNo;
        new WIRequest().Delete(method, callback);
    }

    //获取我的好友列表
    //sns/friends
    public void snsFriends(Callback callback) {
        String method = "sns/friends";
        new WIRequest().Get(method, callback);
    }

    //获取我的关注列表
    //sns/concern
    public void snsConcern(Callback callback) {
        String method = "sns/concern";
        new WIRequest().Get(method, callback);
    }

    //获取我的粉丝列表
    //sns/fans
    public void snsFans(Callback callback) {
        String method = "sns/fans";
        new WIRequest().Get(method, callback);
    }

    //获取我的黑名单列表
    //sns/blacks
    public void snsBlacks(Callback callback) {
        String method = "sns/blacks";
        new WIRequest().Get(method, callback);
    }

    //获取我的好友,关注,粉丝,黑名单列表
    //sns/myList
    public void snsMyList(Callback callback) {
        String method = "sns/myList";
        new WIRequest().Get(method, callback);
    }

    //更新我的好友设置
    //sns/info
    public void snsInfo(int id, List<Param> body, Callback callback) {
        String method = "sns/info" + "/" + id;
        String bodyName = "infoSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //获取个人资料，公开，不需要登录
    //sns/details/memberNo/
    public void snsDetails(int id, Callback callback) {
        String method = "sns/details/memberNo/" + id;
        new WIRequest().Get(method, callback);
    }

    //获取个人资料，私密，需要登录
    //sns/details2/memberNo/
    public void snsDetails2(int id, Callback callback) {
        String method = "sns/details2/memberNo/" + id;
        new WIRequest().Get(method, callback);
    }

    //获取我的附近的人列表
    //sns/nearby
    public void snsNearby(Callback callback) {
        String method = "sns/nearby";
        new WIRequest().Get(method, callback);
    }

    //获取我的推荐的人列表
    //sns/recommendUser
    public void snsRecommendUser(Callback callback) {
        String method = "sns/recommendUser";
        new WIRequest().Get(method, callback);
    }

    /////////////////////会话////////////////////////////////////

    //创建多人会话
    //sns/createDiscuss
    public void snsCreateDiscuss(List<String> ids, Callback callback) {
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("members", idss));
        String method = "sns/createDiscuss";
        String bodyName = "discussSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //添加多人会话
    //sns/addUsersToDiscuss
    public void snsAddUsersToDiscuss(int groupId, List<String> ids, Callback callback) {
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("groupId", groupId));
        body.add(new Param("members", idss));
        String method = "sns/addUsersToDiscuss";
        String bodyName = "addToDiscussSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    // 移除多人会话
    //sns/kickUsersFromDiscuss
    public void snsKickUsersFromDiscuss(int groupId, List<String> ids, Callback callback) {
        String method = "sns/kickUsersFromDiscuss" + "/" + groupId;
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("members", idss));
        new WIRequest().Put(method, body, callback);
    }

    //修改多人会话资料
    //sns/discussInfo
    public void snsDiscussInfo(int groupId, List<Param> body, Callback callback) {
        String method = "sns/discussInfo" + "/" + groupId;
        String bodyName = "discussInfoSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    // 更新昵称多人会话
    //sns/updateNickToDiscuss
    public void snsUpdateNickToDiscuss(int groupId, String name, Callback callback) {
        String method = "sns/updateNickToDiscuss" + "/" + groupId;
        List<Param> body = new ArrayList<>();
        body.add(new Param("groupMemberNickName", name));
        new WIRequest().Put(method, body, callback);
    }

    //获取我的多人会话和群列表
    //sns/groups
    public void snsGroups(Callback callback) {
        String method = "sns/groups";
        new WIRequest().Get(method, callback);
    }


    //获取群成员（多人会话、群组）
    //sns/getMembers
    public void snsGetMembers(int groupId, Callback callback) {
        String method = "sns/getMembers?groupId=" + groupId;
        new WIRequest().Get(method, callback);
    }

    //移交群主（多人会话、群组）
    //sns/transfer/
    public void snsTransfer(int groupId, int memberNo, Callback callback) {
        List<Param> body = Param.makeSingleParam("memberId",memberNo);
        String method = "sns/transfer/" + groupId;
        String bodyName = "changeOwnerSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //获取群组的类别
    //sns/groupType
    public void snsGroupType(Callback callback) {
        String method = "sns/groupType";
        new WIRequest().Get(method, callback);
    }

    // 创建群
    //sns/createGroup
    public void snsCreateGroup(List<Param> body, Callback callback) {
        String method = "sns/createGroup";
        String bodyName = "groupSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //邀请加群
    //sns/addUsersToGroup
    public void snsAddUsersToGroup(int groupId, List<String> ids, Callback callback) {
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("groupId", groupId));
        body.add(new Param("members", idss));
        String method = "sns/addUsersToGroup";
        String bodyName = "addToGroupSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    // 移除群成员
    //sns/kickUsersFromGroup
    public void snsKickUsersFromGroup(int groupId, List<String> ids, Callback callback) {
        String method = "sns/kickUsersFromGroup/" + groupId;
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("members", idss));
        new WIRequest().Put(method, body, callback);
    }
    // 修改群昵称
    //sns/updateNickToGroup
    public void snsUpdateNickToGroup(int groupId, List<Param> body, Callback callback) {
        String method = "sns/updateNickToGroup/" + groupId;
        String bodyName = "changeNicknameSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //群组:添加管理员
    //sns/addManagersToGroup
    public void snsAddManagersToGroup(int groupId, List<String> ids, Callback callback) {
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("groupId", groupId));
        body.add(new Param("members", idss));
        String method = "sns/addManagersToGroup";
        String bodyName = "managerSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    // 移除群管理员
    //sns/removeManagersFromGroup
    public void snsRemoveManagersFromGroup(int groupId, List<String> ids, Callback callback) {
        String method = "sns/removeManagersFromGroup/" + groupId;
        List<Param> body = new ArrayList<>();
        String idss = "";
        for (int i = 0; i < ids.size(); i++) {
            idss += ids.get(i) + ",";
        }
        body.add(new Param("members", idss));
        new WIRequest().Put(method, body, callback);
    }

    // 更新群资料
    //sns/updateGroupInfo
    public void snsUpdateGroupInfo(int groupId, List<Param> body, Callback callback) {
        String method = "sns/updateGroupInfo/" + groupId;
        String bodyName = "groupInfoSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //群组设置(是否允许成员邀请、是否允许匿名聊天、加群验证方式)
    //sns/settingOfGroup
    public void snsSettingOfGroup(int groupId, List<Param> body, Callback callback) {
        String method = "sns/settingOfGroup/" + groupId;
        String bodyName = "settingOfGroupSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

}
