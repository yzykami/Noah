package com.tzw.noah;

import android.content.Context;

import com.netease.nim.demo.NimApplication;
import com.netease.nim.demo.NimDemo;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.TZWTeamCache;
import com.netease.nim.uikit.cache.TZWUserCache;
import com.netease.nim.uikit.tzw_relative.GobalObserver;
import com.tzw.noah.cache.AppCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.logger.Log;

import java.util.logging.Handler;

/**
 * Created by yzy on 2017/6/15.
 */

public class AppContext extends NimApplication {//Application {

    public static Context instance;

    public static GobalObserver go;
//    private Runnable backgroudWorking;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                go = new GobalObserverImpl();
                NimUIKit.setGobalObserver(go);
                NimDemo.setGobalObserver(go);
                instance = getApplicationContext();
                Log.init();
                AppCache.firstInstall();
                new DBInit().snsInit();
                TZWUserCache.getInstance().init(new SnsDBManager(instance).getSnsAllUserList());
                TZWTeamCache.getInstance().init(new SnsDBManager(instance).getSnsAllGroupList());
            }
        }).start();
    }

    public static Context getContext() {
        return instance;
    }


}
