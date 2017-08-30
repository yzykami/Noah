package com.tzw.noah.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tzw.noah.cache.UserCache;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.ui.mine.setting.SafeActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class MyBaseActivity extends AppCompatActivity {
    public static final int LOGINSUCCEED = 1;
    public static final int LOGOUT = 2;
    public static boolean isNetWorkConnected;


    public Map<Object, Object> classMap;
    private Object classType;
    private static int statusBarHeight=-1;

    public void handle_back(View v) {
        showKeyboard(false);
        this.finish();
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isLogin() {
        return UserCache.isLogin();
    }

    boolean isNeedLogin = true;

    public boolean checkLogin(int real_requestcode, Class<?> cls, Bundle bu) {
        synchronized (this) {
            if (!isNeedLogin)
                return true;
            if (isLogin())
                return true;
            else {
                if (classMap == null) {
                    classMap = new HashMap<Object, Object>();
                }
                int login_requestcode = getRandowmCode();
                classMap.put(login_requestcode, new ClassMap(cls, bu, real_requestcode));
                Intent intent = new Intent(this, LoginActivity.class);

                startActivityForResult(intent, login_requestcode);
                return false;
            }
        }
    }

    private int getRandowmCode() {
        boolean created = false;
        int i = hashCode() % 65535;
        while (!created) {
            i = new Random().nextInt(65535);
            if (
                    !classMap.containsKey(i)) {
                created = true;
                return i;
            }
        }
        return i;
    }

    //不需要登录
    public void startActivity2(Class<?> cls) {
        isNeedLogin = false;
        startActivity(cls, null);
    }

    public void startActivity(Class<?> cls) {
        isNeedLogin = true;
        startActivity(cls, null);
    }

    public void startActivity(Class<?> cls, Bundle bu) {

        if (!checkLogin(0, cls, bu))
            return;
        Intent intent = new Intent(this, cls);
        if (bu != null)
            intent.putExtras(bu);
        startActivity(intent);
    }

    public void startActivityForResult(int real_requestcode, Class<?> cls) {
        startActivityForResult(real_requestcode, cls, null);
    }


    public void startActivityForResult(int real_requestcode, Class<?> cls, Bundle bu) {

        if (!checkLogin(real_requestcode, cls, bu))
            return;
        Intent intent = new Intent(this, cls);
        if (bu != null)
            intent.putExtras(bu);
        startActivityForResult(intent, real_requestcode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LOGINSUCCEED) {
            onActivityResultBack(requestCode);
            return;
        }
    }

    private void onActivityResultBack(int requestCode) {
        ClassMap o = (ClassMap) classMap.get(requestCode);
        Class<?> cls = o.getClassType();
        int real_requestcode = o.getRealRequestCode();
        Bundle bu = o.getBunndle();
        Intent intent = new Intent(this, cls);
        if (bu != null)
            intent.putExtras(bu);

        if (real_requestcode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, real_requestcode);
    }

    StringDialogCallback stringDialogCallback;

    public void showLoaddingDialog() {
        stringDialogCallback = new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(IMsg iMsg) {

            }
        };
        stringDialogCallback.onBefore();
    }

    public void dismissLoaddingDialog() {
        if (stringDialogCallback != null) {
            stringDialogCallback.onAfter();
        }
    }

    Handler handler;
    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    public int getStatusBarHeight()
    {
        if(statusBarHeight==-1) {
            // 获取状态栏高度
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    public void setStatusBarHeight(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
            layoutParams.height = getStatusBarHeight();
            view.setLayoutParams(layoutParams);
        }
    }
}

class ClassMap {
    Class<?> cls;
    Bundle bu;
    int real_requestCode = 0;

    public ClassMap(Class<?> cls, Bundle bu, int real_requestCode) {
        this.cls = cls;
        this.bu = bu;
        this.real_requestCode = real_requestCode;
    }

    public Class<?> getClassType() {
        return cls;
    }

    public Bundle getBunndle() {
        return bu;
    }

    public int getRealRequestCode() {
        return real_requestCode;
    }
}

