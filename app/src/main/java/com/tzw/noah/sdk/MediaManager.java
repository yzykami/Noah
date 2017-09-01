package com.tzw.noah.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.net.IMsg;

/**
 * Created by yzy on 2017/8/30.
 */

public class MediaManager {
    Context mContext;
    SnsDBManager snsDBManager;

    private static Handler mdelivery;

    public MediaManager(Context mContext) {
        this.mContext = mContext;
        snsDBManager = new SnsDBManager(mContext);
        mdelivery = new Handler(Looper.getMainLooper());
    }

    private IMsg createImsg() {
        IMsg imsg = new IMsg();
        imsg.setSucceed(true);
        return imsg;
    }


}
