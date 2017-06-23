package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class SafePwdActivity extends MyBaseActivity {

    String TAG="SafePwdActivity";
    SafePwdActivity mycontext = SafePwdActivity.this;
    private EditText et_pwd_old;
    private EditText et_pwd1;
    private EditText et_pwd2;
    private TextView tv_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_safe_pwd);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        et_pwd_old = (EditText) findViewById(R.id.et_pwd_old);
        et_pwd1 = (EditText) findViewById(R.id.et_pwd1);
        et_pwd2 = (EditText) findViewById(R.id.et_pwd2);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);

    }

    private void initview() {
        tv_mobile.setText(UserCache.getUser().memberMobile);
    }

    private void doWorking() {

    }

    public void handle_save(View view) {
        if(!et_pwd1.getText().toString().equals(et_pwd2.getText().toString()))
        {
            toast("新密码两次输入不一致");
            return;
        }
        if (et_pwd_old.length() < 6||et_pwd1.length()<6||et_pwd2.length()<6) {
            toast("密码必须多于6位");
            return;
        }
        List<Param> body =new ArrayList<>();
        body.add(new Param("oldPassword",et_pwd_old.getText().toString()));
        body.add(new Param("newPassword",et_pwd1.getText().toString()));
        NetHelper.getInstance().setUserPassword(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.log(TAG,e);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if(iMsg.isSucceed())
                {
                    toast("密码修改成功");
                    finish();

                }
                else
                {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
