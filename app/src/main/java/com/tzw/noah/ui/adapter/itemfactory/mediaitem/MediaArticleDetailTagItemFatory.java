package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleDetailTagItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailTagItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;

    public MediaArticleDetailTagItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isTag();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_tag, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.tv_tag)
        TextView tv_tag;

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
            tv_tag.setText(mediaArticle.tag);
            if (mediaArticle.equals("全部回复")) {
                container.setBackgroundResource(R.color.transparent);
            }
        }
    }
}
