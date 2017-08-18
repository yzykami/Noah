package com.tzw.noah.ui.mine.setting.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import com.tzw.timeselector.TimeSelector;
import com.tzw.timeselector.view.PickerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class BirthdayActivity extends MyBaseActivity {

    String TAG = "NickNameActivity";
    BirthdayActivity mycontext = BirthdayActivity.this;
    private TextView tv_birthday;

    List<Boolean> selected;
    List<String> items;
    private NickNameAdapter adapter;
    private PickerView month_pv;
    TimeSelector ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_birthday);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
    }

    private void findview() {
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
    }

    private void initview() {

        User user = UserCache.getUser();
        String birthday = user.memberBirthday;
        String showDate = "1990-01-01";
        if (!birthday.equals("0000-00-00 00:00:00") && birthday.length() >= 10) {
            showDate = birthday.substring(0, 10);
        }
        tv_birthday.setText(showDate);

        ts = new TimeSelector(mycontext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_birthday.setText(time.substring(0, 10));
            }
        }, "1900-01-01 00:00", "2017-06-20 00:00", showDate + " 00:00");
        ts.setMode(TimeSelector.MODE.YMD);
        ts.setTitle("请选择您的生日");
        ts.show();
        tv_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ts.show(tv_birthday.getText() + " 00:00");
            }
        });
    }

    private void doWorking() {
    }

    public void handle_save(View view) {
        final User user = UserCache.getUser();
        final String nickname = tv_birthday.getText().toString();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNickName", user.memberNickName));
        body.add(new Param("memberSex", user.memberSex));
        body.add(new Param("memberInterest", user.memberInterest));
        body.add(new Param("memberCharacter", user.memberCharacter));
        body.add(new Param("memberWork", user.memberWork));
        body.add(new Param("areaId", user.areaId));
        body.add(new Param("memberIntroduce", user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(tv_birthday.getText().toString())));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    user.memberBirthday = tv_birthday.getText().toString();
                    UserCache.setUser(user);
                    toast("生日修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

//    public void onBackPressed() {
//        if (ts.isShowing()) {
//            ts.dismissDialog();
//        } else {
//            finish();
//        }
//    }
}
