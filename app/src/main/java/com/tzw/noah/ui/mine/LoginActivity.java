package com.tzw.noah.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.demo.main.activity.MainActivity;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/9.
 */

public class LoginActivity extends MyBaseActivity {

    public static int succeed = 1;
    String TAG = "LoginActivity";
    LoginActivity mycontext = LoginActivity.this;
    String outData = "";


    private TextView tv_selectpwd;
    private TextView tv_selectcode;
    private View v1;
    private View v2;
    private EditText et_username;
    private EditText et_pwd;
    private TextView tv_getcode;
    private TextView tv_regsit;

    int Mode = 0;
//    int MODE_

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_login);
        initdata();
        findview();
        initview();
//        et_username.setText("15858652110");
//        et_pwd.setText("123456");
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            outData = bu.getString("Data");
        }
    }

    private void findview() {
        tv_selectpwd = (TextView) findViewById(R.id.tv_selectpwd);
        tv_selectcode = (TextView) findViewById(R.id.tv_selectcode);
        tv_getcode = (TextView) findViewById(R.id.tv_getcode);
        tv_regsit = (TextView) findViewById(R.id.tv_regsit);
        v1 = (View) findViewById(R.id.v1);
        v2 = (View) findViewById(R.id.v2);
        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
    }

    private void initview() {
        //选择验证码登录
        tv_selectcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectcode.setTextColor(getResources().getColor(R.color.myRed));
                tv_selectpwd.setTextColor(getResources().getColor(R.color.textDarkGray));
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.VISIBLE);
                tv_getcode.setVisibility(View.VISIBLE);
                et_username.setHint("请输入手机号");
                et_pwd.setHint("请输入短信验证码");
            }

        });
        //选择密码登录
        tv_selectpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectpwd.setTextColor(getResources().getColor(R.color.myRed));
                tv_selectcode.setTextColor(getResources().getColor(R.color.textDarkGray));
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                tv_getcode.setVisibility(View.GONE);
                et_username.setHint("请输入会员号/手机号");
                et_pwd.setHint("请输入密码");
            }
        });

        tv_regsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mycontext, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doWorking() {
    }

    public void handle_submit(View view) {
        if (!Utils.isMobileNO(et_username.getText().toString())) {
            toast("请输入正确的手机号！");
            return;
        }
        if (et_pwd.length() < 6) {
            toast("密码必须多于6位");
            return;
        }
        new Thread(new LoginThread()).start();
    }

    class LoginThread implements Runnable {
        public void run() {
            try {
                Message message = new Message();
                message.what = LOGIN_ERROR;

                String username = et_username.getText().toString();
                String pwd = et_pwd.getText().toString();

                List<Param> params;
                params = new ArrayList<>();
                params.add(new Param("memberName", username));
                params.add(new Param("memberPasswd", pwd));

                IMsg iMsg = NetHelper.getInstance().memberLogin(params);

                if (iMsg.isSucceed()) {
                    String token = iMsg.getValue("tokenCode");
                    UserCache.setToken(token);
                    List<Param> p2 = new ArrayList<>();
                    p2.add(new Param("tokenCode", token));
                    iMsg = NetHelper.getInstance().memberLoginKey(p2);
                    String loginKey = iMsg.getJsonObject("loginKeyRObj").getValue("loginKey");
                    UserCache.setLoginkey(loginKey);

                    if (iMsg.isSucceed()) {
                        iMsg = NetHelper.getInstance().getUserDetails();
                        if (iMsg.isSucceed())
                            message.what = LOGIN_SUCCESS;
                        else {
                            UserCache.setToken("");
                            UserCache.setLoginkey("");
                        }
                    }
                }
                Bundle bu = new Bundle();

                bu.putSerializable("MESSAGE_DATA", iMsg);

                message.setData(bu);

                mycontext.LoginResultHandler.sendMessage(message);

            } catch (Exception e) {

                com.tzw.noah.logger.Log.log(TAG, e);
                Message message = new Message();
                message.what = LOGIN_INTERNET_FAULT;

                Bundle bu = new Bundle();
                bu.putString("MESSAGE_DATA", getString(R.string.internet_fault));
                message.setData(bu);

                mycontext.LoginResultHandler.sendMessage(message);
            }
        }
    }


    private final int LOGIN_SUCCESS = 0x1001;
    private final int LOGIN_ERROR = 0x1002;
    private final int LOGIN_INTERNET_FAULT = 0x1003;

    Handler LoginResultHandler = new Handler() {

        public void handleMessage(Message msg) {

            try {
//                LoadDialog.dismiss();
                // 网络错误
                if (msg.what == LOGIN_INTERNET_FAULT) {
                    toast(msg.getData().getString("MESSAGE_DATA"));
                } else if (msg.what == LOGIN_ERROR) {
                    try {
                        IMsg iMsg = (IMsg) msg.getData().getSerializable("MESSAGE_DATA");
                        toast(iMsg.getMsg());
                    } catch (Exception e) {
                        toast(e.getMessage());
                    }
                } else {

                    IMsg iMsg = (IMsg) msg.getData().getSerializable("MESSAGE_DATA");

                    if (iMsg.isSucceed()) {
                        User user = User.load(iMsg);
                        UserCache.setUser(user);
                        new DBInit().snsInit();
                        new SnsDBManager(mycontext).updateUser(user);

                        //云信登录
                        //
                        nim_login(user.netEaseId + "", user.netEaseToken);

                        if (!outData.isEmpty()) {
                            Intent intent = new Intent(mycontext, MineMainActivity.class);
                            startActivity(intent);
                        }
//                        setResult(LOGINSUCCEED);
//                        finish();
                    } else {
                        toast(iMsg.getMsg());
                    }
                    super.handleMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


    private AbortableFuture<LoginInfo> loginRequest;

    private void nim_login(String n_account, String n_token) {
        DialogMaker.showProgressDialog(this, null, getString(com.netease.nim.demo.R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = n_account;
        final String token = n_token;
        // 登录
        loginRequest = NimUIKit.doLogin(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

                onLoginDone();

                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
                setResult(LOGINSUCCEED);
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(mycontext, com.netease.nim.demo.R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mycontext, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(mycontext, com.netease.nim.demo.R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }
}
