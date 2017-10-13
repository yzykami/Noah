package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzy on 2017-10-13.
 */

public class Favorite implements Serializable {
    public int userKeepArticleId;
    public MediaArticle articleDetails;

    public static List<Favorite> loadList(IMsg iMsg) {
        return iMsg.getModelList("collectionListRObj", new TypeToken<List<Favorite>>() {
        }.getType());
    }
}
