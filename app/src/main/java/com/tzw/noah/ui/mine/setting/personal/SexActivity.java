package com.tzw.noah.ui.mine.setting.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
 * Created by yzy on 2017/6/22.
 */

public class SexActivity extends MyBaseActivity {

    String TAG = "SexActivity";
    SexActivity mycontext = SexActivity.this;

    private ImageView iv_male;
    private ImageView iv_female;
    int sex;
    private RelativeLayout rl1;
    private RelativeLayout rl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_sex);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        sex =UserCache.getUser().memberSex;
    }

    private void findview() {
        iv_male = (ImageView) findViewById(R.id.iv_male);
        iv_female = (ImageView) findViewById(R.id.iv_female);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);

    }

    private void initview() {
        if(sex == 0)
        {
            iv_male.setVisibility(View.VISIBLE);
            iv_female.setVisibility(View.GONE);
        }
        else
        {
            iv_male.setVisibility(View.GONE);
            iv_female.setVisibility(View.VISIBLE);
        }
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 0;
                handle_save(null);
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 1;
                handle_save(null);
            }
        });
    }

    private void doWorking() {

    }

    public void handle_save(View view) {
        final User user =UserCache.getUser();
        List<Param> body=new ArrayList<>();
        body.add(new Param("memberNickName",user.memberNickName));
        body.add(new Param("memberSex",sex));
        body.add(new Param("memberInterest",user.memberInterest));
        body.add(new Param("memberCharacter",user.memberCharacter));
        body.add(new Param("memberWork",user.memberWork));
        body.add(new Param("areaId",user.areaId));
        body.add(new Param("memberIntroduce",user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if(iMsg.isSucceed())
                {
                    user.memberSex=sex;
                    UserCache.setUser(user);
                    toast("性别修改成功");
                    finish();
                }
                else
                {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
