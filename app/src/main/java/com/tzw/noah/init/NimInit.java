package com.tzw.noah.init;

import android.content.Context;

import com.netease.nim.uikit.cache.TZWTeamCache;
import com.netease.nim.uikit.cache.TZWUserCache;
import com.netease.nim.uikit.tzw_relative.Group;
import com.netease.nim.uikit.tzw_relative.User;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.sdk.SnsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/8/21.
 */

public class NimInit {
    public static void init(final Context context) {
        List<User> listUser = new SnsDBManager(context).getSnsAllUserList();
        TZWUserCache.getInstance().init(listUser);
        List<Group> listGroup = new SnsDBManager(context).getSnsAllGroupList();
        TZWTeamCache.getInstance().init(listGroup);
        if (listUser == null || listUser.size() == 0) {
            new SnsManager(context).snsMyList(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(IMsg iMsg) {
                    List<User> listUser2 = new SnsDBManager(context).getSnsAllUserList();
                    TZWUserCache.getInstance().init(listUser2);
                }
            });
        }
        if (listGroup == null || listGroup.size() == 0) {
            new SnsManager(context).snsGroups(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(IMsg iMsg) {
                    List<Group> listGroup2 = new SnsDBManager(context).getSnsAllGroupList();
                    TZWTeamCache.getInstance().init(listGroup2);
                }
            });
        }
    }

    public static void updateUsers(Context context) {
        List<User> listUser = new SnsDBManager(context).getSnsAllUserList();
        TZWUserCache.getInstance().init(listUser);
    }

    public static void updateUser(com.tzw.noah.models.User user) {
        List<User> listUser = new ArrayList<>();
        listUser.add(com.tzw.noah.models.User.CopyToCacheUser(user));
        TZWUserCache.getInstance().addOrUpdateUsers(listUser, true);
    }

    public static void updateGroups(Context context) {
        List<Group> listGroup = new SnsDBManager(context).getSnsAllGroupList();
        TZWTeamCache.getInstance().init(listGroup);
    }

    public static void updateGroup(com.tzw.noah.models.Group group) {
        List<Group> listGroup = new ArrayList<>();

    }
}
