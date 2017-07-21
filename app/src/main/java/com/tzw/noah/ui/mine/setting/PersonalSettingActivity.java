package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.demo.login.LogoutHelper;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.Dict;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.personal.AreaProvinceActivity;
import com.tzw.noah.ui.mine.setting.personal.BirthdayActivity;
import com.tzw.noah.ui.mine.setting.personal.CharacterActivity;
import com.tzw.noah.ui.mine.setting.personal.InterestActivity;
import com.tzw.noah.ui.mine.setting.personal.NickNameActivity;
import com.tzw.noah.ui.mine.setting.personal.SexActivity;
import com.tzw.noah.ui.mine.setting.personal.SignActivity;
import com.tzw.noah.ui.mine.setting.personal.WorkActivity;
import com.tzw.noah.utils.FileUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class PersonalSettingActivity extends MyBaseActivity {

    String TAG = "PersonalSettingActivity";
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
        tv_id.setText(user.memberNo + "");
        tv_nickname.setText(user.memberNickName);
        String birth = user.memberBirthday;
        if (birth.equals("0000-00-00 00:00:00")) {
            birth = "请设置";
        } else if (birth.length() > 10) {
            birth = birth.substring(0, 10);
        }
        tv_birth.setText(birth);
        tv_sex.setText(user.memberSex == 0 ? "男" : "女");
        tv_area.setText(getArea(user.areaId));
        tv_interest.setText(getInterestbyID(user.memberInterest));
        tv_character.setText(getCharacterByID(user.memberCharacter));
        tv_job.setText(getWorkbyID(user.memberWork));
        tv_sign.setText(user.memberIntroduce);
    }

    private void doWorking() {

    }

    private String getArea(int areaId) {
        String area = "";
        DBManager db = new DBManager(mycontext);

        Area province = db.queryProvinceByTownId(areaId);
        Area city = db.queryCityByTownId(areaId);
        Area town = db.queryTownByTownId(areaId);
        area = province.areaName + " " + city.areaName + " " + town.areaName;
        return area;
    }

    private String getInterestbyID(String memberInterest) {

        List<Dict> interestList = new DBManager(mycontext).selectInterestList();
        return getDictNameById(memberInterest, interestList);
    }

    private String getCharacterByID(String memberCharacter) {
        List<Dict> interestList = new DBManager(mycontext).selectCharacterList();
        return getDictNameById(memberCharacter, interestList);
    }

    private String getWorkbyID(String memberWork) {

        List<Dict> interestList = new DBManager(mycontext).selectJobtList();
        return getDictNameById(memberWork, interestList);
    }

    private String getDictNameById(String ids, List<Dict> list) {
        String s = "";
        String[] ss = ids.split(",");
        for (String sss : ss) {
            if (!sss.isEmpty()) {
                for (Dict dict : list) {
                    if (dict.dictionaryId.equals(sss)) {
                        if (s.isEmpty()) {
                            s += dict.dictionaryName;
                        } else
                            s += " " + dict.dictionaryName;
                    }
                }
            }
        }
        return s;
    }


    public void handle_nickname(View view) {
        startActivity(NickNameActivity.class);
    }

    public void handle_birth(View view) {
        startActivity(BirthdayActivity.class);
    }

    public void handle_sex(View view) {
        startActivity(SexActivity.class);
    }

    public void handle_area(View view) {
        startActivity(AreaProvinceActivity.class);
    }

    public void handle_interest(View view) {
        startActivity(InterestActivity.class);
    }

    public void handle_character(View view) {
        startActivity(CharacterActivity.class);
    }

    public void handle_job(View view) {
        startActivity(WorkActivity.class);
    }

    public void handle_sign(View view) {
        startActivity(SignActivity.class);
    }

    public void handle_logout(View view) {

        NetHelper.getInstance().memberLogout(Param.makeSingleParam("tokenCode", UserCache.getToken()), new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg imsg) {
                if (imsg.isSucceed()) {
                    UserCache.setLoginkey("");
                    UserCache.setToken("");
                    toast("注销成功。");
                    //云信登出
                    LogoutHelper.logout();
                    setResult(LOGOUT);
                    finish();
                } else if (imsg.getCode() == 1024) {
                    UserCache.setLoginkey("");
                    UserCache.setToken("");
                    toast("注销成功。");
                    //云信登出
                    LogoutHelper.logout();
                    setResult(LOGOUT);
                    finish();
                } else {
                    toast(imsg.getMsg());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initview();
    }
}
