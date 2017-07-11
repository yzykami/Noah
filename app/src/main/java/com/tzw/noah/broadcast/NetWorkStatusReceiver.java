package com.tzw.noah.broadcast;

/**
 * Created by yzy on 2017/7/10.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.NetWorkUtils;


public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        com.tzw.noah.logger.Log.log("NetWorkStatusReceiver", "received = " + NetWorkUtils.getAPNType(context));
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Toast.makeText(context, "network changed", Toast.LENGTH_LONG).show();
            MyBaseActivity.isNetWorkConnected = NetWorkUtils.getAPNType(context) > 0;
        }
    }
}
