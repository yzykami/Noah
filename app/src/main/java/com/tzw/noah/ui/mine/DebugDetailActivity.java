package com.tzw.noah.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.fragment.CrashDetailContentFragment;
import com.tzw.noah.ui.mine.fragment.CrashDetailIndexFragment;
import com.tzw.noah.ui.mine.fragment.CrashListFragment;
import com.tzw.noah.ui.mine.fragment.DatabaseListFragment;
import com.tzw.noah.ui.mine.fragment.DatabaseTableFragment;
import com.tzw.noah.ui.mine.fragment.DatabaseTableListFragment;
import com.tzw.noah.ui.mine.fragment.NetToolFragment;
import com.tzw.noah.ui.mine.fragment.SystemCacheFragment;
import com.tzw.noah.ui.mine.fragment.SystemParamsFragment;
import com.tzw.noah.ui.mine.fragment.SystemSwitchFragment;
import com.tzw.noah.ui.mine.fragment.ThirdPartyFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/12.
 */

public class DebugDetailActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    StringAdapter adapter;

    Context mContext = DebugDetailActivity.this;
    String Tag = "DebugDetailActivity";

    String title = "调试工具";
    int type = 0;
    Object object;
    boolean isFirstLoad = true;

    List<String> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_debug_detail);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
//        selectPage = 0;
//        fragmentList = new Fragment[4];
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
            type = bu.getInt("type");
            object = bu.getSerializable("object");
        }
    }

    public Object getObject() {
        return ((DebugActivity.DataWrapper) object).data;
    }

    private void findview() {

    }

    private void initview() {
        tv_title.setText(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (type == DebugActivity.TYPE_CRASH_LIST) {
            transaction.replace(R.id.framelayout, CrashListFragment.newInstance("", ""));
        } else if (type == DebugActivity.TYPE_CRASH_DETAIL_INDEX) {
            transaction.replace(R.id.framelayout, CrashDetailIndexFragment.newInstance("", ""));
        } else if (type == DebugActivity.TYPE_CRASH_DETAIL_CONTENT) {
            transaction.replace(R.id.framelayout, CrashDetailContentFragment.newInstance("", ""));
        } else if (type == DebugActivity.TYPE_DATEBASE_LIST) {
            transaction.replace(R.id.framelayout, DatabaseListFragment.newInstance());
        } else if (type == DebugActivity.TYPE_DATEBASE_TABLE_LIST) {
            transaction.replace(R.id.framelayout, DatabaseTableListFragment.newInstance());
        } else if (type == DebugActivity.TYPE_DATEBASE_TABLE) {
            transaction.replace(R.id.framelayout, DatabaseTableFragment.newInstance());
        } else if (type == DebugActivity.TYPE_NETTOOL) {
            transaction.replace(R.id.framelayout, NetToolFragment.newInstance());
        } else if (type == DebugActivity.TYPE_SYSTEMPARAMS) {
            transaction.replace(R.id.framelayout, SystemParamsFragment.newInstance());
        } else if (type == DebugActivity.TYPE_THIRDPARTY) {
            transaction.replace(R.id.framelayout, ThirdPartyFragment.newInstance());
        } else if (type == DebugActivity.TYPE_SYSTEMCACHE) {
            transaction.replace(R.id.framelayout, SystemCacheFragment.newInstance());
        } else if (type == DebugActivity.TYPE_SYSTEMSWITCH) {
            transaction.replace(R.id.framelayout, SystemSwitchFragment.newInstance());
        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(isFirstLoad) {
//            isFirstLoad=false;
//            refreshListView();
//        }
//        else
//            refreshListView2();
    }

    private void refreshListView() {
    }

    private void refreshListView2() {
    }
}
