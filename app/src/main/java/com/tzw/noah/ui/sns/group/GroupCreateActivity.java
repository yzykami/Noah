package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.ui.BottomPopupWindow;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

/**
 * Created by yzy on 2017/7/3.
 */

public class GroupCreateActivity extends MyBaseActivity {
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.ll_member)
    LinearLayout ll_member;

    Context mContext = GroupCreateActivity.this;
    private List<SnsPerson> memberlist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_create);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        memberlist = Utils.makeData();
    }

    private void findview() {

    }

    private void initview() {
        ll_member.removeAllViews();
        for (int i = 0; i < 6 && i < memberlist.size(); i++) {
            ll_member.addView(getMemberView(memberlist.get(i)));
        }
    }

    private View getMemberView(SnsPerson snsPerson) {

        float span = getResources().getDimension(R.dimen.bj);

        float sw = Utils.getSrceenWidth();

        int itemSize = (int) ((sw - 7 * span) / 6);

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.sns_group_member_item, null);

        SampleImageViewHead iv = (SampleImageViewHead) rl.findViewById(R.id.iv_head);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();

        int nn = (int) span;

        layoutParams.width = itemSize;
        layoutParams.height = itemSize;
        layoutParams.setMargins(nn, 0, 0, nn / 2);
        iv.setLayoutParams(layoutParams);
        iv.displayImage(snsPerson.headUrl);
        return rl;
    }

    public void handle_manager(View view) {
        startActivity(GroupManagerActivity.class);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            finish();
        }
    }

    public void handle_next(View view) {
        startActivityForResult(100,GroupCreateActivity2.class);
    }
}
