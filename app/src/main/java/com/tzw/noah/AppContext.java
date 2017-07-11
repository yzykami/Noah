package com.tzw.noah;

import android.app.Application;
import android.content.Context;

import com.tzw.noah.cache.AppCache;
import com.tzw.noah.db.SnsDBHelper;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.NetWorkUtils;

/**
 * Created by yzy on 2017/6/15.
 */

public class AppContext extends Application {

    public static Context instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = getApplicationContext();
        Log.init();
        AppCache.firstInstall();
        new DBInit().snsInit();
    }

    public static Context getContext()
    {
        return instance;
    }

}
