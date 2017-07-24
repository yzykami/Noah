package com.tzw.noah.datacache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzy on 2017/7/21.
 */

public final class SnsUserCache {

    static String memberNo = "memberNo";
    static String netEaseId = "netEaseId";
    static String memberNickName = "memberNickName";
    static String remarkName = "remarkName";

    static Map<String, Object> map = new HashMap<>();

    //给主app用,添加User信息
    public static void add(Object o) {
        String key = getKey(o);
        map.put(key, o);
    }

    private static String getKey(Object o) {
        return ReflectUtils.getStrng(o, netEaseId);
    }

    public static int getMemberNo(String account) {
        if (map.containsKey(account)) {
            Object o = map.get(account);
            return ReflectUtils.getInt(o, memberNo);
        } else return 0;
    }

    public static String getDisplayName(String account,String name) {
        if (map.containsKey(account)) {
            Object o = map.get(account);
            String nickname = ReflectUtils.getStrng(o, memberNickName);
            String ramarkname = ReflectUtils.getStrng(o, remarkName);
            if (remarkName.isEmpty())
                return nickname;
            return ramarkname;
        }
        return name;
    }
}
