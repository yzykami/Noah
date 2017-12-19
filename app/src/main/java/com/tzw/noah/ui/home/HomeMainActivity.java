package com.tzw.noah.ui.home;

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

import com.google.zxing1.demoscaner.QrCodeActivity;
import com.google.zxing1.demoscaner.WeChatCaptureActivity;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.cache.ChannelCache;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.home.scan.ActivityScanerCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/8.
 */

public class HomeMainActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;
    @BindView(R.id.iv_config)
    ImageView ivConfig;
    @BindView(R.id.rl_loading)
    RelativeLayout rl_loading;
    @BindView(R.id.rl_error)
    RelativeLayout rl_error;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;

    Context mContext = HomeMainActivity.this;

    String Tag = "HomeMainActivity";
    FragmentViewPagerAdapter fragmentAdapter;
    private int statusBarHeight;
    MediaCategory mediaCategory;

//    private static boolean showMode = false;

    private static int showCount = 0;
    private static int REQUESTCODE_CHANNEL = 10000;

    private static long currentShowPressedTime = 0;

    public static boolean getShowMode() {
        if (showCount >= 3 && System.currentTimeMillis() - currentShowPressedTime < 500) {
//            showMode = false;
            showCount = 0;
            return true;
        }
        return false;
    }

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
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - 3 > 300)
                    showCount = 1;
                currentShowPressedTime = System.currentTimeMillis();
                showCount++;
            }
        });
        ivConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult2(REQUESTCODE_CHANNEL, ChannelManagerActivity.class);
//                startActivity2(ChannelManagerActivity.class);
            }
        });
    }

    private void initview() {

//        tabStrip.setIndicatorWidth((int) getResources().getDimension(R.dimen.pt20));
//        sl_root.getHelper().setCurrentScrollableContainer((PostListFragment) fragmentAdapter.getItem(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_CHANNEL) {
            if (resultCode == 100) {
                ArrayList<Fragment> fragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                List<MediaCategory> mclist = ChannelCache.getChannels(mContext, null);
                for (int i = 0; i < mclist.size(); i++) {
                    titles.add(mclist.get(i).channelName);
                    fragments.add(new ArticleListFragment().setMediaCategory(mclist.get(i)));
                }
                fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
                viewPager.setAdapter(fragmentAdapter);
                tabStrip.setViewPager(viewPager);
            }
        }
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

    public void handle_qrscan(View view) {
//        startActivity(ActivityScanerCode.class);
        startActivity(WeChatCaptureActivity.class);
//        startActivity(QrCodeActivity.class);
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

    public void setLoading() {
        viewPager.setVisibility(View.GONE);
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        rl_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rl_bg.getVisibility() == View.VISIBLE) {
                    setError();
                }
            }
        }, DataCenter.INTEL_TIMEOUT);
    }

    public void setComplete() {
        viewPager.setVisibility(View.VISIBLE);
        rl_bg.setVisibility(View.GONE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
    }

    public void setError() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
        TextView btn = (TextView) rl_error.findViewById(R.id.btn_rlbg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_error.setVisibility(View.GONE);
                initCategory();
            }
        });
    }

    private void initCategory() {
        setLoading();

        ChannelCache.getChannels(mContext, new ChannelCache.ChannelListGetter() {
            @Override
            public void onRead(List<MediaCategory> list) {
                if (list == null || list.size() == 0) {
                    setError();
                    return;
                }
                setComplete();
                List<MediaCategory> mclist = new ArrayList<MediaCategory>();
                for (MediaCategory mc : list)
                    mclist.add(mc);
                ArrayList<Fragment> fragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (int i = 0; i < mclist.size(); i++) {
                    titles.add(mclist.get(i).channelName);
                    fragments.add(new ArticleListFragment().setMediaCategory(mclist.get(i)));
                }
                fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
                viewPager.setAdapter(fragmentAdapter);
                tabStrip.setViewPager(viewPager);
            }
        });
    }

    public void handle_search(View view) {
        Intent intent = new Intent(HomeMainActivity.this, SearchActivity.class);
        startActivity(intent);
        this.getParent().overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
    }
}
