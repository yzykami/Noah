package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.ui.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/4.
 */

public class GroupEditActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.tv_type)
    TextView tv_type;


    Context mContext = GroupEditActivity.this;

    String Tag = "GroupEditActivity";
    Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_edit);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
    }

    private void findview() {

    }

    private void initview() {
        tv_group_name.setText(group.groupName);
        String introduce = "未设置";
        if (!group.groupIntroduction.isEmpty()) {
            introduce = group.groupIntroduction;
        }
        tv_introduce.setText(introduce);
    }

    public void handle_edit_name(View view) {

        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditNameActivity.class, bu);
    }

    public void handle_edit_introduce(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditIntroduceActivity.class, bu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100)
            if (resultCode == 100) {
                if (data != null) {
                    Bundle bu = data.getExtras();
                    if (bu != null) {
                        group = (Group) bu.getSerializable("DATA");
                        initview();
                    }
                }
            }
    }

    public void handle_back()
    {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        Intent intent = new Intent();
        intent.putExtras(bu);
        setResult(100, intent);
        finish();
    }
}
