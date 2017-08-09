package com.tzw.noah.ui.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.adapter.itemfactory.CircleListItemFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.FragmentArrayPagerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.psts.PagerSlidingTabStrip;
import me.xiaopan.sketchsample.activity.MainActivity;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.fragment.ImageOrientationTestFragment;
import me.xiaopan.sketchsample.util.ImageOrientationCorrectTestFileGenerator;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/29.
 */
public class BoardMainFragment extends Fragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip tabStrip;


    String Tag = "BoardMainFragment";
    Context mContext;
    List<User> items = new ArrayList<>();

    CirileMainActivity activity;
    BoardMainFragment instance;
    FragmentViewPagerAdapter fragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.circle_board_main_fragment, container, false);
        ButterKnife.bind(this, view);
        instance = this;


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fragmentAdapter == null) {
//            String[] filePaths = ImageOrientationCorrectTestFileGenerator.getInstance(getContext()).getFilePaths();
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(new BoardFragment());
            fragments.add(new BoardFragment());
            fragments.add(new BoardFragment());
            fragments.add(new BoardFragment());
            fragments.add(new BoardFragment());
//            fragments.add(new BoardFragment());
//            fragments.add(new BoardFragment());
//            for (int w = 0; w < filePaths.length; w++) {
//                fragments[w] = ImageOrientationTestFragment.build(filePaths[w]);
//            }
            List<String> titles = new ArrayList<>();
            titles.add("推荐");
            titles.add("美食");
            titles.add("情感");
            titles.add("娱乐");
            titles.add("汽车");
//            titles.add("房产");
//            titles.add("体育");
            fragmentAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        }
        viewPager.setAdapter(fragmentAdapter);

        tabStrip.setViewPager(viewPager);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (CirileMainActivity) context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
//        if(activity.firstLoad())
//        refreshListView();
//        else
//            refreshListView2();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsMyList(new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
//                        items = DataCenter.getInstance().getFriendList();
//                        items = Utils.processUser(items);
//                        Collections.sort(items, new MyCompare());
//                        adapter = new FriendAdapter(mContext, items);

                    } else {
                        activity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void refreshListView2() {
//        items = DataCenter.getInstance().getFriendList();
//        items = Utils.processUser(items);
//        Collections.sort(items, new MyCompare());
//        adapter = new FriendAdapter(mContext, items);
////        list_view.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
