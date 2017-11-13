package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaGalleryRelativeItemFactory extends AssemblyRecyclerItemFactory<MediaGalleryRelativeItemFactory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;
    private int width = 0, height = 0;

    public MediaGalleryRelativeItemFactory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return true;
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {
        int screenWidth = Utils.getScreenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.pt10) * 1) / 2;
        if (getAdapter().getDataCount() == 1)
            width = screenWidth;
        height = width * 2 / 3;
        return new GalleryItem(R.layout.media_article_relative_gallery, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        FrameLayout container;
        @BindView(R.id.iv_cover)
        SampleImageView iv_cover;
        @BindView(R.id.tv_title)
        TextView tv_title;

        Context mContext;

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
            iv_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });

            ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
            lp.width = width;
            lp.height = height;
            iv_cover.setLayoutParams(lp);
        }

        @Override
        protected void onSetData(int i, MediaArticle mediaArticle) {
            String[] ss = new String[0];
            if (mediaArticle.articleImage.contains(","))
                ss = mediaArticle.articleImage.split(",");
            else if (mediaArticle.articleImage.contains(";"))
                ss = mediaArticle.articleImage.split(";");
            if (ss.length >= 1 && !TextUtils.isEmpty(ss[0]))
                iv_cover.displayImage(ss[0]);
            tv_title.setText(mediaArticle.articleTitle);
        }
    }
}
