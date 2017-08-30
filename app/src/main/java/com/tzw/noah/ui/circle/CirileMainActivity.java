package com.tzw.noah.ui.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by yzy on 2017/6/8.
 */

public class CirileMainActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_title1)
    TextView tv_title1;
    @BindView(R.id.tv_title2)
    TextView tv_title2;
    @BindView(R.id.statusBar)
    View statusBar;

    Context mContext = CirileMainActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_main);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
        }
        setStatusBarHeight(statusBar);
    }

    private void findview() {

    }

    private void initview() {
        ArrayList<Fragment> list_fragment = new ArrayList<>();
        list_fragment.add(new PostListFragment().setMode(PostListFragment.CIRCLE));
        list_fragment.add(new BoardMainFragment());
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list_fragment);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);


        tv_title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });
        tv_title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setSelectPage(int index) {
        if (index == 0) {
            tv_title1.setTextColor(getResources().getColor(R.color.myRed));
            tv_title1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_border_left));
            tv_title2.setTextColor(getResources().getColor(R.color.white));
            tv_title2.setBackgroundDrawable(getResources().getDrawable(R.color.transParent));
        } else {
            tv_title2.setTextColor(getResources().getColor(R.color.myRed));
            tv_title2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_border_right));
            tv_title1.setTextColor(getResources().getColor(R.color.white));
            tv_title1.setBackgroundDrawable(getResources().getDrawable(R.color.transParent));

        }
    }

    public void handle_info(View view) {
    }

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
