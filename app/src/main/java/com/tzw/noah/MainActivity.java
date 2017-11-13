package com.tzw.noah;

import android.Manifest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.main.helper.SystemMessageUnreadManager;
import com.netease.nim.demo.main.reminder.ReminderItem;
import com.netease.nim.demo.main.reminder.ReminderManager;
import com.netease.nim.demo.main.reminder.ReminderSettings;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.tzw.noah.appupdate.UpdateManager;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.DevelopingActivity;
import com.tzw.noah.ui.circle.CirileMainActivity;
import com.tzw.noah.ui.home.HomeMainActivity;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.ui.sns.friendlist.FriendListActivity;
import com.tzw.noah.utils.SchemeUtils;
import com.tzw.noah.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.BitmapPool;
import me.xiaopan.sketch.cache.DiskCache;
import me.xiaopan.sketch.cache.MemoryCache;

public class MainActivity extends TabActivity implements ReminderManager.UnreadNumChangedCallback {

    private TabHost tabHost;

    private FrameLayout layout1, layout2, layout3, layout4, layout5;
    private List<FrameLayout> frameLayoutList = new ArrayList<>();
    private TextView tab_home_text;
    private TextView tab_sns_text;
    private TextView tab_service_text;
    private TextView tab_circle_text;
    private TextView tab_mine_text;
    private ImageView iv_home;
    private ImageView iv_circle;
    private ImageView iv_service;
    private ImageView iv_sns;
    private ImageView iv_mine;
    private ImageView iv_navi;
    private View tab1;

    private Context mContext = MainActivity.this;
    private static MainActivity instance;
    private static android.os.Handler mDelivery;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private boolean isRunning = true;

    String unreadMsg = "0";

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        printTime(this, "MainActivity");
        super.onCreate(savedInstanceState);
        instance = this;

        StatusBarUtil.transparencyBar(this);

        setContentView(R.layout.activity_main);
        mDelivery = new android.os.Handler(Looper.getMainLooper());
        requestBasicPermission();
        waitLoading();
//        registerMsgUnreadInfoObserver(true);
//        registerSystemMessageObservers(true);
//        requestSystemMessageUnreadCount();
        printTime(this, "MainActivity");

