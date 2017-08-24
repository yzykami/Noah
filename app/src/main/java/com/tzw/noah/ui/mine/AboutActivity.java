package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class AboutActivity extends MyBaseActivity {

    String TAG = "AboutActivity";
    AboutActivity mycontext = AboutActivity.this;
    private TextView tv_ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_about);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        tv_ver = (TextView) findViewById(R.id.tv_ver);
    }

    private void initview() {
        String appver = "1.0";
        String bulid = "1";
        try {
            appver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
            bulid = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = "version " + appver + " bulid " + bulid;
        tv_ver.setText(str);
    }

    private void doWorking() {

    }

    public void handle_login(View view) {
        Intent intent = new Intent(mycontext, LoginActivity.class);
        startActivity(intent);
    }
}
