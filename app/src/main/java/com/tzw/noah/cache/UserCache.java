package com.tzw.noah.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.AppContext;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.WIRequest;

import java.lang.reflect.Field;

/**
 * Created by yzy on 2017/6/17.
 */

public class UserCache {

    private static String token = "";
    private static String loginKey = "";
    private static long timeOffset = -12345678l;
    public static User user;

    protected static final String PREFS_FILE = "usercache.xml";
    protected static final String PREFS_TOKEN = "token";
    protected static final String PREFS_LOGINKEY = "loginkey";
    protected static final String PREFS_TIMEOFFSET = "timeoffset";

    public static User getUser() {
        if (user != null)
            return user;
        user = new User();
        try {
            Class c = Class.forName("com.tzw.noah.models.User");
            Field[] fields = c.getDeclaredFields();
            Context context = AppContext.getContext();
            final SharedPreferences prefs = context
                    .getSharedPreferences(PREFS_FILE, 0);
            for (Field field : fields
                    ) {
                if (field.getType().equals(new TypeToken<String>() {
                }.getType())) {
                    field.set(user, prefs.getString(field.getName(), ""));
                }
                if (field.getType().equals(int.class)) {
                    String s = prefs.getString(field.getName(), "0");
                    int num = 0;
                    try {
                        num = Integer.parseInt(s);
                    } catch (Exception e1) {

                    }
                    field.setInt(user, num);
                }
                if (field.getType().equals(double.class)) {
                    String s = prefs.getString(field.getName(), "0");
                    double d = 0;
                    try {
                        d = Double.parseDouble(s);
                    } catch (Exception e2) {

                    }
                    field.setDouble(user, d);
                }
            }

        } catch (Exception e) {
            Log.log("UserCache", e);
        }
        return user;
    }

    public static void setUser(User user) {
        UserCache.user = user;
        try {
            Class c = Class.forName("com.tzw.noah.models.User");
            Field[] fields = c.getDeclaredFields();
            Context context = AppContext.getContext();
            final SharedPreferences prefs = context
                    .getSharedPreferences(PREFS_FILE, 0);

            for (Field field : fields
                    ) {
                if (field.get(user) != null)
                    prefs.edit().putString(field.getName(), field.get(user).toString()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getToken() {
        if (token.isEmpty()) {
            Context context = AppContext.getContext();
            final SharedPreferences prefs = context
                    .getSharedPreferences(PREFS_FILE, 0);
            token = prefs.getString(PREFS_TOKEN, "");
        }
        return token;
    }

    public static void setToken(String token) {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);
        UserCache.token = token;
        prefs.edit().putString(PREFS_TOKEN, token).commit();
    }


    public static String getLoginKey() {
        if (loginKey.isEmpty()) {
            Context context = AppContext.getContext();
            final SharedPreferences prefs = context
                    .getSharedPreferences(PREFS_FILE, 0);
            loginKey = prefs.getString(PREFS_LOGINKEY, "");
        }
        return loginKey;
    }

    public static void setLoginkey(String loginKey) {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);
        UserCache.loginKey = loginKey;
        WIRequest.LoginKey = loginKey;
        prefs.edit().putString(PREFS_LOGINKEY, loginKey).commit();
    }

    public static long getTimeOffset() {
        if (timeOffset == -12345678l) {
            Context context = AppContext.getContext();
            final SharedPreferences prefs = context
                    .getSharedPreferences(PREFS_FILE, 0);
            timeOffset = prefs.getLong(PREFS_TIMEOFFSET, 0);
        }
        return timeOffset;
    }

    public static void setTimeOffset(long TimeOffset) {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);
        UserCache.timeOffset = TimeOffset;
        WIRequest.TimeOffset = TimeOffset;
        prefs.edit().putLong(PREFS_TIMEOFFSET, TimeOffset).commit();
    }

    public static boolean isLogin() {
        token = getToken();
        if (token.isEmpty())
            return false;
        return true;
    }
}
