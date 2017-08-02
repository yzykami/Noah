package com.tzw.noah;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.permission.BaseMPermission;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.tzw.noah.ui.circle.CirileMainActivity;
import com.tzw.noah.ui.friend.FriendMainActivity;
import com.tzw.noah.ui.home.HomeMainActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.ui.service.ServiceMainActivity;
import com.tzw.noah.ui.sns.SnsMainActivity;

import java.util.logging.Handler;

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    private FrameLayout layout1, layout2, layout3, layout4, layout5;
    private TextView tab_home_text;
    private TextView tab_friend_text;
    private TextView tab_service_text;
    private TextView tab_circle_text;
    private TextView tab_mine_text;
    private ImageView iv_home;
    private ImageView iv_circle;
    private ImageView iv_service;
    private ImageView iv_friend;
    private ImageView iv_mine;
    private ImageView iv_navi;
    private View tab1;

    private Context mContext = MainActivity.this;
    private MainActivity instance;
    private static android.os.Handler mDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        mDelivery = new android.os.Handler(Looper.getMainLooper());
        requestBasicPermission();
        waitLoading();

    }

    private void waitLoading() {
        iv_navi = (ImageView) findViewById(R.id.iv_navi);
        iv_navi.setVisibility(View.GONE);
        initview();

    }

    private void initview() {

        tabHost = getTabHost();
        Intent intent1 = new Intent();
        intent1.setClass(MainActivity.this, HomeMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1")
                .setContent(intent1));
        Intent intent2 = new Intent();
        intent2.setClass(MainActivity.this, CirileMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2")
                .setContent(intent2));

        Intent intent3 = new Intent();
//        intent3.setClass(MainActivity.this, SnsMainActivity.class);
        intent3.setClass(MainActivity.this, com.netease.nim.demo.main.activity.MainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(intent3));

        Intent intent4 = new Intent();
        intent4.setClass(MainActivity.this, SnsMainActivity.class);//ServiceMainActivity.class);
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


        tab_home_text = (TextView) findViewById(R.id.tab_home_text);
        tab_circle_text = (TextView) findViewById(R.id.tab_circle_text);
        tab_service_text = (TextView) findViewById(R.id.tab_service_text);
        tab_friend_text = (TextView) findViewById(R.id.tab_friend_text);
        tab_mine_text = (TextView) findViewById(R.id.tab_mine_text);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        iv_service = (ImageView) findViewById(R.id.iv_service);
        iv_friend = (ImageView) findViewById(R.id.iv_friend);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        iv_home.setImageResource(R.drawable.tab_home_clicked);

        iv_navi = (ImageView) findViewById(R.id.iv_navi);
//        tab1 = (View) findViewById(R.id.tab1);
//        tab1.setVisibility(View.GONE);
        iv_navi.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_navi.setVisibility(View.GONE);
//                tab1.setVisibility(View.VISIBLE);
//                mdelivery.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        final String[] BASIC_PERMISSIONS = new String[]{
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.CAMERA,
//                                Manifest.permission.RECORD_AUDIO,
//                                Manifest.permission.READ_PHONE_STATE,
//                                Manifest.permission.INTERNET,
//                                Manifest.permission.ACCESS_NETWORK_STATE,
//                                Manifest.permission.ACCESS_WIFI_STATE,
//                                Manifest.permission.CHANGE_WIFI_STATE,
//                                Manifest.permission.ACCESS_FINE_LOCATION,
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.WRITE_SETTINGS,
//                                Manifest.permission.VIBRATE,
//                                Manifest.permission.WAKE_LOCK,
//                                Manifest.permission.BLUETOOTH,
//                                Manifest.permission.BLUETOOTH_ADMIN,
//                                Manifest.permission.CHANGE_CONFIGURATION,
//                                Manifest.permission.MODIFY_AUDIO_SETTINGS
//                        };
//                        BaseMPermission.getDeniedPermissions(instance, BASIC_PERMISSIONS);
//                    }
//                });
            }
        }, 1500);

        selectUserTag();
    }


    View.OnClickListener l = new View.OnClickListener() {

        public void onClick(View arg0) {
            if (arg0 == layout1) {
                tabHost.setCurrentTabByTag("1");

                iv_home.setImageResource(R.drawable.tab_home_clicked);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_service.setImageResource(R.drawable.tab_service);
                iv_friend.setImageResource(R.drawable.tab_friend);
                iv_mine.setImageResource(R.drawable.tab_mine);


                tab_home_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));

            } else if (arg0 == layout2) {
                tabHost.setCurrentTabByTag("2");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle_clicked);
                iv_service.setImageResource(R.drawable.tab_service);
                iv_friend.setImageResource(R.drawable.tab_friend);
                iv_mine.setImageResource(R.drawable.tab_mine);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));

            } else if (arg0 == layout3) {

                tabHost.setCurrentTabByTag("3");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_friend.setImageResource(R.drawable.tab_friend_clicked);
                iv_service.setImageResource(R.drawable.tab_service);
                iv_mine.setImageResource(R.drawable.tab_mine);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));
            } else if (arg0 == layout4) {
                tabHost.setCurrentTabByTag("4");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_friend.setImageResource(R.drawable.tab_friend);
                iv_service.setImageResource(R.drawable.tab_service_clicked);
                iv_mine.setImageResource(R.drawable.tab_mine);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));
            } else if (arg0 == layout5) {

                tabHost.setCurrentTabByTag("5");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_service.setImageResource(R.drawable.tab_service);
                iv_friend.setImageResource(R.drawable.tab_friend);
                iv_mine.setImageResource(R.drawable.tab_mine_clicked);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
            }

        }
    };

    private void selectUserTag() {
        tabHost.setCurrentTabByTag("5");

        iv_home.setImageResource(R.drawable.tab_home);
        iv_circle.setImageResource(R.drawable.tab_circle);
        iv_service.setImageResource(R.drawable.tab_service);
        iv_friend.setImageResource(R.drawable.tab_friend);
        iv_mine.setImageResource(R.drawable.tab_mine_clicked);

        tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
        tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
        tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
        tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
        tab_mine_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));

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
}
