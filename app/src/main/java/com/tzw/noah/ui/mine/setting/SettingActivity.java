package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.AboutActivity;
import com.tzw.noah.ui.mine.DebugActivity;
import com.tzw.noah.ui.mine.LoginActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class SettingActivity extends MyBaseActivity {


    String TAG = "SettingActivity";
    SettingActivity mycontext = SettingActivity.this;

    public final int callBy_personalsetting = 10001;
    public final int callBy_devicesafe = 10002;
    public final int callBy_commonsetting = 10003;
    public final int callBy_about = 10004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countShowDebug();
            }
        });
    }

    private static int showCount = 0;
    private static long currentShowPressedTime = 0;

    private void countShowDebug() {
        if (!UserCache.isLogin())
            return;
        if (UserCache.getUser().ifDebug == 0)
            return;

        if (System.currentTimeMillis() - currentShowPressedTime > 300)
            showCount = 1;
        currentShowPressedTime = System.currentTimeMillis();
        showCount++;
        if (showCount >= 6) {
            RelativeLayout rl_debug = (RelativeLayout) findViewById(R.id.rl_debug);
            rl_debug.setVisibility(View.VISIBLE);
        }
    }

    private void initview() {
    }

    private void doWorking() {

    }

    public void handle_personalsetting(View view) {

        startActivityForResult(200, PersonalSettingActivity.class);
    }

    public void handle_devicesafe(View view) {
        startActivity(SafeActivity.class);
    }

    public void handle_commonsetting(View view) {
        startActivity(CommonActivity.class);
    }

    public void handle_about(View view) {
        startActivity(AboutActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == LOGINSUCCEED) {
//            switch (requestCode) {
//                case callBy_personalsetting:
//                    handle_personalsetting(null);
//                    break;
//                case callBy_devicesafe:
//                    handle_devicesafe(null);
//                    break;
//                case callBy_commonsetting:
//                    handle_commonsetting(null);
//                    break;
//                case callBy_about:
//                    handle_about(null);
//                    break;
//            }
//        }

        if (resultCode == LOGOUT) {
            finish();
        }
    }

    public void handle_dev(View view) {
        startActivity2(DebugActivity.class);
    }
}
