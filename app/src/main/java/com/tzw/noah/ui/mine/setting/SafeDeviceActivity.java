package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Device;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class SafeDeviceActivity extends MyBaseActivity {

    String TAG = "SafeDeviceActivity";
    SafeDeviceActivity mycontext = SafeDeviceActivity.this;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_safe_device);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        ll = (LinearLayout) findViewById(R.id.ll);
    }

    private void initview() {
        List<Param> body = new ArrayList<>();
        body.add(new Param("TokenCode", UserCache.getToken()));
        NetHelper.getInstance().getUserDevice(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.log(TAG, e);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<Device> list = Device.load(iMsg);
                        for (Device d : list) {
                            ll.addView(getItemView(d));
                        }

                    } catch (Exception ex) {
                        Log.log(TAG, ex);
                    }
                } else {
                    Log.log(TAG, iMsg.getMsg());
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    View getItemView(Device device) {
        View v = LayoutInflater.from(mycontext).inflate(R.layout.mine_settting_safe_device_item, null);
        TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_name.setText(device.tokenCode + "" + device.clientCode);
        tv_time.setText(device.getLastActiveTime());
        return v;
    }

    private void doWorking() {

    }
}
