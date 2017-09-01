package com.netease.nim.uikit.tzw_relative;

import android.content.Context;

import com.netease.nim.uikit.cache.SimpleCallback;

/**
 * Created by yzy on 2017/7/21.
 */

public interface GobalObserver {
    void onShowUser(Context context, String account, int memberNo);

    void onShowTeam(Context context, String account, int memberNo, Group group);

    void onItemAddClick(Context context);

    void onUnreadNumChanged(Object o);

    void onKickOut(Context context,String client);
//
//    void onGetTeamInfo(SimpleCallback callback);
}
