package com.tzw.noah;

import android.app.Application;
import android.content.Context;

import com.tzw.noah.logger.Log;

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
    }

    public static Context getContext()
    {
        return instance;
    }

}
