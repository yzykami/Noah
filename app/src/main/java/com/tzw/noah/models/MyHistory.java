package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzy on 2017-10-13.
 */

public class MyHistory implements Serializable {
    public int memberReadId;
    public MediaArticle articleDetails;

    public static List<MyHistory> loadList(IMsg iMsg) {
        return iMsg.getModelList("readListRObj", new TypeToken<List<MyHistory>>() {
        }.getType());
    }
}
