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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class MediaListPicUDItemFatory extends AssemblyRecyclerItemFactory<MediaListPicUDItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int width = 0, height = 0;

    public MediaListPicUDItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isListPicUD();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        int screenWidth = Utils.getSrceenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.bjs) * 3) / 3;
        height = width * 2 / 3;

        return new GalleryItem(R.layout.media_list_article_item_3pic, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.iv_cover)
        SampleImageView iv_cover;
        @BindView(R.id.iv_cover2)
        SampleImageView iv_cover2;
        @BindView(R.id.iv_cover3)
        SampleImageView iv_cover3;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;
        @BindView(R.id.tv_pic_count)
        TextView tvPicCount;

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
            iv_cover2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
            iv_cover3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
            if (width == 0) {
                int screenWidth = Utils.getSrceenWidth();
                width = (int) (screenWidth - mContext.getResources().getDimension(R.dimen.bjs) * 3) / 3;
                height = width * 15 / 23;
                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
                ViewGroup.LayoutParams lp2 = iv_cover2.getLayoutParams();
                lp2.width = width;
                lp2.height = height;
                iv_cover2.setLayoutParams(lp2);
                ViewGroup.LayoutParams lp3 = iv_cover3.getLayoutParams();
                lp3.width = width;
                lp3.height = height;
                iv_cover3.setLayoutParams(lp3);
            } else {
                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
                ViewGroup.LayoutParams lp2 = iv_cover2.getLayoutParams();
                lp2.width = width;
                lp2.height = height;
                iv_cover2.setLayoutParams(lp2);
                ViewGroup.LayoutParams lp3 = iv_cover3.getLayoutParams();
                lp3.width = width;
                lp3.height = height;
                iv_cover3.setLayoutParams(lp3);
            }
        }

        @Override
        protected void onSetData(int position, final MediaArticle mediaArticle) {
            String ss[] = mediaArticle.appArticleImage.split(",");
            List<SampleImageView> list = new ArrayList<>();
            list.add(iv_cover);
            list.add(iv_cover2);
            list.add(iv_cover3);

            for (int i = 0; i < list.size(); i++) {
                SampleImageView iv = list.get(i);
                if (i < ss.length) {
                    iv.setVisibility(View.VISIBLE);
                    iv.displayRoundImageSmallThumb(ss[i]);
                }
//                else
//                    iv_cover.setVisibility(View.GONE);
            }

            tv_title.setText(mediaArticle.articleTitle);
            tv_time.setText(Utils.getStandardDate(mediaArticle.createTime));
            if (mediaArticle.articleCommentSum == -1) {
                tv_comment_count.setVisibility(View.GONE);
            } else {
                tv_comment_count.setVisibility(View.VISIBLE);
            }
            tv_comment_count.setText(mediaArticle.articleCommentSum + "人评");

            if (mediaArticle.isArticleTypPicGallery()) {
                tvPicCount.setVisibility(View.VISIBLE);
            } else
                tvPicCount.setVisibility(View.GONE);
        }
    }
}
