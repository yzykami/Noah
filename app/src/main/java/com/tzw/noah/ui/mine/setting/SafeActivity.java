package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class SafeActivity extends MyBaseActivity {

    String TAG = "SafeActivity";
    SafeActivity mycontext = SafeActivity.this;
    private TextView tv_mobile;
    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_safe);
        findview();
        initview();
        doWorking();
    }

    private void findview() {

        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_username = (TextView) findViewById(R.id.tv_username);
    }

    private void initview() {
        tv_mobile.setText(UserCache.getUser().memberMobile);
        tv_username.setText(UserCache.getUser().memberNo + "");
    }

    private void doWorking() {

    }


    public void handle_pwd(View view) {
        startActivity(SafePwdActivity.class);
    }

    public void handle_device(View view) {
        startActivity(SafeDeviceActivity.class);
    }
}
