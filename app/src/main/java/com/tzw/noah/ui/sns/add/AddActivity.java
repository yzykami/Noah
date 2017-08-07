package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.friendlist.FansFragment;
import com.tzw.noah.ui.sns.friendlist.FollowFragment;
import com.tzw.noah.ui.sns.friendlist.FriendFragment;
import com.tzw.noah.ui.sns.friendlist.GroupFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;

/**
 * Created by yzy on 2017/6/30.
 */

public class AddActivity extends MyBaseActivity {


    @BindView(R.id.framelayout)
    FrameLayout frameLayout;
    @BindView(R.id.ll)
    LinearLayout ll;

    Context mContext = AddActivity.this;
//    private AssemblyRecyclerAdapter adapter;

    int selectPage;
    Fragment[] fragmentList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_add);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        selectPage = 0;
        fragmentList = new Fragment[2];
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
    }

    private void showFragment(int selected) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (fragmentList[selected] == null) {
            if (selected == 0)
                fragmentList[selected] = new AddFriendFragment();
            if (selected == 1)
                fragmentList[selected] = new AddGroupFragment();
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
            } else {
            }
        }
        if (clickindex == selectPage) {
            return;
        } else {
            selectPage = clickindex;
            showFragment(selectPage);
            setTag(selectPage);
        }
    }

    public void setTag(int index) {
        for (int i = 0; i < ll.getChildCount(); i++) {
            TextView child = (TextView) ll.getChildAt(i);
            if (i == index) {
                child.setTextColor(getResources().getColor(R.color.white));
                child.setBackgroundColor(getResources().getColor(R.color.transParent));
            } else {
                if (i == 0) {
                    child.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_border_left_round));
                } else if (i == ll.getChildCount() - 1) {
                    child.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_boder_right_round));
                } else {
                    child.setBackgroundColor(getResources().getColor(R.color.white));
                }
                child.setTextColor(getResources().getColor(R.color.myRed));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            finish();
        }
    }
}
