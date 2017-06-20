package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class SafePwdActivity extends MyBaseActivity {

    String TAG="RegisterActivity";
    SafePwdActivity mycontext = SafePwdActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_safe_pwd);
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

    public void handle_login(View view) {
        Intent intent = new Intent(mycontext,LoginActivity.class);
        startActivity(intent);
    }
}
