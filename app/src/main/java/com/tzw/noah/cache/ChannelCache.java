package com.tzw.noah.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tzw.noah.AppContext;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.net.WIRequest;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.home.ArticleListFragment;
import com.tzw.noah.utils.FileUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/17.
 */

public class ChannelCache {

    public static User user;

    static List<MediaCategory> channleList;

//    protected static final String DIR = "Media";
    protected static final String FileName = "Channel";


    static boolean isCompleted = false;
    private static int maxCount = 8;

    public interface ChannelListGetter {
        void onRead(List<MediaCategory> list);
    }

    public static List<MediaCategory> getChannels(Context mContext, final ChannelListGetter callback) {
        if (channleList != null) {
            if (callback != null)
                callback.onRead(channleList);
            return channleList;
        }
        channleList = new ArrayList<>();
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

                    channleList = gson.fromJson(el, new TypeToken<List<MediaCategory>>() {
                    }.getType());
                } catch (Exception e) {
                    Log.log("ChannelCache", e);
                }
            }
        } catch (Exception e) {
            Log.log("ChannelCache", e);
        }

        //如果本地获取不到, 向服务器获取
        if (channleList == null || channleList.size() == 0) {
            channleList = new ArrayList<>();
            NetHelper.getInstance().mediaCategory(new StringDialogCallback(mContext) {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null)
                        callback.onRead(channleList);
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            MediaCategory mediaCategory = MediaCategory.load(iMsg);
                            for (int i = 0; i < mediaCategory.children.size() && i < maxCount; i++) {
                                channleList.add(mediaCategory.children.get(i));
                            }
                            setChannels(channleList);
                        } else {
                        }
                    } catch (Exception e) {
                        Log.log("ChannelCache", e);
                    } finally {
                        if (callback != null)
                            callback.onRead(channleList);
                    }
                }
            });
        } else {
            if (callback != null)
                callback.onRead(channleList);
        }
        return channleList;
    }

    public static void setChannels(List<MediaCategory> list) {
        channleList = list;
        try {
            Gson gson = new GsonBuilder().create();
            String content = gson.toJson(list);
            String file = FileName + "_" + UserCache.getUser().memberNo;
            FileUtil.saveInternalFile(file, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
