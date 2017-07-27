package com.netease.nim.demo;

import android.content.Context;

import com.netease.nim.uikit.tzw_relative.GobalObserver;

/**
 * Created by yzy on 2017/7/25.
 */

public final class NimDemo {

    private static Context context;

    //监听点击,回调让主App打开对应界面
    private static GobalObserver gobalObserver;

    public static void init(Context context) {
        NimDemo.context = context;
    }

    public static void setGobalObserver(GobalObserver go) {
        gobalObserver = go;
    }

    public static void onItemAddClick(Context context) {
        if (gobalObserver != null) {
            gobalObserver.onItemAddClick(context);
        }
    }
}
