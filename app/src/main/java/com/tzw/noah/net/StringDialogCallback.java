package com.tzw.noah.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.view.Window;

import android.os.Handler;

/*
*Created by yzy on 2017/6/16.
 */
public abstract class StringDialogCallback extends Callback {

    private ProgressDialog dialog;
    boolean isFinished = false;
    int delay = 200;

    public StringDialogCallback(Activity activity) {
        initDialog(activity);
        this.delay = delay;
    }

    public StringDialogCallback(Context context) {
        initDialog(context);
        this.delay = delay;
    }

    public StringDialogCallback(Activity activity, int delay) {
        initDialog(activity);
        this.delay = delay;
    }

    public StringDialogCallback(Context context, int delay) {
        initDialog(context);
        this.delay = delay;
    }

    void initDialog(Object o) {
        dialog = null;
        if (o instanceof Context) {
            dialog = new ProgressDialog((Context) o);
        } else if (o instanceof Activity) {
            dialog = new ProgressDialog((Activity) o);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }
//    @Override
//    public String parseNetworkResponse(Response response) throws Exception {
//        return response.body().string();
//    }

    @Override
    public void onBefore() {
        //网络请求前显示对话框
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinished && dialog != null && !dialog.isShowing()) {
                    try {
                        dialog.show();
                    } catch (Exception e) {

                    }
                }
            }
        }, delay);
    }

    @Override
    public void onAfter() {
        //网络请求结束后关闭对话框
        isFinished = true;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
