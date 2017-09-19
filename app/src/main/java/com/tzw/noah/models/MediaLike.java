package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class MediaLike implements Serializable{
    public int memberNo;
    public int articleEvaluateId;
    public String memberHeadPic="";
    public int clientType;
    public String createTime="";
    public int clientIp;
    public int webArticleId;
    public String memberNickName="";
    public int evaluateValue;


    public static List<MediaLike> loadList(IMsg iMsg) {
        return iMsg.getModelList("evaluateListRObj", new TypeToken<List<MediaLike>>() {
        }.getType());
    }
}