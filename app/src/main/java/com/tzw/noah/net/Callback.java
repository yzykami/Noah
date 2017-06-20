package com.tzw.noah.net;

import com.tzw.noah.net.IMsg;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/12.
 */


public abstract class Callback {

    /**
     * 请求网络开始前，UI线程
     */
    public void onBefore() {
    }

    public void onAfter() {
    }

    public abstract void onFailure(Call call, IOException e) ;

    public abstract void onResponse(IMsg iMsg);

}
