package com.tzw.noah.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing1.demoscaner.WeChatCaptureActivity;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.cache.ChannelCache;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.home.ArticleListFragment;
import com.tzw.noah.ui.home.ChannelManagerActivity;
import com.tzw.noah.ui.home.SearchActivity;
import com.tzw.noah.ui.mine.fragment.MineCommentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/8.
 */

public class MineCommentActivity extends MyBaseActivity {


    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;

    Context mContext = MineCommentActivity.this;

    String Tag = MineCommentActivity.class.getName();
    FragmentViewPagerAdapter fragmentAdapter;

//    private static int showCount = 0;
//    private static int REQUESTCODE_CHANNEL = 10000;

//    private static long currentShowPressedTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.mine_layout_comment);
        ButterKnife.bind(this);
        setStatusBarLightMode();
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
        tv_title.setText("全部评论");
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("我发出的");
        titles.add("回复我的");
        fragments.add(new MineCommentFragment().setType(0));
        fragments.add(new MineCommentFragment().setType(1));
        fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabStrip.setViewPager(viewPager);
    }
}
