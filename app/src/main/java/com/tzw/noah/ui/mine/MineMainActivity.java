package com.tzw.noah.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.contact.activity.UserProfileSettingActivity;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.permission.BaseMPermission;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.module.Container;
import com.tzw.noah.MainActivity;
import com.tzw.noah.R;
import com.tzw.noah.appupdate.UpdateManager;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.PersonalSettingActivity;
import com.tzw.noah.ui.mine.setting.SettingActivity;
import com.tzw.noah.ui.sns.add.AddActivity;
import com.tzw.noah.ui.sns.friendlist.FriendListActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/8.
 */

public class MineMainActivity extends MyBaseActivity  {

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
    private SampleImageViewHead iv_head;

    SessionCustomization customization = new SessionCustomization();
    private boolean isFirstLoad = true;
    static MineMainActivity instance;

    public static void reload() {
        if (instance != null)
            instance.initview();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_main);
        instance = this;
        setStatusBarLightMode();
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
        iv_head = (SampleImageViewHead) findViewById(R.id.iv_head);



        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
                    option.titleResId = com.netease.nim.demo.R.string.set_head_image;
                    option.crop = true;
                    option.multiSelect = false;
                    option.cropOutputImageWidth = 720;
                    option.cropOutputImageHeight = 720;
                    PickImageHelper.pickImage(MineMainActivity.this, PICK_AVATAR_REQUEST, option);
                }
            }
        });
    }

    private void initview() {


        user = UserCache.getUser();
        boolean islogin = isLogin();
        if (islogin) {
            tv_name.setText(user.memberNickName);
            String sign = "";
            if (!user.memberIntroduce.isEmpty()) {
                String s = user.memberIntroduce;
                if (s.contains("\n"))
                    s = s.split("\n")[0];
                if (s.length() > 14)
                    s = s.substring(0, 12) + "...";
                sign += s + "| ";
            }
            sign += "积分 " + user.growth;
            tv_sign.setText(sign);
            iv_head.displayImage(user.memberHeadPic);
//            ((CircleImageView)iv_head).setNum(99);
            tv_login.setVisibility(View.GONE);
            tv_friend_num.setText(user.friends + "");
            tv_group_num.setText(user.groups + "");
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
            iv_head.setImageResource(R.drawable.sns_user_default);
            tv_friend_num.setText("0");
            tv_group_num.setText("0");
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
        startActivity2(DebugActivity.class);
    }

    public void handle_1(View view){
        throw new RuntimeException(mycontext.toString()
                + "测试异常1");

        //Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_2(View view) {
        throw new RuntimeException(mycontext.toString()
                + "测试异常2");
//        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_3(View view) {
        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.log(TAG, "onResume");
        initview();
        if (isLogin())
//            if (isFirstLoad) {
//                isFirstLoad = false;
            fetchUserDetails();
//            }
    }

    private void fetchUserDetails() {
        NetHelper.getInstance().getUserDetails(new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        User user = User.load(iMsg);
                        UserCache.setUser(user);
                        initview();
                    } catch (Exception e) {
                        Log.log(TAG, e);
                    }
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    public void handle_userdetail(View view) {
        startActivity(PersonalSettingActivity.class);
    }

    public void handle_friendlist(View view) {
        startActivity(FriendListActivity.class);
    }

    public void handle_friendlist_group(View view) {
        Bundle bu = new Bundle();
        bu.putInt("DATA", 3);
        startActivity(FriendListActivity.class, bu);
    }

    public void handle_circle(View view) {
        MainActivity.getInstance().selectTag(1);
    }

    private static final int PICK_AVATAR_REQUEST = 0x0E;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(com.netease.nim.uikit.session.constant.Extras.EXTRA_FILE_PATH);
            updateAvatar(path);
        }
    }

    public void updateAvatar(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);

        if (file == null) {
            return;
        }
        Log.log(TAG, "上传头像大小 = " + file.length() + "");
        Bitmap bm = Utils.getSmallBitmap(path);
        Log.log(TAG, "上传头像大小 = " + bm.getByteCount() + "");

        Map<String, File> fileBody = new HashMap<>();
        fileBody.put("headPortraits", file);
        NetHelper.getInstance().userReplaceThePicture(fileBody, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {

                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        User user = User.load(iMsg);
                        UserCache.setUser(user);
                        new SnsDBManager(mycontext).updateUser(user);
                        initview();
                        toast("头像上传成功");
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(TAG, e);
                }
            }
        });
    }

//    private static Boolean isExit = false;
//    Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            isExit = false;
//        }
//    };
//
//    private void exit() {
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            // 利用handler延迟发送更改状态信息
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            exit();
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }

}
