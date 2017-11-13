package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleDetailSafaItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailSafaItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;

    public MediaArticleDetailSafaItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isSafa();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_safa, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.tv)
        TextView tv;

        Context mContext;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            tv.setOnClickListener(new View.OnClickListener() {
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
//            if(!mediaArticle.hasReply)
//                tv.setText("暂无回复");
        }
    }
}
