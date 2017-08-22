package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class RegisterActivity extends MyBaseActivity {

    String TAG = "RegisterActivity";
    RegisterActivity mycontext = RegisterActivity.this;
    private EditText et_username;
    private EditText et_code;
    private EditText et_pwd;
    private ImageView iv_check;
    boolean ischeck = true;

    private ImageView iv_seepwd;
    boolean isSeepwd=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_register);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_code = (EditText) findViewById(R.id.et_code);
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
                isSeepwd=!isSeepwd;
                if(isSeepwd)
                {
                    iv_seepwd.setImageResource(R.drawable.mine_login_seepwd);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                }
                else {
                    iv_seepwd.setImageResource(R.drawable.mine_login_notseepwd);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().toString().length());
                }
            }
        });
    }

    private void doWorking() {

    }

    public void handle_login(View view) {
        Intent intent = new Intent(mycontext, LoginActivity.class);
        startActivity(intent);
    }

    public void handle_submit(View view) {
        if(!ischeck)
        {
            toast("请阅读并同意《台州网用户协议》");
            return ;
        }
        if (!Utils.isMobileNO(et_username.getText().toString())) {
            toast("请输入正确的手机号！");
            return;
        }
        if (et_pwd.length() < 6) {
            toast("密码必须多于6位");
            return;
        }

        List<Param> body=new ArrayList<>();
        body.add(new Param("memberMobile",et_username.getText().toString()));
        body.add(new Param("memberPasswd",et_pwd.getText().toString()));
        body.add(new Param("vcode","999999"));
        final IMsg[] imsg1 = new IMsg[1];
        NetHelper.getInstance().memberRegister(body,new StringDialogCallback(this) {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.log(TAG,e);
                        toast(getString(R.string.internet_fault));
                    }

                    @Override
                    public void onResponse(IMsg iMsg) {
                        if(iMsg.isSucceed())
                        {
                            toast("注册成功,请登录");
                            finish();
                        }
                        else
                        {
                            Log.log(TAG,iMsg.getMsg());
                            toast(iMsg.getMsg());
                        }
                    }
                });
    }
}
