package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.DeviceUuidFactory;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.utils.VCodeCountDownTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class RegisterActivity extends MyBaseActivity {

    String TAG = "RegisterActivity";
    RegisterActivity mContext = RegisterActivity.this;
    private EditText et_username;
    private EditText et_code;
    private TextView tv_getcode;
    private EditText et_pwd;
    private ImageView iv_check;

    boolean ischeck = true;
    private ImageView iv_seepwd;
    boolean isSeepwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_register);
        findview();
        runCountDown();
        initview();
        doWorking();
    }

    private void findview() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_getcode = (TextView) findViewById(R.id.tv_getcode);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        iv_check = (ImageView) findViewById(R.id.iv_check);
        iv_seepwd = (ImageView) findViewById(R.id.iv_seepwd);
    }

    private void initview() {
        iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischeck = !ischeck;
                if (ischeck) {
                    iv_check.setImageResource(R.drawable.mine_login_checked);
                } else
                    iv_check.setImageResource(R.drawable.mine_login_uncheck);
            }
        });

        iv_seepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSeepwd = !isSeepwd;
                if (isSeepwd) {
                    iv_seepwd.setImageResource(R.drawable.mine_login_seepwd);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                } else {
                    iv_seepwd.setImageResource(R.drawable.mine_login_notseepwd);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                }
            }
        });
        tv_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VCodeCountDownTimer.getInstance().isClickable()) {
                    doGetVCode();
                }
            }
        });
    }

    private void startCountDown() {
        VCodeCountDownTimer.getInstance().start(tv_getcode);
    }

    private void runCountDown() {
        VCodeCountDownTimer.getInstance().run(tv_getcode);
    }

    private void doGetVCode() {
        if (!Utils.isMobileNO(et_username.getText().toString())) {
            toast("请输入正确的手机号！");
            return;
        }

        List<Param> body = new ArrayList<>();
        body.add(new Param("phone", et_username.getText().toString()));
        body.add(new Param("type", "register"));
        body.add(new Param("clientCode", new DeviceUuidFactory(this).getDeviceUuidString()));
        NetHelper.getInstance().smsSendCode(body, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(mContext.getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("验证码已发送");
                        startCountDown();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(TAG, e);
                }
            }
        });
    }


    private void doWorking() {

    }

    public void handle_login(View view) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    public void handle_submit(View view) {

    }
}
