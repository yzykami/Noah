package com.tzw.noah.ui.home;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.circle.PostListFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/8.
 */

public class HomeMainActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;
//    @BindView(R.id.sl_root)
//    ScrollableLayout sl_root;

    Context mContext = HomeMainActivity.this;
    String Tag = "HomeMainActivity";
    FragmentViewPagerAdapter fragmentAdapter;
    private int statusBarHeight;

    MediaCategory mediaCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.home_main);
        ButterKnife.bind(this);
        initdata();
        findview();
        initview();
        initCategory();
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

//        tabStrip.setIndicatorWidth((int) getResources().getDimension(R.dimen.pt20));
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

    private void initCategory() {
        NetHelper.getInstance().mediaCategory(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        mediaCategory = MediaCategory.load(iMsg);
                        if (fragmentAdapter == null) {
                            ArrayList<Fragment> fragments = new ArrayList<>();

                            List<String> titles = new ArrayList<>();


                            for (int i = 0; i < mediaCategory.children.size(); i++) {
                                String s = "哈哈";
//                                if (i % 2 == 0)
                                    s = "";
                                titles.add(mediaCategory.children.get(i).channelName + s);

                                fragments.add(new ArticleListFragment().setMediaCategory(mediaCategory.children.get(i)));
                            }

                            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
                        }
                        viewPager.setAdapter(fragmentAdapter);
                        tabStrip.setViewPager(viewPager);
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
