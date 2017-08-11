package com.tzw.noah.ui.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.widgets.scrollablelayout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/8.
 */

public class CirileDetailActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;
    @BindView(R.id.sl_root)
    ScrollableLayout sl_root;

    Context mContext = CirileDetailActivity.this;
    FragmentViewPagerAdapter fragmentAdapter;
    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_layout_detail);
        ButterKnife.bind(this);

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


        if (fragmentAdapter == null) {
//            String[] filePaths = ImageOrientationCorrectTestFileGenerator.getInstance(getContext()).getFilePaths();
            fragments = new ArrayList<>();
            fragments.add(new PostListFragment().setMode(PostListFragment.CIRCLEDETAIL));
            fragments.add(new PostListFragment().setMode(PostListFragment.CIRCLEDETAIL));
//            fragments.add(new BoardFragment());
//            fragments.add(new BoardFragment());
//            for (int w = 0; w < filePaths.length; w++) {
//                fragments[w] = ImageOrientationTestFragment.build(filePaths[w]);
//            }
            List<String> titles = new ArrayList<>();
            titles.add("最新发表");
            titles.add("最新回复");
            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        }
        viewPager.setAdapter(fragmentAdapter);
        tabStrip.setOnPageChangeListener(this);
        tabStrip.setViewPager(viewPager);
        sl_root.getHelper().setCurrentScrollableContainer((PostListFragment) fragments.get(0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        sl_root.getHelper().setCurrentScrollableContainer((PostListFragment) fragments.get(position));
        setSelectPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setSelectPage(int index) {
    }

    public void handle_more(View view) {
    }
}
