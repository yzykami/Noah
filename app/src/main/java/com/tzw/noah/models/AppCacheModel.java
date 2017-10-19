package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.db.MyField;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzy on 2017-10-13.
 */

public class AppCacheModel implements Serializable {

    @MyField
    public String appCacheId = "";
    @MyField
    public int appCacheVersion;
    @MyField
    public String appCacheRemark = "";
    @MyField
    public int appCacheSort;
    @MyField
    public String updateTime = "";


    public static List<AppCacheModel> loadList(IMsg iMsg) {
        return iMsg.getModelList("appCacheRObj", new TypeToken<List<AppCacheModel>>() {
        }.getType());
    }
}
