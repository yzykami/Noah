package com.tzw.noah.models;

import com.tzw.noah.db.MyField;

import java.util.List;

/**
 * Created by yzy on 2017/6/30.
 */

public class SnsPerson {

    @MyField(name = "shortcut")
    public String shortCut = "";
    @MyField(name = "name")
    public String name = "";
    @MyField(name = "namePingyin")
    public List<Character> namePingyin;
    @MyField(name = "sign")
    public String sign = "";
    @MyField(name = "headUrl")
    public String headUrl = "";
    @MyField(name = "lastChatTime")
    public String lastChatTime = "";
    @MyField(name = "isBlock")
    public boolean isBlock = false;
    @MyField(name = "unReadNum")
    public int unReadNum = 0;
    @MyField(name = "type")
    public int type = 0;

    @MyField(name = "memberId")
    public String memberId = "";

    @MyField(name = "memberNo")
    public String memberNo = "";
    @MyField(name = "memberMobile")
    public String memberMobile = "";
    @MyField(name = "memberNickName")
    public String memberNickName = "";
    @MyField(name = "memberPasswd")
    public String memberPasswd = "";
    @MyField(name = "memberHeadPic")
    public String memberHeadPic = "";
    @MyField(name = "memberSex")
    public int memberSex = 0;
    @MyField(name = "memberLevel")
    public int memberLevel = 0;
    @MyField(name = "memberInterest")
    public String memberInterest = "";
    @MyField(name = "memberCharacter")
    public String memberCharacter = "";
    @MyField(name = "memberWork")
    public String memberWork = "";
    @MyField(name = "areaId")
    public int areaId = 0;
    @MyField(name = "memberIntroduce")
    public String memberIntroduce = "";
    @MyField(name = "memberBirthday")
    public String  memberBirthday= "";
    @MyField(name = "growth")
    public String  growth= "";@MyField(name = "totalScore")
    public String  totalScore= "";
    @MyField(name = "score")
    public String  score= "";
    @MyField(name = "totalBonus")
    public String  totalBonus= "";
    @MyField(name = "bonus")
    public String  bonus= "";
    @MyField(name = "netEaseId")
    public String  netEaseId= "";
    @MyField(name = "netEaseToken")
    public String  netEaseToken= "";
    @MyField(name = "createTime")
    public String  createTime= "";



    public String haha = "";

    public static class Type {
        public static int Person = 0;
        public static int Group = 1;
    }
}
