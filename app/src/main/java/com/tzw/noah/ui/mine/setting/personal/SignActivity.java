package com.tzw.noah.ui.mine.setting.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.User;
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

public class SignActivity extends MyBaseActivity {

    String TAG = "SignActivity";
    SignActivity mycontext = SignActivity.this;
    private EditText et_sign;
    private TextView tv_count;
    int maxCount = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_sign);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
    }

    private void findview() {
        et_sign = (EditText) findViewById(R.id.et_sign);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    private void initview() {
        et_sign.setText(UserCache.getUser().memberIntroduce);
        tv_count.setText((maxCount - UserCache.getUser().memberIntroduce.length())+"");
        et_sign.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = et_sign.getSelectionStart();
                selectionEnd = et_sign.getSelectionEnd();
                if (temp.length() > maxCount) {


                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_sign.setText(s);
                    et_sign.setSelection(tempSelection);
                }
                tv_count.setText((maxCount - temp.length())+"");
            }
        });
    }

    private void doWorking() {
    }


    public void handle_save(View view) {
        final User user = UserCache.getUser();
        final String sign = et_sign.getText().toString();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNickName", user.memberNickName));
        body.add(new Param("memberSex", user.memberSex));
        body.add(new Param("memberInterest", user.memberInterest));
        body.add(new Param("memberCharacter", user.memberCharacter));
        body.add(new Param("memberWork", user.memberWork));
        body.add(new Param("areaId", user.areaId));
        body.add(new Param("memberIntroduce", et_sign.getText().toString()));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    user.memberIntroduce = sign;
                    UserCache.setUser(user);
                    toast("签名修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
