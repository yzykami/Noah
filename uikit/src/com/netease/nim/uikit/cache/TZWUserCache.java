package com.netease.nim.uikit.cache;

import android.text.TextUtils;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.UIKitLogTag;
import com.netease.nim.uikit.tzw_relative.User;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yzy on 2017/7/24.
 */

public class TZWUserCache {

    /**
     * ************************************ 单例 **********************************************
     */

    static class InstanceHolder {
        final static TZWUserCache instance = new TZWUserCache();
    }

    public static TZWUserCache getInstance() {
        return InstanceHolder.instance;
    }

    private Map<String, User> account2UserMap = new ConcurrentHashMap<>();

    //App启动时调用
    public void init(List<User> users) {
        account2UserMap = new ConcurrentHashMap<>();
        addOrUpdateUsers(users, false);
    }

    public void addOrUpdateUsers(final List<User> users, boolean notify) {
        if (users == null || users.isEmpty()) {
            return;
        }

        // update cache
        for (User u : users) {
            account2UserMap.put(u.netEaseId + "", u);
        }

        // log
        List<String> accounts = getAccounts(users);
        DataCacheManager.Log(accounts, "on userInfo changed", UIKitLogTag.USER_CACHE);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
            NimUIKit.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }

    private List<String> getAccounts(List<User> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<>(users.size());
        for (User user : users) {
            accounts.add(user.netEaseId + "");
        }

        return accounts;
    }

    public User getFriendByAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return null;
        }

        return account2UserMap.get(account);
    }
}
