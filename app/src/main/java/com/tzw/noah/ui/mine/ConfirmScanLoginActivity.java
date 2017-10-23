package com.tzw.noah.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Base64Coder;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class ConfirmScanLoginActivity extends MyBaseActivity {

    String TAG = ConfirmScanLoginActivity.class.getName();
    String code = "";
    Context mContext = ConfirmScanLoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout_confirmscanlogin);
        setStatusBarLightMode();
        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {

        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            code = bu.getString("code");
        }
    }

    private void findview() {
    }

    private void initview() {
    }

    private void doWorking() {
        NetHelper.getInstance().memberChangeCode(code, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (!iMsg.isSucceed()) {
                            toast("扫码登录提交失败，请重新扫码");
                            finish();
                        }
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(TAG, e);
                }
            }
        });
    }

    private void doLogin() {
        NetHelper.getInstance().memberLoginByVcode(code, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        finish();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(TAG, e);
                }
            }
        });
    }

    private void doCancel() {
        NetHelper.getInstance().memberCancelLogin(code, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {

                    } else {
//                        toast(iMsg.getMsg());
                    }
                    finish();
                } catch (Exception e) {
                    Log.log(TAG, e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        doCancel();
        super.onBackPressed();
    }

    @Override
    public void handle_back(View v) {
        doCancel();
        super.handle_back(v);
    }


    public void handle_dologin(View view) {
        doLogin();
    }

    public void handle_docancel(View view) {
        doCancel();
    }
}
