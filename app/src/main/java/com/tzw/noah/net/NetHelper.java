package com.tzw.noah.net;

import android.content.Context;

import com.tzw.noah.AppContext;
import com.tzw.noah.utils.DeviceUuidFactory;
import com.tzw.timeselector.Utils.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzy on 2017/6/14.
 */

public class NetHelper {

    public static NetHelper getInstance() {
        return new NetHelper();
    }

    public String ip = "10.0.9.10";

    //version
    public void getAppVersion(Callback callback) {
        int versionCode = 0;
        String url = "http://" + ip + "/download/version.txt";
        HttpTool.getInstance().HttpGet(url, callback);
    }

    //base/time
    public IMsg getBaseTime() {
        String method = "base/time";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/time
    public void getBaseTime(Callback callback) {
        String method = "base/time";
        new WIRequest().Get(method, callback);
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

    //base/config
    public void getBaseConfig(Callback callback) {
        String method = "base/config";
        new WIRequest().Get(method, callback);
    }

    //base/area
    public IMsg getBaseArea() {
        String method = "base/area";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/area
    public void getBaseArea(Callback callback) {
        String method = "base/area";
        new WIRequest().Get(method, callback);
    }

    //base/appcache
    public IMsg getBaseAppCache() {
        String method = "base/appCache";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/appcache
    public void getBaseAppCache(Callback callback) {
        String method = "base/appCache";
        new WIRequest().Get(method, callback);
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

    //base/dictType
    public void getBaseDictType(Callback callback) {
        String method = "base/dictType";
        new WIRequest().Get(method, callback);
    }

    //base/dict
    public IMsg getBaseDict() {
        String method = "base/dict";
        IMsg mr = new WIRequest().Get(method);
        return mr;
    }

    //base/dict
    public void getBaseDict(Callback callback) {
        String method = "base/dict";
        new WIRequest().Get(method, callback);
    }
    //base/SensitiveWordsType
    public void getBaseSensitiveWordsType(Callback callback) {
        String method = "base/SensitiveWordsType";
        new WIRequest().Get(method, callback);
    }

    //base/sensitiveWords
    public void getBaseSensitiveWords(Callback callback) {
        String method = "base/sensitiveWords";
        new WIRequest().Get(method, callback);
    }

    public IMsg setDeviceId(String deviceId) {
        String method = "base/deviceNo";
        List<Param> body = new ArrayList<>();
        body.add(new Param("clientCode", deviceId));
        IMsg imsg = new WIRequest().Post(method, body);
        return imsg;
    }

    public void secretKeyDetails(Callback callback) {
        Context context = AppContext.getContext();
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        String deviceId = deviceUuidFactory.getDeviceUuidString();
        String method = "base/secretKeyDetails";
        List<Param> body = new ArrayList<>();
        body.add(new Param("clientCode", deviceId));
        new WIRequest().Post(method, body, callback);
    }

    //检查设备ID是否已经上传到服务器，已上传返回true，未上传则更新，更新成功返回true，网络或者其他问题返回false
    public IMsg checkDeviceId() {
        Context context = AppContext.getContext();
        IMsg iMsg = new IMsg();
        iMsg.setSucceed(true);
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        if (!deviceUuidFactory.isIsupdated()) {
            iMsg = setDeviceId(deviceUuidFactory.getDeviceUuidStringNoCheck());
            if (iMsg.isSucceed())
                deviceUuidFactory.setUpdate();
            return iMsg;
        }
        return iMsg;
    }

    //发送验证码
    //sms/sendCode
    //1、safety:会员的动态登录；2、register:会员注册；3、forgot:会员找回密码 4、changePhone:更换绑定手机
    public void smsSendCode(List<Param> body, Callback callback) {
        String method = "sms/sendCode";
        String bodyName = "smsCodeSObj";
        new WIRequest().Post(method, body, bodyName, callback);
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

    //验证码登录
    //member/loginBySmsCode
    public IMsg memberLoginBySmsCode(List<Param> body) {
        IMsg iMsg = checkDeviceId();
        if (iMsg.isSucceed()) {
            String method = "member/loginBySmsCode";
            String bodyName = "loginSObj";
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(AppContext.getContext());
            body.add(new Param("clientCode", deviceUuidFactory.getDeviceUuidString()));
            iMsg = new WIRequest().Post(method, body, bodyName);
            return iMsg;
        } else {
            return iMsg;
        }
    }

    //第三方登录
    //member/loginByThird
    public IMsg memberLoginByThird(List<Param> body) {
        IMsg iMsg = checkDeviceId();
        if (iMsg.isSucceed()) {
            String method = "member/loginByThird";
            String bodyName = "loginSObj";
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(AppContext.getContext());
            body.add(new Param("clientCode", deviceUuidFactory.getDeviceUuidString()));
            iMsg = new WIRequest().Post(method, body, bodyName);
            return iMsg;
        } else {
            return iMsg;
        }
    }
    //第三方绑定
    //member/bindThird
    public IMsg memberBindThird(List<Param> body) {
        IMsg iMsg = checkDeviceId();
        if (iMsg.isSucceed()) {
            String method = "member/bindThird";
            String bodyName = "bindThirdSObj";
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(AppContext.getContext());
            body.add(new Param("clientCode", deviceUuidFactory.getDeviceUuidString()));
            iMsg = new WIRequest().Post(method, body, bodyName);
            return iMsg;
        } else {
            return iMsg;
        }
    }

    //注册
    //member/findPwd
    public void memberFindPwd(List<Param> body, Callback callback) {
        String method = "member/findPwd";
        String bodyName = "findPwdSObj";
        new WIRequest().Post(method, body, bodyName, callback);
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

    //扫码成功
    //member/changeCode
    public void memberChangeCode(String qrcode, Callback callback) {
        String method = "member/changeCode";
        String bodyName = "";
        List<Param> body = Param.makeSingleParam("clientCode", qrcode);
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //扫码登录
    //member/loginByVcode
    public void memberLoginByVcode(String qrcode, Callback callback) {
        String method = "member/loginByVcode";
        String bodyName = "";
        List<Param> body = Param.makeSingleParam("clientCode", qrcode);
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //扫码取消登录
    //member/cancelLogin
    public void memberCancelLogin(String qrcode, Callback callback) {
        String method = "member/cancelLogin";
        String bodyName = "";
        List<Param> body = Param.makeSingleParam("clientCode", qrcode);
        new WIRequest().Post(method, body, bodyName, callback);
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
    public void getUserDetails(Callback callback) {
        String method = "user/details";
        new WIRequest().Post(method, null, callback);
    }

    //user/phone
    public void setUserPhone(List<Param> body, Callback callback) {
        String method = "user/phone";
        String bodyName = "phoneSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //user/infoSObj
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

    //user/replaceThePicture
    public void userReplaceThePicture(Map<String, File> fileBody, Callback callback) {
        String method = "user/replaceThePicture";
        new WIRequest().PostFile(method, null, fileBody, "", callback);
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
    //sns/details/netEaseId/
    public void snsDetailsnetEaseId(int id, Callback callback) {
        String method = "sns/details/netEaseId/" + id;
        new WIRequest().Get(method, callback);
    }

    //获取个人资料，私密，需要登
    //sns/details/netEaseId/
    public void snsDetailsnetEaseId2(int id, Callback callback) {
        String method = "sns/details2/netEaseId/" + id;
        new WIRequest().Get(method, callback);
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

    //获取我的附近的人列表
    //sns/nearby
    public void snsNearby2(double lat, double lng, Callback callback) {
        List<Param> body = new ArrayList<>();
        body.add(new Param("lat", lat + ""));
        body.add(new Param("lng", lng + ""));
        String bodyName = "coordinatesSObj";
        String method = "sns/nearby2";
        new WIRequest().Post(method, body, bodyName, callback);
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

    //获取群成员（多人会话、群聊）
    //sns/getMembers
    public void snsGetMembers(int groupId, Callback callback) {
        String method = "sns/getMembers?groupId=" + groupId;
        new WIRequest().Get(method, callback);
    }


    //主动退出群（多人会话、群聊）
    //sns/quit/
    public void snsQuit(int groupId, Callback callback) {
        String method = "sns/quit/" + groupId;
        new WIRequest().Put(method, null, callback);
    }

    //移交群主（多人会话、群聊）
    //sns/transfer/
    public void snsTransfer(int groupId, int memberNo, Callback callback) {
        List<Param> body = Param.makeSingleParam("memberId", memberNo);
        String method = "sns/transfer/" + groupId;
        String bodyName = "changeOwnerSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //获取我的多人会话和群列表
    //sns/groups
    public void snsGroups(Callback callback) {
        String method = "sns/groups";
        new WIRequest().Get(method, callback);
    }

    //获取我的群详情
    //sns/groupDetails
    public void snsGroupDetails(int groupId, Callback callback) {
        String method = "sns/groupDetails/" + groupId;
        new WIRequest().Get(method, callback);
    }

    //获取我的多人会话和群聊信息通知
    //sns/groupNotification
    public void snsGroupNotification(Callback callback) {
        String method = "sns/groupNotification";
        new WIRequest().Get(method, callback);
    }

    //获取个人消息,好友关系变化
    //sns/relationRecords
    public void snsRelationRecords(Callback callback) {
        String method = "sns/relationRecords";
        new WIRequest().Get(method, callback);
    }

    //主动退出群（多人会话、群聊）
    //sns/dismiss/
    public void snsDismiss(int groupId, Callback callback) {
        String method = "sns/dismiss/" + groupId;
        new WIRequest().Put(method, null, callback);
    }

    //获取群聊的类别
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

    // 创建群
    //sns/createGroup
    public void snsCreateGroup2(List<Param> body, Map<String, File> fileBody, Callback callback) {
        String method = "sns/createGroup2";
        String bodyName = "";
        new WIRequest().PostFile(method, body, fileBody, "", callback);
//        new WIRequest().Post(method, body, bodyName, callback);
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

    //群聊:添加管理员
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

    // 申请加群
    //sns/applyToGroup
    public void snsApplyToGroup(int groupId, String msg, Callback callback) {
        String method = "sns/applyToGroup";
        String bodyName = "applyToGroupSObj";
        List<Param> body = new ArrayList<>();
        body.add(new Param("groupId", groupId));
        body.add(new Param("message", msg));
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //审批会员申请入群的请求
    //sns/handleApplyToGroup
    public void snsHandleApplyToGroup(List<Param> body, Callback callback) {
        String method = "sns/handleApplyToGroup";///" + groupId;
        String bodyName = "handleApplyToGroupSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //是否同意邀请入群
    //sns/handleInviteWithGroup
    public void snsHandleInviteWithGroup(List<Param> body, Callback callback) {
        String method = "sns/handleInviteWithGroup";///" + groupId;
        String bodyName = "handleInviteWithGroupSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //群聊设置(是否允许成员邀请、是否允许匿名聊天、加群验证方式)
    //sns/settingOfGroup
    public void snsSettingOfGroup(int groupId, List<Param> body, Callback callback) {
        String method = "sns/settingOfGroup/" + groupId;
        String bodyName = "settingOfGroupSObj";
        new WIRequest().Put(method, body, bodyName, callback);
    }

    //获取我的推荐的人列表
    //sns/recommendGroup
    public void snsRecommendGroup(Callback callback) {
        String method = "sns/recommendGroup";
        new WIRequest().Get(method, callback);
    }

    //更新多人会话和群聊头像
    //sns/uploadIconToGroup
    public void snsUploadIconToGroup(int groupId, Map<String, File> fileBody, Callback callback) {
        String method = "sns/uploadIconToGroup/" + groupId;
        new WIRequest().PostFile(method, null, fileBody, "", callback);
    }

    // 好友搜索
    //sns/searchUser
    public void snsSearchUser(String key, Callback callback) {
        String method = "sns/searchUser";
        List<Param> body = new ArrayList<>();
        body.add(new Param("userInfo", key));
        new WIRequest().Post(method, body, callback);
    }

    // 群聊搜索
    //sns/findGroup
    public void snsFindGroup(String key, Callback callback) {
        String method = "sns/findGroup";
        List<Param> body = new ArrayList<>();
        body.add(new Param("groupInfo", key));
        new WIRequest().Post(method, body, callback);
    }

    // 群聊消息免打扰
    //sns/settingOfMyGroup
    public void snsSettingOfMyGroup(int groupId, int isGet, Callback callback) {
        String method = "sns/settingOfMyGroup/" + groupId;
        List<Param> body = new ArrayList<>();
        body.add(new Param("messageGet", isGet));
        String bodyName = "settingOfMyGroupSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    ///////////////////////////////           MEDIA               ///////////////////
    /////////////////////////////////////////////////////////////////////////////////

    //获取栏目
    //media/category
    public void mediaCategory(Callback callback) {
        String method = "media/category";
        new WIRequest().Get(method, callback);
    }

    //获取栏目
    //media/category
    public IMsg mediaCategory() {
        String method = "media/category";
        return new WIRequest().Get(method);
    }

    //获取文章列表
    //media/articleList/{$channeiId}/{$articleId}
    public void mediaArticleList(int channelId, int articleId, int pagesize, Callback callback) {
        String method = "media/articleList/" + channelId + "/" + articleId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取文章详情
    //media/articleDetails?articleId={$articleId}
    public void mediaArticleDetails(int articleId, Callback callback) {
        String method = "media/articleDetails?articleId=" + articleId;
        new WIRequest().Get(method, callback);
    }

    // 文章点赞
    //media/evaluate
    public void mediaEvaluate(int articleId, int evaluteValue, int articleCommentId, Callback callback) {
        String method = "media/evaluate";
        List<Param> body = new ArrayList<>();
        body.add(new Param("articleId", articleId));
        body.add(new Param("evaluateValue", evaluteValue));
        if (articleCommentId != 0)
            body.add(new Param("articleCommentId", articleCommentId));
        String bodyName = "evaluateSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

//    // 文章收藏
//    //media/saveToFavorite
//    public void mediaFavorite(int articleId, int status, Callback callback) {
//        String method = "media/saveToFavorite";
//        List<Param> body = new ArrayList<>();
//        body.add(new Param("articleId", articleId));
//        body.add(new Param("status", status));
//        String bodyName = "favoriteSObj";
//        new WIRequest().Post(method, body, bodyName, callback);
//    }

    // 文章评论
    //media/commentOnArticle
    public void mediaComment(int articleId, String commentContent, int commentId, Callback callback) {
        String method = "media/commentOnArticle";
        List<Param> body = new ArrayList<>();
        body.add(new Param("articleId", articleId));
        body.add(new Param("commentContent", commentContent));
        if (commentId != 0)
            body.add(new Param("articleCommentPid", commentId));
        String bodyName = "articleCommentSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //获取点赞列表
    //media/evaluateList/{$articleEvaluateId}/{$articleId}/{$pagesize}
    public void mediaLikeList(int articleId, int articleEvaluateId, int pagesize, Callback callback) {
        String method = "media/evaluateList/" + articleId + "/" + articleEvaluateId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取评论列表
    //media/commentList/{$articleId}/{$articleCommntId}/{$pagesize}
    public void mediaCommentList(int articleId, int articleCommntId, int pagesize, Callback callback) {
        String method = "media/commentList/" + articleId + "/" + articleCommntId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取评论列表
    //media/revertList/{$articleId}/{$beArticleCommentId}/{$articleCommntId}/{$pagesize}
    public void mediaCommentList(int articleId, int beArticleCommentId, int articleCommntId, int pagesize, Callback callback) {
        String method = "media/revertList/" + articleId + "/" + beArticleCommentId + "/" + articleCommntId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    // 文章评论
    //media/complain
    public void mediaComplaint(int articleId, String type, String content, Callback callback) {
        String method = "media/complain";
        List<Param> body = new ArrayList<>();
        body.add(new Param("complaintsObjectId", articleId));
        body.add(new Param("complaintsType", type));
        if (!TextUtil.isEmpty(content)) {
            body.add(new Param("complaintsValue", content));
        }
        String bodyName = "complainSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    // 文章搜索
    //media/search
    public void mediaSearch(String keyWords, int articleId, int pagesize, Callback callback) {
        String method = "media/search";
        List<Param> body = new ArrayList<>();
        body.add(new Param("keyWords", keyWords));
        body.add(new Param("articleId", articleId));
        body.add(new Param("pagesize", pagesize));
        String bodyName = "conditionsSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //获取文章列表
    //mix/articleList
    public void mixArticleList(int channelId, int articleId, int pagesize, int advId, int advSize, Callback callback) {
        String method = "mix/articleList";
        HashMap<String, Object> mapSObj = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> mapArticle = new HashMap<>();
        HashMap<String, Object> mapAdv = new HashMap<>();
        mapArticle.put("channelId", channelId);
        mapArticle.put("articleId", articleId);
        mapArticle.put("pagesize", pagesize);

        mapAdv.put(advId + "", advSize);

        map.put("articleListObj", mapArticle);
        map.put("advertisingObj", mapAdv);

        mapSObj.put("articleListSObj", map);

        new WIRequest().PostMap(method, mapSObj, callback);
    }

    //获取文章详情
    //mix/articleList
    public void mixArticleDetail(int articleId, int advId, int advSize, Callback callback) {
        String method = "mix/articleDetails";
        HashMap<String, Object> mapSObj = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
//        HashMap<String, Object> mapArticle = new HashMap<>();
        HashMap<String, Object> mapAdv = new HashMap<>();

        mapAdv.put(advId + "", advSize);

        map.put("articleId", articleId);
//        map.put("articleListObj", mapArticle);
        map.put("advertisingObj", mapAdv);

        mapSObj.put("articleDetailsSObj", map);

        new WIRequest().PostMap(method, mapSObj, callback);
    }


    //获取关键词列表
    //media/articleListByKeyword/{$keywordId}/{$articleId}/{$pagesize}
    public void mediaKeywordList(int keywordId, int articleId, int pagesize, Callback callback) {
        String method = "media/articleListByKeyword/" + keywordId + "/" + articleId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取我的收藏列表
    //media/ collectionList/{$userKeepArticleId }/{$pagesize}
    public void mediaFavoriteList(int articleId, int pagesize, Callback callback) {
        String method = "media/collectionList/" + articleId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取我的收藏列表
    //mix/collectionList/{$infoType}/{$userKeepArticleId }/{$pagesize}
    public void mediaMixFavoriteList(int infoType, int articleId, int pagesize, Callback callback) {
        String method = "mix/collectionList/" + infoType + "/" + articleId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    // 收藏文章
    //mix/saveToFavorite
    public void mediaMixFavorite(int infoType, String articleId, int status, Callback callback) {
        String method = "mix/saveToFavorite";
        List<Param> body = new ArrayList<>();
        body.add(new Param("infoType", infoType));
        body.add(new Param("infoIds", articleId));
        body.add(new Param("status", status));
        String bodyName = "favoriteSObj";
        new WIRequest().Post(method, body, bodyName, callback);
    }

    //获取我的评论列表
    //mix/commentsList/{$infoType}/{$userKeepArticleId }/{$pagesize}
    public void mediaMixCommentsList(int infoType, int memberReadId, int pagesize, Callback callback) {
        String method = "mix/commentsList/" + infoType + "/" + memberReadId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取我的评论列表
    //mix/commentsList/{$infoType}/{$userKeepArticleId }/{$pagesize}
    public void mediaMixRevertList(int infoType, int memberReadId, int pagesize, Callback callback) {
        String method = "mix/revertList/" + infoType + "/" + memberReadId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    //获取我的历史阅读
    //mix/memberReadList/{$infoType}/{$userKeepArticleId }/{$pagesize}
    public void mediaMixMemberReadList(int infoType, int memberReadId, int pagesize, Callback callback) {
        String method = "mix/memberReadList/" + infoType + "/" + memberReadId + "/" + pagesize;
        new WIRequest().Get(method, callback);
    }

    // 删除我的历史阅读
    //mix/deleteHistory
    public void mediaMixDeleteHistory(String ids, Callback callback) {
        String method = "mix/deleteHistory";
        List<Param> body = new ArrayList<>();
        body.add(new Param("readHistoryIds ", ids));
        new WIRequest().Post(method, body, "", callback);
    }
}
