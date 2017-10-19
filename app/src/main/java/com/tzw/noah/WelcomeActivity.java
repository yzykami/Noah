package com.tzw.noah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;

/**
 * Created by yzy on 2017/7/28.
 */

public class WelcomeActivity extends MyBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        printTime(this,"WelcomeActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = getIntent().getData();
        if (uri != null) {
            Log.d("aaa",uri.toString());
        }


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        printTime(this,"WelcomeActivity");
    }
}
