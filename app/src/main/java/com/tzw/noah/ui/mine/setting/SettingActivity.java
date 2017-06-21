package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.AboutActivity;
import com.tzw.noah.ui.mine.LoginActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class SettingActivity extends MyBaseActivity {


    String TAG = "RegisterActivity";
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

    }

    private void initview() {
    }

    private void doWorking() {

    }

    public void handle_personalsetting(View view) {

        startActivityForResult(200 ,PersonalSettingActivity.class);
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
}
