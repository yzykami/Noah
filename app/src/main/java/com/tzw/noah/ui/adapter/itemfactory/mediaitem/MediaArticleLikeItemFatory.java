package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

public class MediaArticleLikeItemFatory extends AssemblyRecyclerItemFactory<MediaArticleLikeItemFatory.GalleryItem> {


    private MediaArticleDetailListener mMediaListListener;
    private int screenWidth;


    public MediaArticleLikeItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isLike();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_like, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.ll_member)
        LinearLayout ll_member;
        @BindView(R.id.tv_like_num)
        TextView tvLikeNum;
        @BindView(R.id.tv_complaint)
        TextView tvComplaint;

        Context mContext;
        int isLike = 0;
        boolean isloading = false;
        MediaArticle ma;

        String Tag = "MediaArticleLikeItemFatory";

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            screenWidth = Utils.getSrceenWidth();
        }

        @Override
        protected void onSetData(final int position, final MediaArticle mediaArticle) {
            ma = getData();
            refreshData(ma);
            final int id = mediaArticle.articleId;
            isLike = mediaArticle.isArticleEvaluate;
            tvLikeNum.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mMediaListListener!=null)
                    {
                        mMediaListListener.onLikeClick(position,ma);
                    }
                }
            });
            ll_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mMediaListListener!=null)
                    {
                        mMediaListListener.onLikeMemberClick(position,ma);
                    }
                }
            });
        }

        public void refreshData(MediaArticle mediaArticle) {
            isLike = mediaArticle.isArticleEvaluate;
            if (isLike == 0) {
                Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.media_like);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                tvLikeNum.setCompoundDrawables(leftDrawable, null, null, null);
                tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.textLightGray));
            } else if (isLike == 1) {
                Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.media_like_ed);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                tvLikeNum.setCompoundDrawables(leftDrawable, null, null, null);
                tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.myRed));
            }
            int likenum = mediaArticle.praiseNumber;// mediaArticle.articleEvaluateObj.size();
            if (likenum > 0)
                tvLikeNum.setText(likenum + "人点赞");
            else
                tvLikeNum.setText("");
            ll_member.removeAllViews();
            int bj = (int) mContext.getResources().getDimension(R.dimen.bj);
            int bj2 = 0;
            int count = 8;
            int imageSize = (int) mContext.getResources().getDimension(R.dimen.head_media);
            while (bj2 <= 0) {
                bj2 = (screenWidth - imageSize * count - bj * 2) / (count - 1);
                count--;
            }

            for (int i = 0; i < mediaArticle.articleEvaluateObj.size() && i <= count; i++) {
                SampleImageViewHead sampleImageViewHead = new SampleImageViewHead(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageSize, imageSize);
                lp.setMargins(0, 0, bj2, 0);
                sampleImageViewHead.setLayoutParams(lp);
                sampleImageViewHead.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (i == count && likenum > count)
                    sampleImageViewHead.displayResourceImage(R.drawable.media_like_more);
                else
                    sampleImageViewHead.displayImage(mediaArticle.articleEvaluateObj.get(i).memberHeadPic);
                sampleImageViewHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mMediaListListener!=null)
                        {
                            mMediaListListener.onLikeMemberClick(0,ma);
                        }
                    }
                });
                ll_member.addView(sampleImageViewHead);
            }
        }
    }
}
