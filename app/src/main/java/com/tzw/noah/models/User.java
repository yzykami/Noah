package com.tzw.noah.models;

import com.tzw.noah.db.MyField;
import com.tzw.noah.net.IMsg;

/**
 * Created by yzy on 2017/6/17.
 */

public class User {

    @MyField
    public int memberId;
    @MyField(name = "id")
    public int memberNo;
    @MyField
    public String memberMobile = "";
    @MyField
    public String memberNickName = "";
    @MyField
    public String memberPasswd = "";
    @MyField
    public String memberHeadPic = "";
    @MyField
    public int memberSex;//(0男、1女)
    @MyField
    public int memberLevel;
    @MyField
    public String memberInterest = "";
    @MyField
    public String memberCharacter = "";
    @MyField
    public String memberWork = "";
    @MyField
    public int areaId;
    @MyField
    public String memberIntroduce = "";
    @MyField
    public String memberBirthday = "";
    @MyField
    public String weChatAuthAccount = "";
    @MyField
    public String qqAuthAccount = "";
    @MyField
    public String weiboAuthAccount = "";
    @MyField
    public int growth;
    @MyField
    public int totalScore;
    @MyField
    public int score;
    @MyField
    public double totalBonus;
    @MyField
    public double bonus;
    @MyField
    public int netEaseId;
    @MyField
    public String netEaseToken = "";
    @MyField
    public int ifEnabled;//(0启用、1禁用)
    @MyField
    public String passwdUpdateTime = "";
    @MyField
    public String updateTime = "";
    @MyField
    public String createTime = "";

    public static User load(IMsg iMsg) {
        return iMsg.getModel("detailsRObj", new User());
    }


    public int type = 0;

    public static class Type {
        public static int Person = 0;
        public static int Group = 1;
    }
}
