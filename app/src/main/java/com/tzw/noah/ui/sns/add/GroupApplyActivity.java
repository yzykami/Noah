package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/7/5.
 */

public class GroupApplyActivity extends MyBaseActivity {


    @BindView(R.id.framelayout)
    FrameLayout frameLayout;

    Context mContext = GroupApplyActivity.this;
//    private AssemblyRecyclerAdapter adapter;

    int selectPage;
    Fragment[] fragmentList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_apply);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        selectPage = 0;
        fragmentList = new Fragment[2];
    }

    private void findview() {

    }

    private void initview() {
        showFragment(selectPage);
    }

    public void showFragment(int selected) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (fragmentList[selected] == null) {
            if (selected == 0)
                fragmentList[selected] = new GroupApplyFragment1();
            if (selected == 1)
                fragmentList[selected] = new GroupApplyFragment2();
        }
//        fragment.setArguments(getArguments(selected));
        transaction.replace(R.id.framelayout, fragmentList[selected]);
        transaction.commit();
    }

    private void doWorking() {
    }


    public void handle_select(View v) {
        int clickindex = 0;
        if (clickindex == selectPage) {
            return;
        } else {
            selectPage = clickindex;
            showFragment(selectPage);
        }
    }
}
