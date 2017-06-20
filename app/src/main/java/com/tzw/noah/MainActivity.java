package com.tzw.noah;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.tzw.noah.ui.circle.CirileMainActivity;
import com.tzw.noah.ui.friend.FriendMainActivity;
import com.tzw.noah.ui.home.HomeMainActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.ui.service.ServiceMainActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
        intent3.setClass(MainActivity.this, ServiceMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(intent3));
        Intent intent4 = new Intent();
        intent4.setClass(MainActivity.this, FriendMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("4").setIndicator("4").setContent(intent4));

        Intent intent5 = new Intent();
        intent5.setClass(MainActivity.this, MineMainActivity.class);
        tabHost.addTab(tabHost.newTabSpec("5").setIndicator("5").setContent(intent5));

        layout1 = (FrameLayout) findViewById(R.id.frame_home);
        layout2 = (FrameLayout) findViewById(R.id.frame_circle);
        layout3 = (FrameLayout) findViewById(R.id.frame_service);
        layout4 = (FrameLayout) findViewById(R.id.frame_friend);
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

            }
            else if (arg0 == layout2) {
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

            }

            else if (arg0 == layout3) {
                tabHost.setCurrentTabByTag("3");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_service.setImageResource(R.drawable.tab_service_clicked);
                iv_friend.setImageResource(R.drawable.tab_friend);
                iv_mine.setImageResource(R.drawable.tab_mine);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));
            }

            else if (arg0 == layout4) {

                tabHost.setCurrentTabByTag("4");

                iv_home.setImageResource(R.drawable.tab_home);
                iv_circle.setImageResource(R.drawable.tab_circle);
                iv_service.setImageResource(R.drawable.tab_service);
                iv_friend.setImageResource(R.drawable.tab_friend_clicked);
                iv_mine.setImageResource(R.drawable.tab_mine);

                tab_home_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_circle_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_service_text.setTextColor(getResources().getColorStateList(R.color.mygray));
                tab_friend_text.setTextColor(getResources().getColorStateList(R.color.tabcolorbg));
                tab_mine_text.setTextColor(getResources().getColorStateList(R.color.mygray));
            }
            else if (arg0 == layout5) {

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
}
