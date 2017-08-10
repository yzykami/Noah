package com.tzw.noah;

import android.content.Intent;
import android.os.Bundle;

import com.tzw.noah.ui.MyBaseActivity;

/**
 * Created by yzy on 2017/7/28.
 */

public class WelcomeActivity extends MyBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
