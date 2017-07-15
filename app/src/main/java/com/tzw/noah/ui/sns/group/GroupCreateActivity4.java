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
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/5.
 */

public class GroupCreateActivity4 extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv1)
    TextView tv_interduce;
    @BindView(R.id.iv1)
    ImageView iv1;

    Context mContext = GroupCreateActivity4.this;

    String Tag = "GroupCreateActivity4";
    Group group;

    boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_create4);
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
        setBackground(iv1, isChecked);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                setBackground(iv1, isChecked);
            }
        });
    }


    public void handle_submit(View view) {
        List<Param> body = new ArrayList<>();
        body.add(new Param("groupName", group.groupName));
        body.add(new Param("groupTypeId", group.groupTypeId));
        body.add(new Param("groupIntroduction", group.groupIntroduction));
        body.add(new Param("joinmode", group.joinmode));

        new SnsManager(mContext).snsCreateGroup(body, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    toast("群组创建成功");
                    setResult(100);
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });

    }

    private void setBackground(ImageView iv, boolean isChecked) {
        if (isChecked) {
            iv.setImageResource(R.drawable.btn1_selected);
        } else {
            iv.setImageResource(R.drawable.btn1_unselect);
        }
    }

    public void handle_edit_introduce(View view) {
        startActivityForResult(200, GroupEditIntroduceActivity.class);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 200) {
            if (data != null) {
                Bundle bu = data.getExtras();
                if (bu != null) {
                    group.groupIntroduction = bu.getString("DATA");
                    tv_interduce.setText(group.groupIntroduction);
                }
            }
        }
    }
}
