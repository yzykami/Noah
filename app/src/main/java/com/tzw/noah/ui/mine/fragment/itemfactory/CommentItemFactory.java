package com.tzw.noah.ui.mine.fragment.itemfactory;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MyComment;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.home.HomeDetailActivity;
import com.tzw.noah.ui.home.HomeDetailGalleryActivity;
import com.tzw.noah.ui.home.HomeDetailVideoActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.MTextView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

public class CommentItemFactory extends AssemblyRecyclerItemFactory<CommentItemFactory.ChatListItem> {

    public interface MediaArticleDetailListener {
        void onItemClick(int position, Object o);

        void onCommentClick(int position, Object data);
    }

    private MediaArticleDetailListener onImageClickListener;
    private int itemSize;
    MyBaseActivity mActivity;

    public CommentItemFactory(MediaArticleDetailListener onImageClickListener, MyBaseActivity activity) {
        this.onImageClickListener = onImageClickListener;
        mActivity = (MyBaseActivity) activity;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MyComment) {
            return true;
        }
        return false;
    }

    @Override
    public ChatListItem createAssemblyItem(ViewGroup viewGroup) {

        return new ChatListItem(R.layout.mine_comment_item, viewGroup);
    }


    public class ChatListItem extends BindAssemblyRecyclerItem<MyComment> {
        @BindView(R.id.container)
        RelativeLayout container;
        @BindView(R.id.rl_comment)
        RelativeLayout rl_comment;
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_commnet)
        TextView tvCommnet;
        //        @BindView(R.id.tv_comment_reply1)
//        TextView tvCommentReply1;
//        @BindView(R.id.tv_comment_reply2)
//        TextView tvCommentReply2;
//        @BindView(R.id.tv_comment_reply3)
//        TextView tvCommentReply3;
//        @BindView(R.id.tv_seeall)
//        TextView tvSeeall;
//        @BindView(R.id.v_space)
//        View vSpace;
        @BindView(R.id.ll_reply)
        LinearLayout llReply;
        @BindView(R.id.tv_original)
        TextView tvOriginal;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        @BindView(R.id.iv_notify)
        ImageView ivNotify;
        @BindView(R.id.ll_reply_count)
        LinearLayout llReplyCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;

        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_like_count)
        TextView tvLikeCount;
        //        @BindView(R.id.tv_plus1)
