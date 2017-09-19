package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaArticleDetailTitleItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailTitleItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;

    public MediaArticleDetailTitleItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isTitle();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_title_item, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_author)
        TextView tv_author;

        Context mContext;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
//            container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mMediaListListener != null) {
//                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
//                    }
//                }
//            });
        }

        @Override
        protected void onSetData(int i, MediaArticle mediaArticle) {
            tv_title.setText(mediaArticle.articleTitle);
            if(TextUtils.isEmpty(mediaArticle.articleAuthor))
                mediaArticle.articleAuthor="作者不详";
            tv_author.setText(mediaArticle.articleAuthor + "  " + Utils.getStandardDate(mediaArticle.createTime));
        }
    }
}
