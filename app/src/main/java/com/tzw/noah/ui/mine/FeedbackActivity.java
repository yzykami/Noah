package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class FeedbackActivity extends MyBaseActivity {

    String TAG="RegisterActivity";
    FeedbackActivity mycontext = FeedbackActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_feedback);
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
