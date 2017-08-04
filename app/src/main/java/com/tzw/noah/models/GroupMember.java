package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.db.MyField;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/14.
 */

public class GroupMember implements Serializable {

    @MyField
    public int groupId;
//    @MyField
//    public int memberId;
    @MyField
    public String groupMemberName = "";
    @MyField
    public int netEaseId;
    @MyField
    public int memberNo;
    @MyField
    public String memberNickName = "";
    public String memberIntroduce = "";
    @MyField
    public String memberHeadPic = "";
    @MyField
    public int memberType = -1;

    public String getMemberName() {
        if (groupMemberName.isEmpty())
            return memberNickName;
        return groupMemberName;
    }

    public static List<User> createUserList(List<GroupMember> list) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            User u = new User();
            GroupMember gm = list.get(i);
            u.memberNo = gm.memberNo;
//            u.memberId = gm.memberId;
            u.netEaseId = gm.netEaseId;
            u.memberNickName = gm.memberNickName;
            u.memberIntroduce = gm.memberIntroduce;
            u.memberHeadPic = gm.memberHeadPic;
            userList.add(u);
        }
        return userList;
    }

    public static List<GroupMember> loadList(IMsg iMsg) {
        List<GroupMember> list = new ArrayList<>();
        Utils.listAdd(list, loadOwner(iMsg));
        Utils.listAdd(list, loadManager(iMsg));
        Utils.listAdd(list, loadMemberRObj(iMsg));
        return list;
    }

    public static List<GroupMember> loadOwner(IMsg iMsg) {
        IMsg iMsg2 = iMsg.getJsonObject("memberListRObj");
        return iMsg2.getModelList("ownerRObj", new TypeToken<List<GroupMember>>() {
        }.getType());
    }

    public static List<GroupMember> loadManager(IMsg iMsg) {
        IMsg iMsg2 = iMsg.getJsonObject("memberListRObj");
        return iMsg2.getModelList("managerRObj", new TypeToken<List<GroupMember>>() {
        }.getType());
    }

    public static List<GroupMember> loadMemberRObj(IMsg iMsg) {
        IMsg iMsg2 = iMsg.getJsonObject("memberListRObj");
        return iMsg2.getModelList("memberRObj", new TypeToken<List<GroupMember>>() {
        }.getType());
    }

}