        Uri uri = getIntent().getData();
        if (uri != null) {
            Log.d("aaa-oncreate", uri.toString());
            new SchemeUtils().parse(mContext, uri);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            Log.d("aaa-newIntent", uri.toString());
            new SchemeUtils().parse(mContext, uri);
        }
    }

    private void waitLoading() {
        iv_navi = (ImageView) findViewById(R.id.iv_navi);
        iv_navi.setVisibility(View.VISIBLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initview();

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
//        new Thread(new Monitor()).start();
    }

    public void ss() {
        tabHost.getTabWidget().removeViewAt(0);
        Intent intent1 = new Intent();
        intent1.setClass(MainActivity.this, HomeMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1")
                .setContent(intent1));
        tabHost.setCurrentTabByTag("1");
    }

    private void initview() {

        tabHost = getTabHost();
        Intent intent1 = new Intent();
        intent1.setClass(MainActivity.this, HomeMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1")
                .setContent(intent1));
        Intent intent2 = new Intent();
        intent2.setClass(MainActivity.this, DevelopingActivity.class);// CirileMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2")
                .setContent(intent2));

        Intent intent3 = new Intent();
//        intent3.setClass(MainActivity.this, SnsMainActivity.class);
        intent3.setClass(MainActivity.this, com.netease.nim.demo.main.activity.MainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(intent3));

        Intent intent4 = new Intent();
        intent4.setClass(MainActivity.this, FriendListActivity.class);//ServiceMainActivity.class);//SnsMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("4").setIndicator("4").setContent(intent4));

        Intent intent5 = new Intent();
        intent5.setClass(MainActivity.this, MineMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("5").setIndicator("5").setContent(intent5));

        layout1 = (FrameLayout) findViewById(R.id.frame_home);
        layout2 = (FrameLayout) findViewById(R.id.frame_circle);
        layout3 = (FrameLayout) findViewById(R.id.frame_friend);
        layout4 = (FrameLayout) findViewById(R.id.frame_service);
        layout5 = (FrameLayout) findViewById(R.id.frame_mine);

        layout1.setOnClickListener(l);
        layout2.setOnClickListener(l);
        layout3.setOnClickListener(l);
        layout4.setOnClickListener(l);
        layout5.setOnClickListener(l);

        frameLayoutList.add(layout1);
        frameLayoutList.add(layout2);
        frameLayoutList.add(layout3);
        frameLayoutList.add(layout4);
        frameLayoutList.add(layout5);


        tab_home_text = (TextView) findViewById(R.id.tab_home_text);
        tab_circle_text = (TextView) findViewById(R.id.tab_circle_text);
        tab_service_text = (TextView) findViewById(R.id.tab_service_text);
        tab_sns_text = (TextView) findViewById(R.id.tab_friend_text);
        tab_mine_text = (TextView) findViewById(R.id.tab_mine_text);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        iv_service = (ImageView) findViewById(R.id.iv_service);
        iv_sns = (ImageView) findViewById(R.id.iv_friend);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        iv_home.setImageResource(R.drawable.tab_home_clicked_2);

        iv_navi = (ImageView) findViewById(R.id.iv_navi);
//        tab1 = (View) findViewById(R.id.tab1);
//        tab1.setVisibility(View.GONE);
//        iv_navi.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                iv_navi.setVisibility(View.GONE);
//                Bitmap bmp = ((BitmapDrawable) iv_navi.getDrawable()).getBitmap();
//                iv_navi = null;
////                bmp.recycle();
//                instance.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        }, 120);

        selectUserTag();
        mDelivery.postDelayed(new Runnable() {
            @Override
            public void run() {
                new UpdateManager(mContext).checkUpdateInfo();
            }
        }, 500);
    }


    private int login_requestcode = 100;
    View.OnClickListener l = new View.OnClickListener() {

        public void onClick(View arg0) {
            if (arg0 == layout1) {
                tabHost.setCurrentTabByTag("1");

                iv_home.setImageResource(R.drawable.tab_home_clicked_2);
                iv_circle.setImageResource(R.drawable.tab_circle_2);
                iv_service.setImageResource(R.drawable.tab_friend_2);
                iv_sns.setImageResource(R.drawable.tab_sns_2);
                iv_mine.setImageResource(R.drawable.tab_mine_2_2);


                tab_home_text.setTextColor(getResources().getColorStateList(R.color.myRed));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_sns_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));

            } else if (arg0 == layout2) {
                tabHost.setCurrentTabByTag("2");

                iv_home.setImageResource(R.drawable.tab_home_2);
                iv_circle.setImageResource(R.drawable.tab_circle_clicked_2);
                iv_service.setImageResource(R.drawable.tab_friend_2);
                iv_sns.setImageResource(R.drawable.tab_sns_2);
                iv_mine.setImageResource(R.drawable.tab_mine_2_2);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.myRed));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_sns_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));

            } else if (arg0 == layout3) {

                if (!UserCache.isLogin()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, login_requestcode);
                    return;
                }


                tabHost.setCurrentTabByTag("3");

                iv_home.setImageResource(R.drawable.tab_home_2);
                iv_circle.setImageResource(R.drawable.tab_circle_2);
                iv_sns.setImageResource(R.drawable.tab_sns_clicked_2);
                iv_service.setImageResource(R.drawable.tab_friend_2);
                iv_mine.setImageResource(R.drawable.tab_mine_2_2);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_sns_text.setTextColor(getResources().getColorStateList(R.color.myRed));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));

            } else if (arg0 == layout4) {
                if (!UserCache.isLogin()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, login_requestcode);
                    return;
                }

                tabHost.setCurrentTabByTag("4");

                iv_home.setImageResource(R.drawable.tab_home_2);
                iv_circle.setImageResource(R.drawable.tab_circle_2);
                iv_sns.setImageResource(R.drawable.tab_sns_2);
                iv_service.setImageResource(R.drawable.tab_friend_clicked_2);
                iv_mine.setImageResource(R.drawable.tab_mine_2_2);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_sns_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.myRed));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));
            } else if (arg0 == layout5) {

                tabHost.setCurrentTabByTag("5");

                updateMineActivityUnreadMsgCount();

                iv_home.setImageResource(R.drawable.tab_home_2);
                iv_circle.setImageResource(R.drawable.tab_circle_2);
                iv_service.setImageResource(R.drawable.tab_friend_2);
                iv_sns.setImageResource(R.drawable.tab_sns_2);
                iv_mine.setImageResource(R.drawable.tab_mine_clicked_2_2);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_sns_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.myRed));
            }

        }
    };

    private void selectUserTag() {
        if (UserCache.isLogin())
            tabHost.setCurrentTabByTag("3");
        tabHost.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectTag(0);
                iv_navi.setVisibility(View.GONE);
                Bitmap bmp = ((BitmapDrawable) iv_navi.getDrawable()).getBitmap();
                iv_navi = null;
//                bmp.recycle();
                instance.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }, 120);


