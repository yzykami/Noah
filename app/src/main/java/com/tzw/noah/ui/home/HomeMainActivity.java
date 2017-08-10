package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.circle.PostListFragment;
import com.tzw.noah.widgets.scrollablelayout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/8.
 */

public class HomeMainActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {




    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;
//    @BindView(R.id.sl_root)
//    ScrollableLayout sl_root;

    Context mContext = HomeMainActivity.this;
    FragmentViewPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
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
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(new PostListFragment());
            fragments.add(new PostListFragment());
            fragments.add(new PostListFragment());
            fragments.add(new PostListFragment());
//            fragments.add(new PostListFragment());
//            fragments.add(new BoardFragment());
//            fragments.add(new BoardFragment());
//            for (int w = 0; w < filePaths.length; w++) {
//                fragments[w] = ImageOrientationTestFragment.build(filePaths[w]);
//            }
            List<String> titles = new ArrayList<>();
            titles.add("最新");
            titles.add("排行榜");
            titles.add("台州");
            titles.add("汽车");
//            titles.add("房产");

            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        }
        viewPager.setAdapter(fragmentAdapter);
        tabStrip.setViewPager(viewPager);
//        sl_root.getHelper().setCurrentScrollableContainer((PostListFragment) fragmentAdapter.getItem(0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        sl_root.getHelper().setCurrentScrollableContainer((PostListFragment) fragmentAdapter.getItem(position));
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
