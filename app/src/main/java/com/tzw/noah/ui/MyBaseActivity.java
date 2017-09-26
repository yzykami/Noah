package com.tzw.noah.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.ui.mine.setting.SafeActivity;
import com.tzw.noah.utils.StatusBarUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class MyBaseActivity extends AppCompatActivity implements StatusBarUtil.StatusBarProvider {
    public static final int LOGINFAILURE = 0;
    public static final int LOGINSUCCEED = 1;
    public static final int LOGOUT = 2;
    public static final int MAKESURELOGINCODE = 888;

    public static boolean isNetWorkConnected;

    public static List<Activity> listActivity = new ArrayList<>();

    public Map<Object, Object> classMap;
    private Object classType;
    private static int statusBarHeight = -1;

    private View statusBar;

    public interface LoginListener {
        public void onLogin(boolean isLogin);
    }

    protected LoginListener mLoginListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        listActivity.add(this);
    }

    @Override
    protected void onDestroy() {
        try {
            listActivity.remove(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void exit() {
        try {
            for (int i = 0; i < listActivity.size(); i++) {
                Activity a = listActivity.get(i);
                if (a != null)
                    a.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }

    public void setStatusBarLightMode() {
        if (statusBar == null)
            statusBar = findViewById(R.id.statusBar);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        setStatusBarHeight(statusBar);
    }

    public void setStatusBarDarkMode() {
        if (statusBar == null)
            statusBar = findViewById(R.id.statusBar);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarDarkMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.transParent);
        setStatusBarHeight(statusBar);
    }

    @Override
    public View getStatusBar() {
        return statusBar;
    }

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
        startActivity2(cls, null);
    }

    //不需要登录
    public void startActivity2(Class<?> cls, Bundle bu) {
        Intent intent = new Intent(this, cls);
        if (bu != null)
            intent.putExtras(bu);
        startActivity(intent);
    }
    //不需要登录
    public void startActivityForResult2(int real_requestcode, Class<?> cls) {
        startActivityForResult2(real_requestcode, cls, null);
    }

    //不需要登录
    public void startActivityForResult2(int real_requestcode, Class<?> cls, Bundle bu) {
        Intent intent = new Intent(this, cls);
        if (bu != null)
            intent.putExtras(bu);
        startActivityForResult(intent, real_requestcode);
    }

    //需要登录
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    //需要登录
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
        } else if (resultCode == LOGINFAILURE) {
            if (mLoginListener != null)
                mLoginListener.onLogin(false);
        }
    }

    protected boolean makesureLogin() {
        return checkLogin(MAKESURELOGINCODE, null, null);
    }

    private void onActivityResultBack(int requestCode) {
        ClassMap o = (ClassMap) classMap.get(requestCode);
        Class<?> cls = o.getClassType();
        int real_requestcode = o.getRealRequestCode();

        if (real_requestcode == MAKESURELOGINCODE) {
            if (mLoginListener != null)
                mLoginListener.onLogin(true);
            return;
        }

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

    public void showKeyboard(boolean isShow) {
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

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus, int time) {
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
        }, time);
    }

    public int getStatusBarHeight() {
        if (statusBarHeight == -1) {
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
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
            layoutParams.height = getStatusBarHeight();
            view.setLayoutParams(layoutParams);
        }
    }

    public void setStatusBarHeight() {
        if (statusBar == null)
            statusBar = findViewById(R.id.statusBar);
        if (statusBar == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = getStatusBarHeight();
            statusBar.setLayoutParams(layoutParams);
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

