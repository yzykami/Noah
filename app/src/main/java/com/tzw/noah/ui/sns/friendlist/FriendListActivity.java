package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.add.AddActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;

/**
 * Created by yzy on 2017/6/26.
 */

public class FriendListActivity extends MyBaseActivity {


    @BindView(R.id.framelayout)
    FrameLayout frameLayout;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv_detail)
    ImageView iv_add;

    Context mContext = FriendListActivity.this;
//    private AssemblyRecyclerAdapter adapter;

    int selectPage;
    Fragment[] fragmentList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_friendlist);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        selectPage = 0;
        fragmentList = new Fragment[4];
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            selectPage = bu.getInt("DATA");
        }
    }

    private void findview() {

    }

    private void initview() {
        showFragment(selectPage);
        setTag(selectPage);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bu = new Bundle();
                if (selectPage == 3)
                    bu.putInt("DATA", 1);
                else
                    bu.putInt("DATA", 0);
                startActivity(AddActivity.class, bu);
            }
        });
    }

    private void showFragment(int selected) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (fragmentList[selected] == null) {
            if (selected == 0)
                fragmentList[selected] = new FriendFragment();
            if (selected == 1)
                fragmentList[selected] = new FollowFragment();
            if (selected == 2)
                fragmentList[selected] = new FansFragment();
            if (selected == 3)
                fragmentList[selected] = new GroupFragment();
        }
//        fragment.setArguments(getArguments(selected));
        transaction.replace(R.id.framelayout, fragmentList[selected]);
        transaction.commit();
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }

    LoadMoreItemFactory loadMoreItem;


    private void doWorking() {
    }


    public void handle_select(View v) {
        int clickindex = 0;
        for (int i = 0; i < ll.getChildCount(); i++) {
            TextView child = (TextView) ll.getChildAt(i);
            if (child.equals(v)) {
                clickindex = i;
                child.setTextColor(getResources().getColor(R.color.white));
                child.setBackgroundColor(getResources().getColor(R.color.myRed));
            } else {
                child.setBackgroundColor(getResources().getColor(R.color.white));
                child.setTextColor(getResources().getColor(R.color.myRed));
            }
        }
        if (clickindex == selectPage) {
            return;
        } else {
            selectPage = clickindex;
            showFragment(selectPage);
        }
    }

    public void setTag(int index) {
        for (int i = 0; i < ll.getChildCount(); i++) {
            TextView child = (TextView) ll.getChildAt(i);
            if (i == index) {
                child.setTextColor(getResources().getColor(R.color.white));
                child.setBackgroundColor(getResources().getColor(R.color.myRed));
            } else {
                child.setBackgroundColor(getResources().getColor(R.color.white));
                child.setTextColor(getResources().getColor(R.color.myRed));
            }
        }
    }
}
