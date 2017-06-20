package com.tzw.noah.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.ui.mine.setting.SafeActivity;

/**
 * Created by yzy on 2017/6/9.
 */

public class MyBaseActivity extends AppCompatActivity {
    public static final int LOGINSUCCEED = 1;
    public static final int LOGOUT = 2;

    public void handle_back(View v) {
        this.finish();
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isLogin() {
        return UserCache.isLogin();
    }

    public boolean checkLogin(int login_requestcode) {
        if (isLogin())
            return true;
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            startActivityForResult(intent, login_requestcode);
            return false;
        }
    }

    public void startActivity(int login_requestcode ,Class<?> cls)
    {
        if (!checkLogin(login_requestcode))
            return;
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivityForResult(int login_requestcode ,int real_requestcode,Class<?> cls)
    {
        if (!checkLogin(login_requestcode))
            return;
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent,real_requestcode);
    }

}