//        iv_home.setImageResource(R.drawable.tab_home);
//        iv_circle.setImageResource(R.drawable.tab_circle);
//        iv_service.setImageResource(R.drawable.tab_service);
//        iv_sns.setImageResource(R.drawable.tab_friend);
//        iv_mine.setImageResource(R.drawable.tab_mine_clicked_2_2);
//
//        tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
//        tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
//        tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
//        tab_sns_text.setTextColor(getResources().getColorStateList(R.color.mygray));
//        tab_mine_text.setTextColor(getResources().getColorStateList(R.color.myRed));

    }

    public void selectTag(int index) {
        frameLayoutList.get(index).callOnClick();
    }


    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    public void onUnreadNumChanged(ReminderItem item) {
        ImageView indicatorView = (ImageView) findViewById(R.id.tab_new_indicator);
        final DropFake unreadTV = ((DropFake) findViewById(R.id.tab_new_msg_label));
        unreadTV.setRadius(ScreenUtil.dip2px(8));

        if (item == null || unreadTV == null || indicatorView == null) {
            unreadTV.setVisibility(View.GONE);
            indicatorView.setVisibility(View.GONE);
            return;
        }
        int unread = 0;
        if (item == null) {
            unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        } else
            unread = item.unread();

        boolean indicator = item.indicator();
        unreadTV.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
        indicatorView.setVisibility(indicator ? View.VISIBLE : View.GONE);
        if (unread > 0) {
            unreadTV.setRadius(ScreenUtil.dip2px(8));
            unreadTV.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
        }
        if (unread > 0)
            unreadMsg = unreadTV.getText();
        else
            unreadMsg = "0";
        updateMineActivityUnreadMsgCount();

    }

    private void updateMineActivityUnreadMsgCount() {
        if (tabHost.getCurrentTab() == 4) {
            View view = tabHost.getChildAt(4);
            Activity activity = getCurrentActivity();
            if (activity instanceof MineMainActivity) {
                ((MineMainActivity) activity).setNoticeMsgCount(unreadMsg);
            }
        }
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            selectTag(2);
            return;
        } else {
            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }


    private class Monitor implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Handler mDelivery = new Handler(Looper.getMainLooper());
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = "", s2 = "", s3 = "";

                        MemoryCache memoryCache = Sketch.with(getBaseContext()).getConfiguration().getMemoryCache();
                        String usedSizeFormat = Formatter.formatFileSize(getBaseContext(), memoryCache.getSize());
                        String maxSizeFormat = Formatter.formatFileSize(getBaseContext(), memoryCache.getMaxSize());
                        s1 = usedSizeFormat + "/" + maxSizeFormat;

                        BitmapPool bitmapPool = Sketch.with(getBaseContext()).getConfiguration().getBitmapPool();
                        usedSizeFormat = Formatter.formatFileSize(getBaseContext(), bitmapPool.getSize());
                        maxSizeFormat = Formatter.formatFileSize(getBaseContext(), bitmapPool.getMaxSize());
                        s2 = usedSizeFormat + "/" + maxSizeFormat;

                        DiskCache diskCache = Sketch.with(getBaseContext()).getConfiguration().getDiskCache();
                        usedSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getSize());
                        maxSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getMaxSize());
                        s3 = usedSizeFormat + "/" + maxSizeFormat;

                        tv1.setText("   " + s1);
                        tv2.setText("; " + s2);
                        tv3.setText("; " + s3);
                    }
                });
            }
        }
    }

    long firstTime, preSystime = 0;

    protected void printTime(Context context, String pre) {
        if (1 == 1)
            return;
        long currentSystime = System.currentTimeMillis();
        long totaltime, interval = 0;
        if (preSystime != 0)
            interval = currentSystime - preSystime;
        else
            firstTime = currentSystime;
        totaltime = currentSystime - firstTime;
        String msg = pre + currentSystime + " " + interval + " " + totaltime;
        preSystime = currentSystime;
        android.util.Log.d("init-time", msg);
    }
}
