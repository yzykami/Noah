package com.tzw.noah.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.init.NimInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
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

import me.xiaopan.sketchsample.widget.SampleImageView;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class CombineActivity extends MyBaseActivity {

    public static int succeed = 1;
    String TAG = "LoginActivity";
    CombineActivity mContext = CombineActivity.this;
    String outData = "";


    private TextView tv_selectpwd;
    private TextView tv_selectcode;
    private View v1;
    private View v2;
    private EditText et_username;
    private EditText et_pwd;
    private EditText et_code;
    private TextView tv_getcode;
    private TextView tv_submit;
    //    private TextView tv_regsit;
    private TextView tv_title;
    //    private TextView tv_update;
    private ImageView iv_seepwd;
    boolean isSeepwd = false;
    private TextView tv_forgetpwd;
    private LinearLayout ll_agreement;
    private RelativeLayout rl_code;
    private View view_code;

    SampleImageView imageView;

    int MODE = 0;
//    int MODE_

    int secretCode = 0;
    String unionid;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_combine);
        initdata();
        findview();
        runCountDown();
        initview();
//        et_username.setText("15858652110");
//        et_pwd.setText("123456");
        tv_selectpwd.callOnClick();
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            unionid = bu.getString("id");
            type = bu.getInt("type");
        }
        MODE = 0;
    }

    private void findview() {
        tv_selectpwd = (TextView) findViewById(R.id.tv_selectpwd);
        tv_selectcode = (TextView) findViewById(R.id.tv_selectcode);
        tv_getcode = (TextView) findViewById(R.id.tv_getcode);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_update = (TextView) findViewById(R.id.tv_update);
//        tv_regsit = (TextView) findViewById(R.id.tv_regsit);
        v1 = (View) findViewById(R.id.v1);
        v2 = (View) findViewById(R.id.v2);
        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_code = (EditText) findViewById(R.id.et_code);
        iv_seepwd = (ImageView) findViewById(R.id.iv_seepwd);
//        imageView = (SampleImageView) findViewById(R.id.iv_adv);
//        imageView.setVisibility(View.GONE);
        tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);
        ll_agreement = (LinearLayout) findViewById(R.id.ll_agreement);
        rl_code = (RelativeLayout) findViewById(R.id.rl_code);
        view_code = (View) findViewById(R.id.view_code);
//        imageView.displayRoundImageBigThumb("drawable://" + R.drawable.adv);

//        et_code.setVisibility(View.GONE);
    }

    private void initview() {
        //选择注册
        tv_selectcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = 1;
                tv_selectcode.setTextColor(getResources().getColor(R.color.myRed));
                tv_selectpwd.setTextColor(getResources().getColor(R.color.textDarkGray));
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.VISIBLE);
                tv_getcode.setVisibility(View.VISIBLE);
                et_username.setHint("请输入手机号");
                et_pwd.setHint("请输入密码");
                tv_submit.setText("注册");
//                et_code.setVisibility(View.VISIBLE);
                rl_code.setVisibility(View.VISIBLE);
                view_code.setVisibility(View.VISIBLE);
                tv_forgetpwd.setVisibility(View.GONE);
                ll_agreement.setVisibility(View.VISIBLE);
            }

        });
        //选择老用户
        tv_selectpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = 0;
                tv_selectpwd.setTextColor(getResources().getColor(R.color.myRed));
                tv_selectcode.setTextColor(getResources().getColor(R.color.textDarkGray));
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                tv_getcode.setVisibility(View.GONE);
                et_username.setHint("请输入会员号/手机号");
                et_pwd.setHint("密码（6-16个字符，数字加字母）");
                tv_submit.setText("登录并绑定");
                rl_code.setVisibility(View.GONE);
                view_code.setVisibility(View.GONE);
                tv_forgetpwd.setVisibility(View.VISIBLE);
                ll_agreement.setVisibility(View.GONE);
            }
        });

//        tv_regsit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

