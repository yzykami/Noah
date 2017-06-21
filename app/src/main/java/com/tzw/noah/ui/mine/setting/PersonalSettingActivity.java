package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.personal.AreaProvinceActivity;
import com.tzw.noah.ui.mine.setting.personal.BirthdayActivity;
import com.tzw.noah.ui.mine.setting.personal.NickNameActivity;
import com.tzw.noah.utils.FileUtil;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class PersonalSettingActivity extends MyBaseActivity {

    String TAG = "RegisterActivity";
    PersonalSettingActivity mycontext = PersonalSettingActivity.this;

    final static int CallBy_NickName = 10001;
    final static int CallBy_Birth = 10002;
    final static int CallBy_Sex = 10003;
    final static int CallBy_Area = 10004;
    final static int CallBy_Interest = 10005;
    final static int CallBy_Character = 10006;
    final static int CallBy_Job = 10007;
    final static int CallBy_Sign = 10008;

    private TextView tv_id;
    private TextView tv_nickname;
    private TextView tv_birth;
    private TextView tv_sex;
    private TextView tv_area;
    private TextView tv_interest;
    private TextView tv_character;
    private TextView tv_job;
    private TextView tv_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_birth = (TextView) findViewById(R.id.tv_birth);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_interest = (TextView) findViewById(R.id.tv_interest);
        tv_character = (TextView) findViewById(R.id.tv_character);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
    }

    private void initview() {
        User user = UserCache.getUser();
        tv_id.setText(user.memberId + "");
        tv_nickname.setText(user.memberNickName);
        String birth = user.memberBirthday;
        if (birth.equals("0000-00-00 00:00:00")) {
            birth = "请设置";
        } else if (birth.length() > 10) {
            birth = birth.substring(0, 10);
        }
        tv_birth.setText(birth);
        tv_sex.setText(user.memberSex == 0 ? "男" : "女");
        tv_area.setText(user.areaId + "");
        tv_interest.setText(user.memberInterest);
        tv_character.setText(user.memberCharacter);
        tv_job.setText(user.memberWork);
        tv_sign.setText(user.memberIntroduce);
    }

    private void doWorking() {

    }


    public void handle_nickname(View view) {
        startActivity(NickNameActivity.class);
    }

    public void handle_birth(View view) {
        startActivity(BirthdayActivity.class);
    }

    public void handle_sex(View view) {
        FileUtil.save2InternalSdCard("dd", "123", "abc");
        FileUtil.readFromInternalSdCard("dd", "123");
    }

    public void handle_area(View view) {
        startActivity(AreaProvinceActivity.class);
//        startActivity(CallBy_Area, AreaProvinceActivity.class);
    }

    public void handle_interest(View view) {
    }

    public void handle_character(View view) {
    }

    public void handle_job(View view) {
    }

    public void handle_sign(View view) {
    }

    public void handle_logout(View view) {

        NetHelper.getInstance().memberLogout(Param.makeSingleParam("tokenCode", UserCache.getToken()), new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
            }

            @Override
            public void onResponse(IMsg imsg) {
                if (imsg.isSucceed()) {
                    UserCache.setLoginkey("");
                    UserCache.setToken("");
                    toast("注销成功。");
                    setResult(LOGOUT);
                    finish();
                } else if (imsg.getCode() == 1024) {
                    UserCache.setLoginkey("");
                    UserCache.setToken("");
                    toast("注销成功。");
                    setResult(LOGOUT);
                    finish();
                } else {
                    toast(imsg.getMsg());
                }
            }
        });
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == LOGINSUCCEED) {
//            switch (requestCode) {
//                case CallBy_NickName:
//                    handle_nickname(null);
//                    break;
//                case CallBy_Sex:
//                    handle_sex(null);
//                    break;
//                case CallBy_Birth:
//                    handle_birth(null);
//                    break;
//                case CallBy_Area:
//                    handle_area(null);
//                    break;
//                case CallBy_Interest:
//                    handle_interest(null);
//                    break;
//                case CallBy_Character:
//                    handle_character(null);
//                    break;
//                case CallBy_Job:
//                    handle_job(null);
//                    break;
//                case CallBy_Sign:
//                    handle_sign(null);
//                    break;
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        initview();
    }
}
