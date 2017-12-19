package com.tzw.noah.ui.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.fragment.MineCommentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/8.
 */

public class CircleMainActivity2 extends MyBaseActivity {


    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;

    Context mContext = CircleMainActivity2.this;

    String Tag = CircleMainActivity2.class.getName();
    FragmentViewPagerAdapter fragmentAdapter;

//    private static int showCount = 0;
//    private static int REQUESTCODE_CHANNEL = 10000;

//    private static long currentShowPressedTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.circle_layout_main);
        ButterKnife.bind(this);
//        setStatusBarLightMode();
        setStatusBarHeight();
        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
        }
    }

    private void findview() {
    }

    private void initview() {
        tv_title.setText("");
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("热帖");
        titles.add("圈子");
        fragments.add(new MineCommentFragment().setType(0));
        fragments.add(new MineCommentFragment().setType(1));
        fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabStrip.setViewPager(viewPager);
    }
}
