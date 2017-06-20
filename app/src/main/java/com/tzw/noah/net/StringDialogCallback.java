package com.tzw.noah.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import android.os.Handler;

/*
*Created by yzy on 2017/6/16.
 */
public abstract class StringDialogCallback extends Callback {

    private ProgressDialog dialog;
    boolean isFinished = false;

    public StringDialogCallback(Activity activity) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }
    public StringDialogCallback(Context context) {
        dialog = new ProgressDialog(context);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinished && dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
            }
        },200);
    }

    @Override
    public void onAfter(){
        //网络请求结束后关闭对话框
        isFinished=true;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
