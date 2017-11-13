package com.tzw.noah.ui.adapter.itemfactory.medialist;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.models.Advertising;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.ui.home.ViewpagerFragment;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.DepthPageTransformer;
import me.xiaopan.sketchsample.widget.ZoomOutPageTransformer;

public class MediaListGalleryItemFatory extends AssemblyRecyclerItemFactory<MediaListGalleryItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private Fragment mFragment;
    private int itemSize;
    private int width, height;

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
        int screenWidth = Utils.getScreenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.bjs) * 2);
        height = width * 9 / 16;
        return new GalleryItem(R.layout.media_list_article_item_viewpage, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> implements ViewPager.OnPageChangeListener, MyViewPager.HScrollListener {
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
//                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
            viewPager.setOnPageChangeListener(this);
            viewPager.setHScrollListener(this);
            ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
            lp.height = height;
            lp.width = width;
            viewPager.setLayoutParams(lp);
        }

        @Override
        protected void onSetData(int index, final MediaArticle mediaArticle) {

            if (viewPager.getAdapter() != null)
                return;

            int count = mediaArticle.Advertisings.size();
            ll_select.removeAllViews();
            for (int i = 0; i < count; i++) {
                ImageView imageView;

                imageView = new ImageView(mContext);
                ll_select.addView(imageView);

                if (i == 0) {
                    imageView.setImageResource(R.drawable.media_viewpage_select);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dp2px(mContext, 15), Utils.dp2px(mContext, 15)));
                    imageView.setPadding(Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5));
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                } else {
                    imageView.setImageResource(R.drawable.media_viewpage_unselect);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dp2px(mContext, 10), Utils.dp2px(mContext, 15)));
                    imageView.setPadding(Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5));
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
//            if (fragmentAdapter == null) {
//            String[] filePaths = ImageOrientationCorrectTestFileGenerator.getInstance(getContext()).getFilePaths();
            fragments = new ArrayList<>();
//            MediaArticle ma = new MediaArticle();
//            ma.articleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/57231505287820252037.jpg";
//            fragments.add(ViewpagerFragment.newInstance(ma));
//            ma = new MediaArticle();
//            ma.articleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/59761505287820489701.jpg";
//            fragments.add(ViewpagerFragment.newInstance(ma));
//            ma = new MediaArticle();
//            ma.articleImage = "http://taizhouwang.oss-cn-beijing.aliyuncs.com/media/articleContent/20170913/85851505287820546460.jpg";
//            fragments.add(ViewpagerFragment.newInstance(ma));
            List<String> titles = new ArrayList<>();
//            titles.add("");
//            titles.add("");

            for (Advertising adv : mediaArticle.Advertisings) {
                titles.add("");
                MediaArticle mma = new MediaArticle();
                mma.articleImage = adv.advertImage;
                mma.articleTitle = adv.advertTitle;
                fragments.add(ViewpagerFragment.newInstance(mma));
            }
            fragmentAdapter = new FragmentViewPagerAdapter(mFragment.getChildFragmentManager(), fragments, titles);
//            }
            viewPager.setAdapter(fragmentAdapter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    // 4.0上使用
                    viewPager.setPageTransformer(true, new DepthPageTransformer());
                } else {
                    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                }
            }


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
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dp2px(mContext, 15), Utils.dp2px(mContext, 15)));
                    imageView.setPadding(Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5));
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                } else {
                    imageView.setImageResource(R.drawable.media_viewpage_unselect);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dp2px(mContext, 10), Utils.dp2px(mContext, 15)));
                    imageView.setPadding(Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 2.5f), Utils.dp2px(mContext, 5));
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.d("aaa", "gallery " + state);
            if (mMediaListListener != null) {
                boolean flag = true;
                if (state == 0)
                    flag = false;
                mMediaListListener.onGalleryPageScrollStateChanged(flag);//state == 1);
            }
        }

        @Override
        public void onHScrollChanging(boolean isScrolling) {
            if (mMediaListListener != null) {
                mMediaListListener.onGalleryPageScrollStateChanged(isScrolling);
            }
        }
    }
}