//        TextView tvPlus1;
        @BindView(R.id.ll_like_count)
        LinearLayout llLikeCount;

        private Context mContext;
        FrameLayout baseView;

        public ChatListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);

            baseView = (FrameLayout) mActivity.findViewById(android.R.id.content);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onItemClick(getAdapterPosition(), getData());
                    }
                    User u = new User();
                    u.memberNickName = getData().memberNickName;
                    u.memberNo = getData().memberNo;
                    u.memberHeadPic = getData().memberHeadPic;
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", u);
                    mActivity.startActivity(PersonalActivity.class, bu);
                }
            });
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onItemClick(getAdapterPosition(), getData());
                    }
                    User u = new User();
                    u.memberNickName = getData().memberNickName;
                    u.memberNo = getData().memberNo;
                    u.memberHeadPic = getData().memberHeadPic;
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", u);
                    mActivity.startActivity(PersonalActivity.class, bu);
                }
            });
            tvCommnet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onCommentClick(getAdapterPosition(), getData());
                    }
                }
            });
            llReplyCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onCommentClick(getAdapterPosition(), getData());
                    }
                }
            });

            llLikeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (onImageClickListener != null) {
//                        onImageClickListener.onCommentLikeClick(getAdapterPosition(), getData());
//                    }
                    CommentLikeClick(getAdapterPosition(), getData());
                }
            });

            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaArticle ma = getData().articleDetails;
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", ma);
                    if (ma.isArticleTypeArticle())
                        mActivity.startActivity(HomeDetailActivity.class, bu);
                    else if (ma.isArticleTypeArticle())
                        mActivity.startActivity(HomeDetailVideoActivity.class, bu);
                    else if (ma.isArticleTypPicGallery())
                        mActivity.startActivity(HomeDetailGalleryActivity.class, bu);
                }
            });
        }

        @Override
        protected void onSetData(int index, MyComment mc) {
//            final MediaComment mc = new MediaComment();

            imageView.displayImage(mc.memberHeadPic);
            imageView.setTag(mc.articleCommentId);
            tvName.setText(mc.memberNickName);
            tvCommnet.setText(trim(mc.commentContent));
            tvTime.setText(Utils.getStandardDate(mc.createTime));
            tvCommentCount.setText(mc.repliesNumber + "");
            tvLikeCount.setText(mc.praiseNumber + "");

            if (mc.isArticleEvaluate == 1) {
                ivLike.setImageResource(R.drawable.media_comment_like_ed);
                tvLikeCount.setTextColor(mContext.getResources().getColor(R.color.myRed));
            } else {
                ivLike.setImageResource(R.drawable.media_comment_like);
                tvLikeCount.setTextColor(mContext.getResources().getColor(R.color.textLightGray));
            }
            tvOriginal.setText("[原文] " + mc.articleDetails.articleTitle);
        }


        private SpannableString getClickableSpan(final int start, final int end, final String str, final MediaComment mediaComment) {
            SpannableString spannableString = new SpannableString(str);
            //设置下划线文字
//            spannableString.setSpan(new UnderlineSpan(), 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.sp12)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置文字的单击事件
            spannableString.setSpan(new MyClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (widget != null) {
                        if (onImageClickListener != null) {
                            onImageClickListener.onItemClick(0, mediaComment);
                        }
                    } else {
                        if (onImageClickListener != null) {
                            onImageClickListener.onCommentClick(getAdapterPosition(), getData());
                        }
                    }
//                    Toast.makeText(mContext, str.substring(start, end), Toast.LENGTH_SHORT).show();
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置文字的前景色
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.myBlue)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }


        class MyClickableSpan extends ClickableSpan {

            private String content;


            public MyClickableSpan() {
            }

            public MyClickableSpan(String content) {
                this.content = content;
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
//                Intent intent = new Intent(mContext, PersonalActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("content", content);
//                intent.putExtra("bundle", bundle);
//                mContext.startActivity(intent);
//                Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
            }
        }

        public void CommentLikeClick(final int position, final MyComment mc) {
//            final MediaComment mc = new MediaComment();
            if (!mActivity.makesureLogin()) {
                return;
            }
//        if(isLike==1)
//            return;

            NetHelper.getInstance().mediaEvaluate(mc.webArticleId, mc.isArticleEvaluate == 0 ? 1 : 0, mc.articleCommentId, new StringDialogCallback(mContext, 500) {
                @Override
                public void onFailure(Call call, IOException e) {
                    ((MyBaseActivity) mContext).toast(mContext.getResources().getString(R.string.internet_fault));
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        if (iMsg.isSucceed() || iMsg.getCode() == 1204) {
                            int isLike = mc.isArticleEvaluate;
                            isLike = isLike == 0 ? 1 : 0;
                            if (iMsg.getCode() == 0) {
                                if (isLike == 1) {
                                    mc.praiseNumber++;
                                } else {
                                    mc.praiseNumber--;
                                    if (mc.praiseNumber < 0)
                                        mc.praiseNumber = 0;
                                }
                            }

                            mc.isArticleEvaluate = isLike;
                            tvLikeCount.setText(mc.praiseNumber + "");

                            if (mc.isArticleEvaluate == 1) {
                                ivLike.setImageResource(R.drawable.media_comment_like_ed);
                                tvLikeCount.setTextColor(mContext.getResources().getColor(R.color.myRed));
                                Animation anima = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                                anima.setDuration(300);
                                anima.setInterpolator(new LinearInterpolator());
//                                tvLikeCount.setAnimation(anima);
                                tvLikeCount.startAnimation(anima);
                                Animation anima2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                anima2.setDuration(300);
                                anima2.setInterpolator(new LinearInterpolator());
                                ivLike.startAnimation(anima2);

                                int[] size = new int[2];
                                ivLike.getLocationInWindow(size);
                                final TextView tvp1 = new TextView(mContext);
                                tvp1.setText("+1");
                                tvp1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                                tvp1.setTextColor(mContext.getResources().getColor(R.color.myRed));
                                tvp1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                                baseView.addView(tvp1);
                                TextPaint textPaint = tvp1.getPaint();
                                float textPaintWidth = textPaint.measureText("+1");
                                int offset = 0;
                                size[0] += ivLike.getWidth() / 2 - textPaintWidth / 2;
                                size[1] -= 18;
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
//                                        .with(objectAnimatorScaleX1)
//                                        .with(objectAnimatorScaleY1);
                                animatorSetGroup1.setDuration(1000);
                                animatorSetGroup1.start();

                                tvp1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        baseView.removeView(tvp1);
                                    }
                                }, 1200);

                            } else {
                                ivLike.setImageResource(R.drawable.media_comment_like);
                                tvLikeCount.setTextColor(mContext.getResources().getColor(R.color.textLightGray));
                            }
                        } else {
                            ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                        }
                    } catch (Exception e) {
                        Log.log("CommentLikeClick", e);
                    }
                }
            });
        }
    }

    private String trim(String commentContent) {
        String s = commentContent.trim();
        if (s.contains("\r\n"))
            s = s.replace("\r\n", "");
        if (s.contains("\n"))
            s = s.replace("\n", "");
        return s;
    }


}
