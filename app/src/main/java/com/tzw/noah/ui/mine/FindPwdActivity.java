package com.tzw.noah.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.group.GroupCreateActivity4;
import com.tzw.noah.utils.DeviceUuidFactory;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.utils.VCodeCountDownTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/21.
 */

public class FindPwdActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.tv_getcode)
    TextView tv_getcode;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.iv_seepwd)
    ImageView iv_seepwd;


    Context mContext = FindPwdActivity.this;

    String TAG = "FindPwdActivity";

    public static String mobile="";
    public static String vcode="";

    boolean isSeepwd=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_findpwd1);
        ButterKnife.bind(this);
        runCountDown();
        initdata();
        findview();
        initview();
    }

    private void initdata() {
    }

    private void findview() {

    }

    private void initview() {
        iv_seepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSeepwd=!isSeepwd;
                if(isSeepwd)
                {
                    iv_seepwd.setImageResource(R.drawable.mine_login_seepwd);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                }
                else {
                    iv_seepwd.setImageResource(R.drawable.mine_login_notseepwd);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
        body.add(new Param("type", "forgot"));
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
