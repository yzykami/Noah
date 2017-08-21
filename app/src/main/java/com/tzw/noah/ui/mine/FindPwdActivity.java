package com.tzw.noah.ui.mine;

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

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.group.GroupCreateActivity4;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/8/21.
 */

public class FindPwdActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;

    Context mContext = FindPwdActivity.this;

    String Tag = "FindPwdActivity";

    public static String mobile="";
    public static String vcode="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_findpwd1);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            setResult(100);
            finish();
        }
    }

    public void handle_submit(View view) {
        if (!Utils.isMobileNO(et_username.getText().toString())) {
            toast("请输入正确的手机号！");
            return;
        }
        if (et_pwd.length() <= 0) {
            toast("请输入验证码");
            return;
        }
        mobile = et_username.getText().toString();
        vcode = et_pwd.getText().toString();
        Intent intent = new Intent(this, FindPwdActivity2.class);
        startActivityForResult(intent, 100);
//        showLoaddingDialog();
//        new Thread(new LoginActivity.LoginThread()).start();
    }
}
