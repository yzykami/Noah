package com.tzw.noah.ui.adapter.itemfactory.medialist;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.circle.PostListFragment;
import com.tzw.noah.ui.home.ViewpagerFragment;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.MyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaListGalleryItemFatory extends AssemblyRecyclerItemFactory<MediaListGalleryItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private Fragment mFragment;
    private int itemSize;

    public MediaListGalleryItemFatory(MediaListListener mMediaListListener) {
        mFragment = (Fragment) mMediaListListener;
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isListViewpager();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_list_article_item_viewpage, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> implements ViewPager.OnPageChangeListener {
        @BindView(R.id.container)
        RelativeLayout container;
        @BindView(R.id.viewPager)
        MyViewPager viewPager;
        @BindView(R.id.ll_select)
        LinearLayout ll_select;

        Context mContext;
        FragmentViewPagerAdapter fragmentAdapter;
        ArrayList<Fragment> fragments;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
            viewPager.setOnPageChangeListener(this);
        }

        @Override
        protected void onSetData(int index, final MediaArticle mediaArticle) {

            int count =3;// fragmentAdapter.getCount();
            ll_select.removeAllViews();
            for (int i = 0; i < count; i++) {
                ImageView imageView;

                imageView = new ImageView(mContext);
                ll_select.addView(imageView);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dp2px(mContext, 10), Utils.dp2px(mContext, 15)));
                imageView.setPadding(0, Utils.dp2px(mContext, 5), 0, Utils.dp2px(mContext, 5));

                if (i == 0) {
                    imageView.setImageResource(R.drawable.media_viewpage_select);
                } else
                    imageView.setImageResource(R.drawable.media_viewpage_unselect);
            }
//            if (fragmentAdapter == null) {
//            String[] filePaths = ImageOrientationCorrectTestFileGenerator.getInstance(getContext()).getFilePaths();
                fragments = new ArrayList<>();
                MediaArticle ma = new MediaArticle();
                ma.appArticleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/57231505287820252037.jpg";
                fragments.add(ViewpagerFragment.newInstance(ma));
                ma = new MediaArticle();
                ma.appArticleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/59761505287820489701.jpg";
                fragments.add(ViewpagerFragment.newInstance(ma));
                ma = new MediaArticle();
                ma.appArticleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/85851505287820546460.jpg";
                fragments.add(ViewpagerFragment.newInstance(ma));
                List<String> titles = new ArrayList<>();
                titles.add("");
                titles.add("");
                titles.add("");
                fragmentAdapter = new FragmentViewPagerAdapter(mFragment.getChildFragmentManager(), fragments, titles);
//            }
            viewPager.setAdapter(fragmentAdapter);


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int count = 3;//viewPager.();
            for (int i = 0; i < count; i++) {
                ImageView imageView = (ImageView) ll_select.getChildAt(i);

                if (i == position) {
                    imageView.setImageResource(R.drawable.media_viewpage_select);
                } else
                    imageView.setImageResource(R.drawable.media_viewpage_unselect);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
