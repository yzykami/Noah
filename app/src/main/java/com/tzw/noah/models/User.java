package com.tzw.noah.models;

import com.tzw.noah.net.IMsg;

/**
 * Created by yzy on 2017/6/17.
 */

public class User {

    public int memberId;
    public int memberNo;
    public String memberMobile="";
    public String memberNickName="";
    public String memberPasswd="";
    public String memberHeadPic="";
    public int memberSex;//(0男、1女)
    public int memberLevel;
    public String memberInterest="";
    public String memberCharacter="";
    public String memberWork="";
    public int areaId;
    public String memberIntroduce="";
    public String memberBirthday="";
    public String weChatAuthAccount="";
    public String qqAuthAccount="";
    public String weiboAuthAccount="";
    public int growth;
    public int totalScore;
    public int score;
    public double totalBonus;
    public double bonus;
    public int ifEnabled;//(0启用、1禁用)
    public String passwdUpdateTime="";
    public String updateTime="";
    public String createTime="";

    public static User load(IMsg iMsg)
    {
        return iMsg.getModel("detailsRObj",new User());
    }
}
