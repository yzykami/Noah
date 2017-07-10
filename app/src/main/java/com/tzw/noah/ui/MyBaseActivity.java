package com.tzw.noah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.ui.mine.setting.SafeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by yzy on 2017/6/9.
 */

public class MyBaseActivity extends AppCompatActivity {
    public static final int LOGINSUCCEED = 1;
    public static final int LOGOUT = 2;
    public static boolean isNetWorkConnected;


    public Map<Object, Object> classMap;
    private Object classType;

    public void handle_back(View v) {
        this.finish();
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isLogin() {
        return UserCache.isLogin();
    }

    public boolean checkLogin(int real_requestcode, Class<?> cls, Bundle bu) {
        synchronized (this) {
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

    public void startActivity(Class<?> cls) {
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

