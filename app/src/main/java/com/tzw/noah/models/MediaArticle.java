package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.util.List;

/**
 * Created by yzy on 2017/8/30.
 */

public class MediaArticle {

    public int articleId;
    public String webClassIds = "";
    public String articleTitle = "";
    public int articleType;
    public String webArticleImage = "";
    public String appArticleImage = "";
    public String h5ArticleImage = "";
    public String articleAbstract = "";
    public String keyWordIds = "";
    public String articleAuthor = "";
    public int ifOriginal;
    public String articleSource = "";
    public int ifAttachment;
    public int ifVideo;
    public int ifWatermark;
    public int ifHot;
    public int ifRecommend;
    public int ifTop;
    public int readNumber;
    public int praiseNumber;
    public int keepNumber;
    public int articleState;
    public int editorType;
    public int editorId;
    public String createTime = "";

    public static List<MediaArticle> load(IMsg iMsg) {
        return iMsg.getModelList("articleListRObj", new TypeToken<List<MediaArticle>>() {
        }.getType());
    }


}
