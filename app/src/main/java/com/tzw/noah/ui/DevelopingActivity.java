package com.tzw.noah.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.MainActivity;
import com.tzw.noah.R;

/**
 * Created by yzy on 2017/10/22.
 */

public class DevelopingActivity extends MyBaseActivity {

    String TAG = DevelopingActivity.class.getName();
    String title = "";
    Context mContext = DevelopingActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout_developing);
        setStatusBarDarkMode();
        initdata();
        findview();
        initview();
    }

    private void initdata() {

        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
        }
    }

    private void findview() {
        title = "圈子";
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView btn_rlbg = (TextView) findViewById(R.id.btn_rlbg);
        btn_rlbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHome();
            }
        });
    }

    private void initview() {
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        gotoHome();
    }

    @Override
    public void handle_back(View v) {
        super.handle_back(v);
    }

    public void gotoHome()
    {
        MainActivity.getInstance().selectTag(0);
    }
}
