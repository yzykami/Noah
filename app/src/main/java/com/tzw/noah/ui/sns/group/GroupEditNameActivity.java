package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
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

public class GroupEditNameActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;

    Context mContext = GroupEditNameActivity.this;

    String Tag = "GroupEditNameActivity";

    Group group;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_edit_name);
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
//        tv_title.setText("群昵称");
        et_nickname.setHint("请输入您的群名称");
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nickname.setText("");
            }
        });
        if (!group.groupName.isEmpty()) {
            et_nickname.setText(group.groupName);
        }
    }

    public void handle_save(View view) {
        if (group.groupAttribute == Group.Type.GROUP) {
            if(et_nickname.getText().toString().equals(""))
            {
                toast("群名称不能为空");
                return;
            }
            List<Param> body = new ArrayList<>();
//        body.add(new Param("groupName", group.groupName));
            body.add(new Param("groupName", et_nickname.getText().toString()));
            body.add(new Param("groupTypeId", group.groupTypeId));
//        body.add(new Param("groupIntroduction",group.groupIntroduction));
//        body.add(new Param("groupBulletin",group.groupBulletin));
            new SnsManager(mContext).snsUpdateGroupInfo(group.groupId, body, new StringDialogCallback(mContext) {
                @Override
                public void onFailure(Call call, IOException e) {
                    toast(getResources().getString(R.string.internet_fault));
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            group.groupName = et_nickname.getText().toString();
                            Bundle bu = new Bundle();
                            bu.putSerializable("DATA", group);
                            Intent intent = new Intent();
                            intent.putExtras(bu);
                            setResult(100, intent);
                            finish();
                            toast("群名称修改成功");
                        } else {
                            toast(iMsg.getMsg());
                        }
                    } catch (Exception e) {
                        Log.log(Tag, e);
                    }
                }
            });
        } else {
            List<Param> body = new ArrayList<>();
            body.add(new Param("groupName", et_nickname.getText().toString()));
            new SnsManager(mContext).snsDiscussInfo(group.groupId, body, new StringDialogCallback(mContext) {
                @Override
                public void onFailure(Call call, IOException e) {
                    toast(getResources().getString(R.string.internet_fault));
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed()) {
                            group.groupName = et_nickname.getText().toString();
                            Bundle bu = new Bundle();
                            bu.putSerializable("DATA", group);
                            Intent intent = new Intent();
                            intent.putExtras(bu);
                            setResult(100, intent);
                            finish();
                            toast("群名称修改成功");
                        } else {
                            toast(iMsg.getMsg());
                        }
                    } catch (Exception e) {
                        Log.log(Tag, e);
                    }
                }
            });
        }
    }
}
