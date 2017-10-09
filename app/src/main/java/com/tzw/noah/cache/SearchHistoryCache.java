package com.tzw.noah.cache;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/17.
 */

public class SearchHistoryCache {

    public static User user;

    static List<String> list;

    //    protected static final String DIR = "Media";
    protected static final String FileName = "SearchHistory";


    static boolean isCompleted = false;
    private static int maxCount = 20;

//    public interface SearchHistoryListGetter {
//        void onRead(List<MediaCategory> list);
//    }

    public static List<String> getSearchHistorys(Context mContext) {
        if (list != null) {
            return list;
        }
        list = new ArrayList<>();
        try {
            String file = FileName + "_" + UserCache.getUser().memberNo;

            String content = FileUtil.readInternalFile(file);
            if (TextUtils.isEmpty(content)) {

            } else {
                Gson gson = new GsonBuilder().create();
                try {
                    //创建一个JsonParser
                    JsonParser parser = new JsonParser();
                    //通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
                    JsonElement el = parser.parse(content);

                    list = gson.fromJson(el, new TypeToken<List<String>>() {
                    }.getType());
                } catch (Exception e) {
                    Log.log("SearchHistoryCache", e);
                }
            }
        } catch (Exception e) {
            Log.log("SearchHistoryCache", e);
        }
        return list;
    }

    public static void setSearchHistorys(List<String> list) {
        SearchHistoryCache.list = new ArrayList<>();
        for (int i = 0; i < list.size() && i < maxCount; i++) {
            SearchHistoryCache.list.add(list.get(i));
        }
        try {
            Gson gson = new GsonBuilder().create();
            String content = gson.toJson(SearchHistoryCache.list);
            String file = FileName + "_" + UserCache.getUser().memberNo;
            FileUtil.saveInternalFile(file, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
