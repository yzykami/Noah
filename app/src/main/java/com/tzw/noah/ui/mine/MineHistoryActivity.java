package com.tzw.noah.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.mine.fragment.MineCommentFragment;
import com.tzw.noah.ui.mine.fragment.MineHistoryFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/8.
 */

public class MineHistoryActivity extends MyBaseActivity {


    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_shadow1)
    TextView tv_shadow1;
    @BindView(R.id.tv_shadow2)
    TextView tv_shadow2;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.tv_select_all)
    TextView tv_select_all;
    @BindView(R.id.rl_delete)
    RelativeLayout rl_delete;

    Context mContext = MineHistoryActivity.this;

    String Tag = MineHistoryActivity.class.getName();
    FragmentViewPagerAdapter fragmentAdapter;

    boolean isEditMode = false;
    private boolean hasSelectAll = false;
//    private static int showCount = 0;
//    private static int REQUESTCODE_CHANNEL = 10000;

    //    private static long currentShowPressedTime = 0;
    ArrayList<Fragment> fragments;

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
        tv_right.setVisibility(View.VISIBLE);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });
    }

    private void initview() {
        tv_title.setText("历史阅读");
        fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("文章");
        titles.add("帖子");
        tv_shadow1.setText("文章");
        tv_shadow2.setText("帖子");
        fragments.add(new MineHistoryFragment().setType(0));
        fragments.add(new MineHistoryFragment().setType(1));
        fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabStrip.setViewPager(viewPager);
    }

    public void handle_edit(View view) {
        isEditMode = !isEditMode;
        if (isEditMode) {
            tv_right.setText("取消");
            rl_delete.setVisibility(View.VISIBLE);
        } else {
            tv_right.setText("编辑");
            rl_delete.setVisibility(View.GONE);
            hasSelectAll = false;
        }

        MineHistoryFragment fragment = (MineHistoryFragment) fragments.get(viewPager.getCurrentItem());
        AssemblyRecyclerAdapter adapter = fragment.getAdapter();
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isEditMode = isEditMode;
            if (isEditMode)
                list.get(i).isSelected = false;
        }
        adapter.setDataList(list);
        refreshDeleteLayout();

//        adapter.notifyDataSetChanged();
    }

    public void handle_select_all(View view) {
        hasSelectAll = !hasSelectAll;
        MineHistoryFragment fragment = (MineHistoryFragment) fragments.get(viewPager.getCurrentItem());
        AssemblyRecyclerAdapter adapter = fragment.getAdapter();
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isEditMode = isEditMode;
            if (isEditMode)
                list.get(i).isSelected = hasSelectAll;
        }
        adapter.setDataList(list);
        refreshDeleteLayout();
    }

    public void refreshDeleteLayout() {
        int count = 0;
        MineHistoryFragment fragment = (MineHistoryFragment) fragments.get(viewPager.getCurrentItem());
        AssemblyRecyclerAdapter adapter = fragment.getAdapter();
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected)
                count++;
        }
        if (count == 0) {
            tv_delete.setText("删除");
            tv_delete.setTextColor(getResources().getColor(R.color.textLightGray));
        } else {
            tv_delete.setText("删除(" + count + ")");
            tv_delete.setTextColor(getResources().getColor(R.color.textDarkGray));
        }
        if (count == list.size())
            hasSelectAll = true;
        else
            hasSelectAll = false;
        if (hasSelectAll) {
            tv_select_all.setText("取消全选");
        } else {
            tv_select_all.setText("选择全部");
        }
    }

    public void doDelete() {
        MineHistoryFragment fragment = (MineHistoryFragment) fragments.get(viewPager.getCurrentItem());
        final AssemblyRecyclerAdapter adapter = fragment.getAdapter();
        String ids = "";
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected)
                ids += list.get(i).articleId + ",";
        }
        if (TextUtils.isEmpty(ids))
            return;
        if(ids.endsWith(","))
            ids=ids.substring(0,ids.length()-1);
        NetHelper.getInstance().mediaMixDeleteHistory(ids, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
//                isloading = false;
                ((MyBaseActivity) mContext).toast(mContext.getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
//                isloading = false;
                try {
                    if (iMsg.isSucceed()) {
                        ((MyBaseActivity) mContext).toast("取消收藏成功");
                        List<MediaArticle> list = adapter.getDataList();
                        Iterator<MediaArticle> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            MediaArticle ma = iterator.next();
                            if (ma.isSelected)
                                iterator.remove();
                        }
//                        if (list.size() == 0)
//                            setEmpty();
                        adapter.setDataList(list);
                        handle_edit(null);
                    } else {
                        ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
