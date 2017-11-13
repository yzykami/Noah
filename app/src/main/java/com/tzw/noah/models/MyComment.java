package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzy on 2017-10-13.
 */

public class MyComment implements Serializable {

    public int webArticleId;
    public int praiseNumber;
    public int repliesNumber;
    public String commentContent = "";
    public String memberNickName = "";
    public int memberNo;
    public int articleCommentId;
    public String memberHeadPic = "";
    public int articleCommentPid;
    public String createTime = "";
    public int isArticleEvaluate;
    public int commentType;
    public int memberCommentId;
    public int memberId;


    public MediaArticle articleDetails;
    public MediaComment parentCommentContent;


    public static List<MyComment> loadList(IMsg iMsg) {
        return iMsg.getModelList("commentsListRObj", new TypeToken<List<MyComment>>() {
        }.getType());
    }
    public static List<MyComment> loadReplyList(IMsg iMsg) {
        return iMsg.getModelList("revertListRObj", new TypeToken<List<MyComment>>() {
        }.getType());
    }
}
