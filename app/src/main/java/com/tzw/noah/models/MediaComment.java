package com.tzw.noah.models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tzw.noah.AppContext;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class MediaComment implements Serializable {
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
    private Object children;
    //    private List<MediaComment> children=new ArrayList<>();
    public int repliesNumber;
    public int isArticleEvaluate;
    public int praiseNumber;

    public boolean isCommentDetail = false;
    public boolean isTopCommentDetail = false;


    public List<MediaComment> sonList() {
        if (children != null && children instanceof ArrayList) {
            Gson gson = new GsonBuilder().create();
            try {
                //创建一个JsonParser
                JsonParser parser = new JsonParser();
                //通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
                JsonElement el = parser.parse(gson.toJson(children));
                return gson.fromJson(el, new TypeToken<List<MediaComment>>() {
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

    public static MediaComment Clone(MediaComment obj) {
        MediaComment clone = new MediaComment();
        try {
            Class c = Class.forName("com.tzw.noah.models.MediaComment");
            Field[] fields = c.getDeclaredFields();
            Context context = AppContext.getContext();
            for (Field field : fields) {
                if (field.get(obj) != null)
                    field.set(clone, field.get(obj));
            }
        } catch (Exception e) {

        }
        return clone;
    }
}