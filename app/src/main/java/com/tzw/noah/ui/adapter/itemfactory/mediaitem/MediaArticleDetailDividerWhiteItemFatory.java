package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleDetailDividerWhiteItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailDividerWhiteItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;

    public MediaArticleDetailDividerWhiteItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isDividerWhite();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_divider_white, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;

        Context mContext;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
        }

        @Override
        protected void onSetData(int i, MediaArticle mediaArticle) {
//            tv_tag.setText(mediaArticle.tag);
        }
    }
}
