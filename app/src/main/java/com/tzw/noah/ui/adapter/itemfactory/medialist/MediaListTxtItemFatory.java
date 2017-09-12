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

public class MediaListTxtItemFatory extends AssemblyRecyclerItemFactory<MediaListTxtItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int itemSize;

    public MediaListTxtItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isListTxt();
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
            tv_comment_count.setText(mediaArticle.readNumber+"");
        }
    }
}
