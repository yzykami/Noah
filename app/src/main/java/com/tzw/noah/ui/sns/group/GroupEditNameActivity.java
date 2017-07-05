package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/5.
 */

public class GroupEditNameActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;

    Context mContext = GroupEditNameActivity.this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_edit_groupname);
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
