package com.tzw.noah.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing1.demoscaner.WeChatCaptureActivity;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.tzw.noah.MainActivity;
import com.tzw.noah.R;
import com.tzw.noah.cache.AppCache;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.home.FavoriteActivity;
import com.tzw.noah.ui.mine.setting.PersonalSettingActivity;
import com.tzw.noah.ui.mine.setting.SettingActivity;
import com.tzw.noah.ui.sns.friendlist.FriendListActivity;
import com.tzw.noah.utils.StatusBarUtil;
import com.tzw.noah.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

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
    private TextView tv_notice_count;
    private TextView tv_growth;
    private TextView tv_score;
    private User user;
    private SampleImageViewHead iv_head;

    String imageHeadUrl = "";
    SessionCustomization customization = new SessionCustomization();
    private boolean isFirstLoad = true;
    static MineMainActivity instance;
    boolean isOnResume = false;

    public static void reload() {
        if (instance != null)
            instance.initview();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_main);
        instance = this;
        StatusBarUtil.transparencyBar(this);
        setStatusBarHeight();
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        RelativeLayout rl_debug = (RelativeLayout) findViewById(R.id.rl_debug);
        rl_debug.setVisibility(View.VISIBLE);


        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_friend_num = (TextView) findViewById(R.id.tv_friend_num);
        tv_group_num = (TextView) findViewById(R.id.tv_group_num);
        tv_circle_num = (TextView) findViewById(R.id.tv_circle_num);
        tv_airtle_num = (TextView) findViewById(R.id.tv_airtle_num);
        tv_reply_num = (TextView) findViewById(R.id.tv_reply_num);
        tv_notice_count = (TextView) findViewById(R.id.tv_notice_count);
        tv_growth = (TextView) findViewById(R.id.tv_growth);
        tv_score = (TextView) findViewById(R.id.tv_score);
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
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mycontext, PersonalSettingActivity.class);
                    startActivity(intent);
                }
            });
            String sign = "";
            if (!user.memberIntroduce.isEmpty()) {
//                String s = user.memberIntroduce;
//                if (s.contains("\n"))
//                    s = s.split("\n")[0];
//                if (s.length() > 14)
//                    s = s.substring(0, 12) + "...";
//                sign += s + "| ";
                sign = user.memberIntroduce.replace("\r\n", "");
            }
//            sign += "积分 " + user.growth;
            tv_sign.setText(sign);
            if (!imageHeadUrl.equals(user.memberHeadPic)) {
                iv_head.displayImage(user.memberHeadPic);
                imageHeadUrl = user.memberHeadPic;
            }
//            ((CircleImageView)iv_head).setNum(99);
            tv_login.setVisibility(View.GONE);
            tv_friend_num.setText(user.friends + "");
            tv_group_num.setText(user.groups + "");
            tv_growth.setText(user.growth + "");
            tv_score.setText(user.totalScore + "");

        } else {
//            tv_login.setVisibility(View.VISIBLE);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mycontext, LoginActivity.class);
                    startActivity(intent);
                }
            });
            tv_name.setText("点击登录");
            tv_sign.setText("1秒登录，专享个性化服务");
            if (!imageHeadUrl.equals("")) {
                imageHeadUrl = "";
                iv_head.setImageResource(R.drawable.sns_user_default);
            }
            tv_friend_num.setText("0");
            tv_group_num.setText("0");
            tv_growth.setText("0");
            tv_score.setText("0");
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
        startActivity2(SettingActivity.class);
    }

    public void handle_dev(View view) {
        startActivity2(DebugActivity.class);
    }

    public void handle_1(View view) {
//        throw new RuntimeException(mContext.toString()
//                + "测试异常1");

        //Toast.makeText(mContext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_2(View view) {
//        throw new RuntimeException(mContext.toString()
//                + "测试异常2");
//        Toast.makeText(mContext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    public void handle_3(View view) {
        Toast.makeText(mycontext, "该功能正在研发中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.log(TAG, "onResume");
        isOnResume = true;
        if (isLogin())
//            if (isFirstLoad) {
//                isFirstLoad = false;
            fetchUserDetails();
//            }
        else
            initview();
    }

    private void fetchUserDetails() {
        NetHelper.getInstance().getUserDetails(new Callback() {
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
        if (isLogin())
            startActivity(PersonalSettingActivity.class);
        else {
            handle_login(view);
        }
    }

    public void handle_friendlist(View view) {
        FriendListActivity.selectPage = 0;
        MainActivity.getInstance().selectTag(3);
        //startActivity(FriendListActivity.class);
    }

    public void handle_friendlist_group(View view) {
        FriendListActivity.selectPage = 3;
        MainActivity.getInstance().selectTag(3);
//        Bundle bu = new Bundle();
//        bu.putInt("DATA", 3);
//        startActivity(FriendListActivity.class, bu);
    }

    public void handle_circle(View view) {
        toast("功能开发中,敬请期待~");
//        MainActivity.getInstance().selectTag(1);
    }

    public void handle_post(View view) {
        toast("功能开发中,敬请期待~");
    }

    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int QR_SCAN_REQUEST = 111;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(com.netease.nim.uikit.session.constant.Extras.EXTRA_FILE_PATH);
            updateAvatar(path);
        }
//        if (resultCode == Activity.RESULT_OK && requestCode == QR_SCAN_REQUEST) {
//            Bundle bu = data.getExtras();
//            if (bu != null) {
//                startActivity(ConfirmScanLoginActivity.class, bu);
//            }
//        }
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

    public void handle_favorite(View view) {
        startActivity(FavoriteActivity.class);
//        getParent().overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
    }

    public void handle_qrscan(View view) {
//        startActivityForResult(QR_SCAN_REQUEST, WeChatCaptureActivity.class);
        startActivity(WeChatCaptureActivity.class);
    }

    public void handle_comment(View view) {
        startActivity(MineCommentActivity.class);
    }

    public void handle_history(View view) {
        startActivity(MineHistoryActivity.class);
    }

    public void setNoticeMsgCount(String unreadMsg) {
        tv_notice_count.setText(unreadMsg);
    }


}
