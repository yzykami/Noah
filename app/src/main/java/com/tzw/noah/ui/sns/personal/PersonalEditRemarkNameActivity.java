package com.tzw.noah.ui.sns.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/12.
 */

public class PersonalEditRemarkNameActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;

    Context mContext = PersonalEditRemarkNameActivity.this;

    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_personal_edit_remarkname);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            user = (User) bu.getSerializable("DATA");
        } else
            user = new User();
    }

    private void findview() {

    }

    private void initview() {
        if (user.remarkName.isEmpty())
            et_nickname.setText(user.memberNickName);
        else
            et_nickname.setText(user.remarkName);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nickname.setText("");
            }
        });
    }

    public void handle_save(View view) {
        if (et_nickname.getText().toString().isEmpty()) {
            toast("请输入备注名");
            return;
        }
        user.remarkName = et_nickname.getText().toString();

        new SnsManager(mContext).snsInfo(user, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    toast("备注修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
