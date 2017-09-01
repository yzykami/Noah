package com.tzw.noah.ui.adapter.itemfactory.medialist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.utils.Utils;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaListGalleryItemFatory extends AssemblyRecyclerItemFactory<MediaListGalleryItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int itemSize;

    public MediaListGalleryItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return true;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_item, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
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
        }

        @Override
        protected void onSetData(int i, MediaArticle mediaArticle) {
            if (mediaArticle.appArticleImage.isEmpty()) {
                iv_cover.setVisibility(View.GONE);
            } else {
                iv_cover.setVisibility(View.VISIBLE);
                iv_cover.displayImageThumb(mediaArticle.appArticleImage);
            }
            tv_title.setText(mediaArticle.articleTitle);
        }

        private View getPicture() {
            float span = mContext.getResources().getDimension(R.dimen.bj);

            float sw = Utils.getSrceenWidth();

            int picNum = 3;

            int itemSize = (int) ((sw - (picNum + 1) * span) / picNum);

            SampleImageView iv = new SampleImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // fake Data
            List<String> images = Utils.getImageList();

            iv.displayImage(images.get(new Random().nextInt(images.size())));

            int nn = (int) span;

            layoutParams.width = itemSize;
            layoutParams.height = itemSize * 2 / 3;
            layoutParams.setMargins(nn, 0, 0, 0);
            iv.setLayoutParams(layoutParams);

            return iv;
        }
    }
}
