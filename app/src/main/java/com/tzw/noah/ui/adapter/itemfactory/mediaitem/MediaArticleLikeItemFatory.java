package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.StatusBarUtil;
import com.tzw.noah.utils.Utils;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class MediaArticleLikeItemFatory extends AssemblyRecyclerItemFactory<MediaArticleLikeItemFatory.GalleryItem> {

    private MyBaseActivity mActivity;
    private MediaArticleDetailListener mMediaListListener;
    private int screenWidth;
    public GalleryItem galleryItem;


    public MediaArticleLikeItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
        mActivity = (MyBaseActivity) mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isLike();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        galleryItem = new GalleryItem(R.layout.media_article_like, viewGroup);
        return galleryItem;
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.ll_member)
        LinearLayout ll_member;
        @BindView(R.id.ll_like_count)
        LinearLayout ll_like_count;
        @BindView(R.id.tv_like_num)
        TextView tvLikeNum;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_complaint)
        TextView tvComplaint;

        Context mContext;
        int isLike = 0;
        boolean isloading = false;
        MediaArticle ma;

        String Tag = "MediaArticleLikeItemFatory";
        private FrameLayout baseView;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);

            baseView = (FrameLayout) mActivity.findViewById(android.R.id.content);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            screenWidth = Utils.getScreenWidth();
        }

        @Override
        protected void onSetData(final int position, final MediaArticle mediaArticle) {
            ma = getData();
            refreshData(ma);
            final int id = mediaArticle.articleId;
            isLike = mediaArticle.isArticleEvaluate;
            ll_like_count.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onLikeClick(position, ma);
                    }
                }
            });
            ll_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onLikeMemberClick(position, ma);
                    }
                }
            });
            tvComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaListListener != null) {
                        mMediaListListener.onComplaintClick();
                    }
                }
            });
        }

        public void refreshData(MediaArticle mediaArticle) {
            isLike = mediaArticle.isArticleEvaluate;
            if (isLike == 0) {
//                Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.media_like);
//                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
//                tvLikeNum.setCompoundDrawables(leftDrawable, null, null, null);
                tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.textLightGray));
                ivLike.setImageResource(R.drawable.media_comment_like);
            } else if (isLike == 1) {
//                Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.media_like_ed);
//                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
//                tvLikeNum.setCompoundDrawables(leftDrawable, null, null, null);
                ivLike.setImageResource(R.drawable.media_comment_like_ed);
                tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.myRed));
            }
            int likenum = mediaArticle.praiseNumber;// mediaArticle.articleEvaluateObj.size();
            if (likenum > 0) {
                tvLikeNum.setText(likenum + "人点赞");
                ll_member.setVisibility(View.VISIBLE);
            } else {
                ll_member.setVisibility(View.GONE);
                tvLikeNum.setText("");
            }
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
                else {
                    String headUri = mediaArticle.articleEvaluateObj.get(i).memberHeadPic;
                    if (TextUtils.isEmpty(headUri))
                        sampleImageViewHead.displayResourceImage(R.drawable.sns_user_default);
                    else
                        sampleImageViewHead.displayImage(mediaArticle.articleEvaluateObj.get(i).memberHeadPic);
                }
                sampleImageViewHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mMediaListListener != null) {
                            mMediaListListener.onLikeMemberClick(0, ma);
                        }
                    }
                });
                ll_member.addView(sampleImageViewHead);
            }
        }

        int[] size = new int[2];

        public void setLikeAnima() {
            ivLike.setImageResource(R.drawable.media_comment_like_ed);
            tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.myRed));

            Animation anima = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
            anima.setDuration(300);
            anima.setInterpolator(new LinearInterpolator());
            tvLikeNum.startAnimation(anima);
            Animation anima2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anima2.setDuration(300);
            anima2.setInterpolator(new LinearInterpolator());
            ivLike.startAnimation(anima2);

            ivLike.getLocationInWindow(size);
            if (size[0] == 0 || size[1] == 0) {
                ivLike.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivLike.getLocationInWindow(size);
                        doPlus1();
                    }
                }, 100);
            } else
                doPlus1();
        }

        public void doPlus1() {
            if (size[0] == 0 || size[1] == 0)
                return;
            int top = mContext.getResources().getDimensionPixelOffset(R.dimen.title_height)+ getStatusBarHeight();
            int bottom = ScreenUtil.getDisplayHeight() - mContext.getResources().getDimensionPixelOffset(R.dimen.media_input_textHeight);
            if(size[1]+20<top||size[1]-20>=bottom)
                return;
            final TextView tvp1 = new TextView(mContext);
            tvp1.setText("+1");
            tvp1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvp1.setTextColor(mContext.getResources().getColor(R.color.myRed));
            tvp1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            baseView.addView(tvp1);
            TextPaint textPaint = tvp1.getPaint();
            float textPaintWidth = textPaint.measureText("+1");
            int offset = 0;
            size[0] += ivLike.getWidth() / 2 - textPaintWidth / 2;
            size[1] -= 25;
//        tvp1.layout(size[0] - ivWidth / 2, size[1], size[0] + ivWidth / 2, size[1] + tvp1.getHeight());
//                                Animation anima3 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
            AnimatorSet animatorSetGroup1 = new AnimatorSet();
            ObjectAnimator objectAnimatorScaleX1 = ObjectAnimator.ofFloat(tvp1, "scaleX", 0f, 1f);
            ObjectAnimator objectAnimatorScaleY1 = ObjectAnimator.ofFloat(tvp1, "scaleY", 0f, 1f);
            ObjectAnimator objectAnimatorRotateX1 = ObjectAnimator.ofFloat(tvp1, "translationY", size[1] - 0, size[1] - 20f);
            ObjectAnimator objectAnimatorRotateY1 = ObjectAnimator.ofFloat(tvp1, "translationX", size[0], size[0]);
            animatorSetGroup1
                    .play(objectAnimatorRotateX1)
                    .with(objectAnimatorRotateY1);
//                    .with(objectAnimatorScaleX1)
//                    .with(objectAnimatorScaleY1);
            animatorSetGroup1.setDuration(1000);
            animatorSetGroup1.start();

            tvp1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    baseView.removeView(tvp1);
                }
            }, 1200);
        }
        public int getStatusBarHeight() {
            int statusBarHeight =-1;
            if (statusBarHeight == -1) {
                // 获取状态栏高度
                //获取status_bar_height资源的ID
                int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
                }
            }
            return statusBarHeight;
        }
    }
}
