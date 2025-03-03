package me.xiaopan.sketchsample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.FragmentArrayPagerAdapter;
import me.xiaopan.psts.PagerSlidingTabStrip;
import me.xiaopan.sketchsample.BaseFragment;
import me.xiaopan.sketchsample.BindContentView;
import com.tzw.noah.R;
import me.xiaopan.sketchsample.activity.MainActivity;

@BindContentView(R.layout.fragment_pager_tab)
public class ImageShaperTestFragment extends BaseFragment {
    @BindView(R.id.tab_pagerTabFragment_tabs)
    PagerSlidingTabStrip tabStrip;

    @BindView(R.id.pager_pagerTabFragment_content)
    ViewPager viewPager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(new FragmentArrayPagerAdapter(getChildFragmentManager(), new Fragment[]{
                new RoundRectImageShaperTestFragment(),
                new CircleImageShaperTestFragment(),
                new ShapeSizeImageShaperTestFragment(),
        }));

        tabStrip.setTabViewFactory(new MainActivity.TitleTabFactory(new String[]{
                "ROUND_RECT",
                "CIRCLE",
                "SHAPE_SIZE",
        }, getActivity()));
        tabStrip.setViewPager(viewPager);
    }
}