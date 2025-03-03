package com.tzw.noah.ui.adapter.itemfactory.advertising;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.Advertising;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.webview.WebViewActivity;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;

public class AdvDetailItemFatory extends AssemblyRecyclerItemFactory<AdvDetailItemFatory.GalleryItem> {

    private MediaListListener mMediaListListener;
    private int width = 0, height = 0;
    MyBaseActivity mActivity;

    public AdvDetailItemFatory(MediaListListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
        mActivity = (MyBaseActivity) mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof Advertising)
            return true;
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        int screenWidth = Utils.getScreenWidth();
        width = (int) (screenWidth - viewGroup.getContext().getResources().getDimension(R.dimen.bjs) * 2);
        height = width * 9 / 16;

        return new GalleryItem(R.layout.media_list_article_item_bigpic, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<Advertising> {
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
                    if (getData().advertUrl.isEmpty())
                        return;
                    MediaArticle ma = new MediaArticle();
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", ma);
                    bu.putString("title", "广告");
                    ma.articleContent = getData().advertUrl;
                    mActivity.startActivity2(WebViewActivity.class, bu);
                }
            });
            if (width == 0) {
                int screenWidth = Utils.getScreenWidth();
                width = (int) (screenWidth - mContext.getResources().getDimension(R.dimen.bjs) * 2);
                height = width * 9 / 16;
                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
            } else {
                ViewGroup.LayoutParams lp = iv_cover.getLayoutParams();
                lp.width = width;
                lp.height = height;
                iv_cover.setLayoutParams(lp);
            }

            View view = container.getChildAt(0);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            int bj = (int) mContext.getResources().getDimension(R.dimen.bj);
            lp.setMargins(bj, 0, bj, 0);
            view.setLayoutParams(lp);
        }

        @Override
        protected void onSetData(int i, final Advertising mediaArticle) {
            String[] ss = ss = mediaArticle.advertImage.split(",");
            if (mediaArticle.advertImage.contains(","))
                ss = mediaArticle.advertImage.split(",");
            else if (mediaArticle.advertImage.contains(";"))
                ss = mediaArticle.advertImage.split(";");

            if (mediaArticle.advertImage.isEmpty()) {
                iv_cover.setVisibility(View.GONE);
            } else {
                iv_cover.setVisibility(View.VISIBLE);
                iv_cover.setBackgroundResource(R.color.transParent);
                iv_cover.displayRoundImageSmallThumb(ss[0]);
            }
            tv_title.setText(mediaArticle.advertTitle);
            tv_time.setText(Utils.getStandardDate(mediaArticle.startTime));
            tv_time.setVisibility(View.GONE);
//            if(mediaArticle.articleCommentSum==-1)
//            {
            tv_comment_count.setVisibility(View.GONE);
//            }
//            else {
//                tv_comment_count.setVisibility(View.VISIBLE);
//            }
//            tv_comment_count.setText(mediaArticle.articleCommentSum + "人评");
//
//            if (mediaArticle.isArticleTypPicGallery()) {
//                tvPicCount.setText(mediaArticle.articleContentImageNum+"图");
//                tvPicCount.setVisibility(View.VISIBLE);
//            }
//            else
            tvPicCount.setVisibility(View.GONE);
//            if(mediaArticle.isArticleTypeVideo())
//            {
//                ivPlayIcon.setVisibility(View.VISIBLE);
//            }
//            else
            ivPlayIcon.setVisibility(View.GONE);
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText("广告");
            tvTag.setBackgroundResource(R.drawable.bg_blue_border_round_1px);
            tvTag.setTextColor(mContext.getResources().getColor(R.color.myBlue));
        }
    }
}
