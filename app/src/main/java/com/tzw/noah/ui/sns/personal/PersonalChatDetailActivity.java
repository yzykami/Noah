package com.tzw.noah.ui.sns.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/3.
 */

public class PersonalChatDetailActivity extends MyBaseActivity {
    Context mContext = PersonalChatDetailActivity.this;

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;

    @BindView(R.id.iv_head)
    ImageView iv_head;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_personal_chat_detail);
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
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PersonalActivity.class);
            }
        });
    }
}
