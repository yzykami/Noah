package com.tzw.noah.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by yzy on 2017/10/22.
 */

public class ScanResultActivity extends MyBaseActivity {

    String TAG = ScanResultActivity.class.getName();
    String code = "";
    Context mContext = ScanResultActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout_scan_result);
        setStatusBarLightMode();
        initdata();
        findview();
        initview();
    }

    private void initdata() {

        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            code = bu.getString("code");
        }
    }

    private void findview() {
        TextView tvResult = (TextView) findViewById(R.id.tv_result);
        tvResult.setText(code);
    }

    private void initview() {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void handle_back(View v) {
        super.handle_back(v);
    }
}
