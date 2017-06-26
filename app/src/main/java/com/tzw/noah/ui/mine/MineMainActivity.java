package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.SettingActivity;
import com.tzw.noah.widgets.CircleImageView;

/**
 * Created by yzy on 2017/6/8.
 */

public class MineMainActivity extends MyBaseActivity {

    String TAG = "MineMainActivity";
    MineMainActivity mycontext = MineMainActivity.this;
    private TextView tv_name;
    private TextView tv_sign;
    private TextView tv_login;
    private TextView tv_friend_num;
    private TextView tv_group_num;
    private TextView tv_circle_num;
    private TextView tv_airtle_num;
    private TextView tv_reply_num;
    private User user;
    private ImageView iv_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_main);

        findview();
        initview();
        doWorking();
    }

    private void findview() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_friend_num = (TextView) findViewById(R.id.tv_friend_num);
        tv_group_num = (TextView) findViewById(R.id.tv_group_num);
        tv_circle_num = (TextView) findViewById(R.id.tv_circle_num);
        tv_airtle_num = (TextView) findViewById(R.id.tv_airtle_num);
        tv_reply_num = (TextView) findViewById(R.id.tv_reply_num);
        iv_head = (ImageView) findViewById(R.id.iv_head);
    }

    private void initview() {
        user = UserCache.getUser();
        boolean islogin = isLogin();
        if (islogin) {
            tv_name.setText(user.memberNickName);
            String sign = "";
            if (!user.memberIntroduce.isEmpty()) {
                sign += user.memberIntroduce + "| ";
            }
            sign += "积分 " + user.growth;
            tv_sign.setText(sign);
            iv_head.setImageResource(R.drawable.mine_login_user);
//            ((CircleImageView)iv_head).setNum(99);
            tv_login.setVisibility(View.GONE);
        } else {
            tv_login.setVisibility(View.VISIBLE);
            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mycontext, LoginActivity.class);
                    startActivity(intent);
                }
            });
            tv_name.setText("未登录");
            tv_sign.setText("1秒登录，专享个性化服务");
            iv_head.setImageResource(R.drawable.default_user);
        }
    }

    private void doWorking() {

    }

    public final int callby_feedback = 10001;
    public final int callby_about = 10002;
    public final int callby_setting = 10003;

    public void handle_login(View view) {
        Intent intent = new Intent(mycontext, LoginActivity.class);
        startActivity(intent);
    }

    public void handle_feedback(View view) {

        startActivity(FeedbackActivity.class);
    }

    public void handle_about(View view) {
        startActivity(AboutActivity.class);
    }

    public void handle_setting(View view) {
        startActivity(SettingActivity.class);
    }

    public void handle_dev(View view) {
    }

    public void handle_1(View view) {
        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_2(View view) {
        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_3(View view) {
        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.log(TAG,"onResume");
        initview();
    }
}
