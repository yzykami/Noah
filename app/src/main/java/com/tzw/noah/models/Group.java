package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.db.MyField;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzy on 2017/7/13.
 */

public class Group implements Serializable {

    @MyField(name = "id")
    public int groupId;
    @MyField
    public int netEaseGroupId;
    @MyField
    public String groupName = "";
    @MyField
    public String groupIntroduction = "";
    @MyField
    public String groupBulletin = "";
    @MyField
    public String groupHeader = "";
    @MyField
    public int groupTypeId;
    @MyField
    public int groupAttribute;
    @MyField
    public int ifInvited;
    @MyField
    public int ifAnonymous;
    @MyField
    public int ifEnabled;
    @MyField
    public int reviewState;
    @MyField
    public String reviewRemark = "";
    @MyField
    public int auditorId;
    @MyField
    public String reviewTime = "";
    @MyField
    public String createTime = "";
    @MyField
    public int joinmode;
    @MyField
    public int ownerNo;

    public int memberType;
    public int memberCount;

    public String tag = "";

    public static List<Group> loadDiscussList(IMsg iMsg) {
        return iMsg.getModelList("discussRObj", new TypeToken<List<Group>>() {
        }.getType());
    }

    public static List<Group> loadGroupList(IMsg iMsg) {
        return iMsg.getModelList("groupRObj", new TypeToken<List<Group>>() {
        }.getType());
    }

    public static class Type {
        public static int GROUP = 1092;
        public static int DISCUSS = 1091;
    }
}
