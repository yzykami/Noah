package com.tzw.noah;

import android.content.Context;

import com.netease.nim.demo.NimApplication;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.TZWUserCache;
import com.netease.nim.uikit.tzw_relative.GobalObserver;
import com.tzw.noah.cache.AppCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.logger.Log;

/**
 * Created by yzy on 2017/6/15.
 */

public class AppContext extends NimApplication {//Application {

    public static Context instance;

    public static GobalObserver go;


    @Override
    public void onCreate() {
        super.onCreate();
        go = new GobalObserverImpl();
        NimUIKit.setGobalObserver(go);
        instance = getApplicationContext();
        TZWUserCache.getInstance().init(new SnsDBManager(instance).getSnsAllUserList());
        Log.init();
        AppCache.firstInstall();
        new DBInit().snsInit();
    }


    public static Context getContext() {
        return instance;
    }

}
