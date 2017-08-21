package com.netease.nim.uikit.cache;

import android.text.TextUtils;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.UIKitLogTag;
import com.netease.nim.uikit.tzw_relative.Group;
import com.netease.nim.uikit.tzw_relative.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yzy on 2017/7/24.
 */

public class TZWTeamCache {

    /**
     * ************************************ 单例 **********************************************
     */

    static class InstanceHolder {
        final static TZWTeamCache instance = new TZWTeamCache();
    }

    public static TZWTeamCache getInstance() {
        return InstanceHolder.instance;
    }

    private Map<String, Group> account2UserMap = new ConcurrentHashMap<>();

    //App启动时调用
    public void init(List<Group> groups) {

        account2UserMap = new ConcurrentHashMap<>();
        addOrUpdateUsers(groups, false);
    }

    public void addOrUpdateUsers(final List<Group> groups, boolean notify) {
        if (groups == null || groups.isEmpty()) {
            return;
        }

        // update cache
        for (Group u : groups) {
            account2UserMap.put(u.netEaseGroupId + "", u);
        }

        // log
        List<String> accounts = getAccounts(groups);
        DataCacheManager.Log(accounts, "on userInfo changed", UIKitLogTag.USER_CACHE);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
            NimUIKit.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }

    private List<String> getAccounts(List<Group> groups) {
        if (groups == null || groups.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<>(groups.size());
        for (Group group : groups) {
            accounts.add(group.netEaseGroupId + "");
        }

        return accounts;
    }

    public Group getTeamByAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return null;
        }

        return account2UserMap.get(account);
    }
}
