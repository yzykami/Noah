package com.tzw.noah.ui.home;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.demoscaner.QrCodeActivity;
import com.google.zxing.demoscaner.WeChatCaptureActivity;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.cache.ChannelCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.circle.PostListFragment;
import com.tzw.noah.ui.sns.search.UserSearchActivity;

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
                if (System.currentTimeMillis() - currentShowPressedTime > 300)
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
//                fragmentAdapter.setListFragments(fragments);
//                fragmentAdapter.setTitles(titles);
                viewPager.setAdapter(fragmentAdapter);
                tabStrip.setViewPager(viewPager);
//                fragmentAdapter.notifyDataSetChanged();
//                tabStrip.notifyDataSetChanged();
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

    private void initCategory() {
//        NetHelper.getInstance().mediaCategory(new StringDialogCallback(mContext) {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                toast(getResources().getString(R.string.internet_fault));
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                try {
//                    if (iMsg.isSucceed()) {
//                        mediaCategory = MediaCategory.load(iMsg);
//                        if (fragmentAdapter == null) {
//                            ArrayList<Fragment> fragments = new ArrayList<>();
//
//                            List<String> titles = new ArrayList<>();
//
//
//                            for (int i = 0; i < mediaCategory.children.size(); i++) {
//                                String s = "哈哈";
////                                if (i % 2 == 0)
//                                s = "";
//                                titles.add(mediaCategory.children.get(i).channelName + s);
//
//                                fragments.add(new ArticleListFragment().setMediaCategory(mediaCategory.children.get(i)));
//                            }
//
//                            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
//                        }
//                        viewPager.setAdapter(fragmentAdapter);
//                        tabStrip.setViewPager(viewPager);
//                    }
//                } catch (Exception e) {
//                    Log.log(Tag, e);
//                }
//            }
//        });


        ChannelCache.getChannels(mContext, new ChannelCache.ChannelListGetter() {
            @Override
            public void onRead(List<MediaCategory> list) {
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
//                fragmentAdapter.setListFragments(fragments);
//                fragmentAdapter.setTitles(titles);
                viewPager.setAdapter(fragmentAdapter);
                tabStrip.setViewPager(viewPager);
//                fragmentAdapter.notifyDataSetChanged();
//                tabStrip.notifyDataSetChanged();
            }
        });
    }

    public void handle_search(View view) {
        Intent intent = new Intent(HomeMainActivity.this, SearchActivity.class);
        startActivity(intent);
        this.getParent().overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
//        this.getParent().overridePendingTransition(R.anim.window_push_enter, 0);
    }
}
