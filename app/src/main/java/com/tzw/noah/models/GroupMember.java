package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/14.
 */

public class GroupMember implements Serializable {

    public int groupId;
    public int memberId;
    public String groupMemberName = "";
    public int netEaseId;
    public int memberNo;
    public String memberNickName = "";
    public String memberIntroduce = "";
    public String memberHeadUrl = "";
    public int memberType = -1;

    public String getMemberName() {
        if (groupMemberName.isEmpty())
            return memberNickName;
        return groupMemberName;
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
