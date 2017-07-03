package com.tzw.noah.ui.sns.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.ListenedScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

/**
 * Created by yzy on 2017/7/3.
 */

public class PersonalMoreActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;

    Context mContext = PersonalMoreActivity.this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_personal_more);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
    }

    private void findview() {

    }

    private void initview() {
    }

}
