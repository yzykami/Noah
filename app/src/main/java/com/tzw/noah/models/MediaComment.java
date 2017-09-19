package com.tzw.noah.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class MediaComment implements Serializable{
    public int memberNo;
    public int articleCommentId;
    public int beArticleCommentId;
    public String memberHeadPic = "";
    public int clientType;
    public int commentMemberId;
    public String createTime = "";
    public int clientIp;
    public int beCommentMemberId;
    public int webArticleId;
    public String commentContent = "";
    public String memberNickName = "";
    private JsonElement children;
//    private List<MediaComment> children=new ArrayList<>();
    public int repliesNumber;

    public boolean isCommentDetail=false;


    public List<MediaComment> sonList() {
        if (children != null && children instanceof JsonArray) {
            Gson gson = new GsonBuilder().create();
            try {
                return gson.fromJson(children, new TypeToken<List<MediaComment>>() {
                }.getType());
            } catch (Exception e) {
                Log.log("MediaComment", e);
            }
        }
        return new ArrayList<>();
    }

    public static MediaComment load(IMsg iMsg) {
        return iMsg.getModel("articleCommentRObj", new TypeToken<MediaComment>() {
        }.getType());
    }

    public static List<MediaComment> loadList(IMsg iMsg) {
        return iMsg.getModelList("commentListRObj", new TypeToken<List<MediaComment>>() {
        }.getType());
    }

    //评论的评论
    public static List<MediaComment> loadList2(IMsg iMsg) {
        return iMsg.getModelList("reverListRObj", new TypeToken<List<MediaComment>>() {
        }.getType());
    }
}