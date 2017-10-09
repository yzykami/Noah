package com.tzw.noah.ui.adapter.itemfactory.advertising;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Advertising;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class AdvListPicUDItemFatory extends AssemblyRecyclerItemFactory<AdvListPicUDItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int width = 0, height = 0, ml = 0;

    public AdvListPicUDItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof Advertising)
            return ((Advertising) o).isUD();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        int screenWidth = Utils.getSrceenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.bjs) * 3) / 3;
        height = width * 2 / 3;
        ml = (int) viewGroup.getContext().getResources().getDimension(R.dimen.bjs) / 2;


        return new GalleryItem(R.layout.media_list_article_item_3pic, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<Advertising> {
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
        @BindView(R.id.tv_tag)
        TextView tvTag;

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
                ml = (int) mContext.getResources().getDimension(R.dimen.bjs) / 2;

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) iv_cover2.getLayoutParams();
                lp2.width = width;
                lp2.height = height;
                lp2.setMargins(ml, 0, 0, 0);
                iv_cover2.setLayoutParams(lp2);
                LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) iv_cover3.getLayoutParams();
                lp3.width = width;
                lp3.height = height;
                lp3.setMargins(ml, 0, 0, 0);
                iv_cover3.setLayoutParams(lp3);
            } else {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) iv_cover2.getLayoutParams();
                lp2.width = width;
                lp2.height = height;
                lp2.setMargins(ml, 0, 0, 0);
                iv_cover2.setLayoutParams(lp2);
                LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) iv_cover3.getLayoutParams();
                lp3.width = width;
                lp3.height = height;
                lp3.setMargins(ml, 0, 0, 0);
                iv_cover3.setLayoutParams(lp3);
            }
        }

        @Override
        protected void onSetData(int position, final Advertising mediaArticle) {
            String[] ss = new String[0];
            if (mediaArticle.advertImage.contains(","))
                ss = mediaArticle.advertImage.split(",");
            else if (mediaArticle.advertImage.contains(";"))
                ss = mediaArticle.advertImage.split(";");
            List<SampleImageView> list = new ArrayList<>();
            list.add(iv_cover);
            list.add(iv_cover2);
            list.add(iv_cover3);

            for (int i = 0; i < list.size(); i++) {
                SampleImageView iv = list.get(i);
                if (i < ss.length) {
                    iv.setVisibility(View.VISIBLE);
                    iv.displayRoundImageSmallThumb(ss[i]);
                } else
                    iv.displayResourceImage(R.drawable.media_default_pic);
//                    iv_cover.setVisibility(View.GONE);
            }

            tv_title.setText(mediaArticle.advertTitle);
            tv_time.setText(Utils.getStandardDate(mediaArticle.startTime));
            tv_time.setVisibility(View.GONE);
//            if (mediaArticle.articleCommentSum == -1) {
            tv_comment_count.setVisibility(View.GONE);
//            } else {
//                tv_comment_count.setVisibility(View.VISIBLE);
//            }
//            tv_comment_count.setText(mediaArticle.articleCommentSum + "人评");
//
//            if (mediaArticle.isArticleTypPicGallery()) {
//                tvPicCount.setText(mediaArticle.articleContentImageNum + "图");
//                tvPicCount.setVisibility(View.VISIBLE);
//            } else
            tvPicCount.setVisibility(View.GONE);
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText("广告");
            tvTag.setBackgroundResource(R.drawable.bg_blue_border_round_1px);
            tvTag.setTextColor(mContext.getResources().getColor(R.color.myBlue));
        }
    }
}
