package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupType;
import com.tzw.noah.ui.MyBaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/5.
 */

public class GroupCreateActivity2 extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.iv_check)
    ImageView iv_check;
    @BindView(R.id.et_nickname)
    EditText et_nickname;

    Context mContext = GroupCreateActivity2.this;

    String Tag = "GroupCreateActivity2";
    boolean checked = true;

    int maxCount = 10;
    Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_create2);
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
        iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = !checked;
                setBackground(iv_check, checked);
            }
        });
        et_nickname.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = et_nickname.getSelectionStart();
                selectionEnd = et_nickname.getSelectionEnd();
                if (temp.length() > maxCount) {


                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_nickname.setText(s);
                    et_nickname.setSelection(tempSelection);
                }
            }
        });
    }

    private void setBackground(ImageView iv, boolean isChecked) {

        if (isChecked) {
            iv.setImageResource(R.drawable.mine_login_checked);
        } else {
            iv.setImageResource(R.drawable.mine_login_uncheck);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            setResult(100);
            finish();
        }
    }

    public void handle_next(View view) {
        if (!checked) {
            toast("请阅读并同意台州网用户协议");
            return;
        }
        if (et_nickname.getText().toString().isEmpty()) {
            toast("请填写群名称");
            return;
        }
        Bundle bu = new Bundle();
        group.groupName = et_nickname.getText().toString();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupCreateActivity4.class, bu);
    }
}
