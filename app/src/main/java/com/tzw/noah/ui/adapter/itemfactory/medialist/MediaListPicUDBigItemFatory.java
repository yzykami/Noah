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

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaListPicUDBigItemFatory extends AssemblyRecyclerItemFactory<MediaListPicUDBigItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int width = 0, height = 0;

    public MediaListPicUDBigItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isListPicUDBig();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        int screenWidth = Utils.getSrceenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.bjs) * 3) / 3;
        height = width * 2 / 3;

        return new GalleryItem(R.layout.media_list_article_item_bigpic, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.iv_cover)
        SampleImageView iv_cover;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;
        @BindView(R.id.tv_pic_count)
        TextView tvPicCount;
        @BindView(R.id.iv_play_icon)
        ImageView ivPlayIcon;

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
//            if (width == 0) {
//                int screenWidth = Utils.getSrceenWidth();
//                width = (int) (screenWidth - mContext.getResources().getDimension(R.dimen.bjs) * 3) / 3;
//                height = width * 15 / 23;
//                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
//                lp.width = width;
//                lp.height = height;
//                iv_cover.setLayoutParams(lp);
//            } else {
//                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
//                lp.width = width;
//                lp.height = height;
//                iv_cover.setLayoutParams(lp);
//            }
        }

        @Override
        protected void onSetData(int i, final MediaArticle mediaArticle) {
            String ss[] = mediaArticle.appArticleImage.split(",");

            if (mediaArticle.appArticleImage.isEmpty()) {
                iv_cover.setVisibility(View.GONE);
            } else {
                iv_cover.setVisibility(View.VISIBLE);
                iv_cover.displayRoundImageSmallThumb(ss[0]);
            }
            tv_title.setText(mediaArticle.articleTitle);
            tv_time.setText(Utils.getStandardDate(mediaArticle.createTime));
            if(mediaArticle.articleCommentSum==-1)
            {
                tv_comment_count.setVisibility(View.GONE);
            }
            else {
                tv_comment_count.setVisibility(View.VISIBLE);
            }
            tv_comment_count.setText(mediaArticle.articleCommentSum + "人评");

            if (mediaArticle.isArticleTypPicGallery()) {
                tvPicCount.setVisibility(View.VISIBLE);
            }
            else
                tvPicCount.setVisibility(View.GONE);
            if(mediaArticle.isArticleTypVideo())
            {
                ivPlayIcon.setVisibility(View.VISIBLE);
            }
            else
                ivPlayIcon.setVisibility(View.GONE);
        }
    }
}
