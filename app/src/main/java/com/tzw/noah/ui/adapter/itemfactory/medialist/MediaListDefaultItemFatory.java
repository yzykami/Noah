package com.tzw.noah.ui.adapter.itemfactory.medialist;

import android.content.Context;
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

public class MediaListDefaultItemFatory extends AssemblyRecyclerItemFactory<MediaListDefaultItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int itemSize;

    public MediaListDefaultItemFatory(MediaListListener mMediaListListener) {
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

        return new GalleryItem(R.layout.media_list_article_item_txt, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        //        @BindView(R.id.iv_cover)
//        SampleImageView iv_cover;
        @BindView(R.id.title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        Context mContext;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
//            tv_title = (TextView) findViewById(R.id.title);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int i, final MediaArticle mediaArticle) {
            tv_title.setText(mediaArticle.articleTitle);
            tv_time.setText(Utils.getStandardDate(mediaArticle.createTime));
            if(mediaArticle.articleCommentSum==-1)
            {
                tv_comment_count.setVisibility(View.GONE);
            }
            else {
                tv_comment_count.setVisibility(View.VISIBLE);
            }
            tv_comment_count.setText(mediaArticle.articleCommentSum+"人评");
        }
    }
}