//        tv_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (secretCode == 0)
//                    secretCode++;
//                else if (secretCode == 2) {
//                    et_username.setText("15858652110");
//                    et_pwd.setText("123123");
//                    handle_submit(null);
//                } else secretCode = 0;
//            }
//        });
//
//        tv_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (secretCode == 1)
//                    secretCode++;
//                else
//                    secretCode = 0;
//            }
//        });

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

    public void handle_submit(View view) {
        if (!Utils.isMobileNO(et_username.getText().toString())) {
            toast("请输入正确的手机号！");
            return;
        }
        if (et_pwd.length() < 6) {
            toast("密码必须多于6位");
            return;
        }
        if (MODE == 1) {
            if (et_code.length() <= 0) {
                toast("请输入验证码");
                return;
            }
        }
        showLoaddingDialog();
        new Thread(new LoginThread()).start();
    }

    public void handle_qqlogin(View view) {
    }

    public void handle_wxlogin(View view) {
        if (secretCode == 1)
            secretCode++;
        else
            secretCode = 0;
    }

    public void handle_wblogin(View view) {
    }

    public void handle_findpwd(View view) {
        startActivity2(FindPwdActivity.class);
    }

    public void handle_back(View view) {
        setResult(LOGINFAILURE);
        super.handle_back(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class LoginThread implements Runnable {
        public void run() {
            try {
                Message message = new Message();
                message.what = LOGIN_ERROR;

                String username = et_username.getText().toString();
                String pwd = et_pwd.getText().toString();
                String vcode = et_code.getText().toString();

                List<Param> params;
                params = new ArrayList<>();
                params.add(new Param("memberName", username));
                params.add(new Param("memberPasswd", pwd));

                IMsg iMsg = null;
                if (MODE == 0) {
                    params = new ArrayList<>();
                    params.add(new Param("memberName", username));
                    params.add(new Param("memberPasswd", pwd));
                    params.add(new Param("type", type));
                    params.add(new Param("unionid", unionid));
                    params.add(new Param("bind", 1));
                    iMsg = NetHelper.getInstance().memberBindThird(params);
                } else {
                    params = new ArrayList<>();
                    params.add(new Param("memberMobile", username));
                    params.add(new Param("memberPasswd", pwd));
                    params.add(new Param("type", type));
                    params.add(new Param("vcode", vcode));
                    params.add(new Param("unionid", unionid));
                    params.add(new Param("bind", 2));
                    iMsg = NetHelper.getInstance().memberBindThird(params);
                }
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

                mContext.LoginResultHandler.sendMessage(message);

            } catch (Exception e) {

                Log.log(TAG, e);
                Message message = new Message();
                message.what = LOGIN_INTERNET_FAULT;

                Bundle bu = new Bundle();
                bu.putString("MESSAGE_DATA", getString(R.string.internet_fault));
                message.setData(bu);

                mContext.LoginResultHandler.sendMessage(message);
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
                    dismissLoaddingDialog();
                    toast(msg.getData().getString("MESSAGE_DATA"));
                } else if (msg.what == LOGIN_ERROR) {
                    dismissLoaddingDialog();
                    try {
                        IMsg iMsg = (IMsg) msg.getData().getSerializable("MESSAGE_DATA");
                        toast(iMsg.getMsg());
                    } catch (Exception e) {
                        toast(e.getMessage());
                    }
                } else {

//                    dismissLoaddingDialog();
                    IMsg iMsg = (IMsg) msg.getData().getSerializable("MESSAGE_DATA");

                    if (iMsg.isSucceed()) {
                        User user = User.load(iMsg);
                        UserCache.setUser(user);
                        new DBInit().snsInit();
                        new SnsDBManager(mContext).updateUser(user);
                        NimInit.init(mContext);
                        //云信登录
                        //
                        nim_login(user.netEaseId + "", user.netEaseToken);

                        if (!outData.isEmpty()) {
                            Intent intent = new Intent(mContext, MineMainActivity.class);
                            startActivity(intent);
                        }
//                        setResult(LOGINSUCCEED);
//                        finish();
                    } else {
                        dismissLoaddingDialog();
                        toast(iMsg.getMsg());
                    }
                    super.handleMessage(msg);
                }
            } catch (Exception e) {
                dismissLoaddingDialog();
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
        DialogMaker.dismissProgressDialog();

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

                // 初始化最近会话列表
                RecentContactsFragment.initRecentContacts();

                // 进入主界面
                setResult(LOGINSUCCEED);
                finish();
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(mContext, com.netease.nim.demo.R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(mContext, com.netease.nim.demo.R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    private void onLoginDone() {
        loginRequest = null;
        dismissLoaddingDialog();
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
