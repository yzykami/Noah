package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class MediaArticleDatailCommentItemFactory extends AssemblyRecyclerItemFactory<MediaArticleDatailCommentItemFactory.ChatListItem> {


    private MediaArticleDetailListener onImageClickListener;
    private int itemSize;

    public MediaArticleDatailCommentItemFactory(MediaArticleDetailListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaComment) {
            return true;
        }
        return false;
    }

    @Override
    public ChatListItem createAssemblyItem(ViewGroup viewGroup) {

        return new ChatListItem(R.layout.media_comment_item, viewGroup);
    }


    public class ChatListItem extends BindAssemblyRecyclerItem<MediaComment> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_commnet)
        TextView tvCommnet;
        @BindView(R.id.tv_comment_reply1)
        TextView tvCommentReply1;
        @BindView(R.id.tv_comment_reply2)
        TextView tvCommentReply2;
        @BindView(R.id.tv_comment_reply3)
        TextView tvCommentReply3;
        @BindView(R.id.tv_seeall)
        TextView tvSeeall;
        @BindView(R.id.ll_reply)
        LinearLayout llReply;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        @BindView(R.id.iv_notify)
        ImageView ivNotify;
        @BindView(R.id.llr)
        LinearLayout llr;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        private Context mContext;

        public ChatListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);

        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            MediaComment mc = getData();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onItemClick(getAdapterPosition(), getData());
                    }
                }
            });
            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onCommentClick(getAdapterPosition(), getData());
                    }
                }
            });

        }

        @Override
        protected void onSetData(int index, MediaComment mc) {
//            imageView.setNum(i);

            List<MediaComment> replys = mc.sonList();
//            mc.repliesNumber = replys.size();

            imageView.displayImage(mc.memberHeadPic);
            tvName.setText(mc.memberNickName);
            tvCommnet.setText(mc.commentContent);
            tvTime.setText(Utils.getStandardDate(mc.createTime));
            tvCommentCount.setText(mc.repliesNumber + "");

            if(mc.isCommentDetail) {
                tvCommnet.setMaxLines(256);
                llReply.setVisibility(View.GONE);
                return;
            }

            if (replys.size() == 0) {
                llReply.setVisibility(View.GONE);
            } else {
                llReply.setVisibility(View.VISIBLE);
                for (int i = 0; i < llReply.getChildCount(); i++) {
                    TextView tv = (TextView) llReply.getChildAt(i);
                    if (i > replys.size() - 1) {
                        tv.setVisibility(View.GONE);
                    } else {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(getClickableSpan(0, replys.get(i).memberNickName.length(), replys.get(i).memberNickName + ":   " + replys.get(i).commentContent));
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
//                        tv.setText(replys.get(i).memberNickName + ":   " +replys.get(i).commentContent);
                    }
                }
            }
            if (mc.repliesNumber >= 3) {
                tvSeeall.setVisibility(View.VISIBLE);
                tvSeeall.setText("查看全部" + mc.repliesNumber + "条回复");
            }
        }


        private SpannableString getClickableSpan(final int start, final int end, final String str) {
            SpannableString spannableString = new SpannableString(str);
            //设置下划线文字
//            spannableString.setSpan(new UnderlineSpan(), 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.sp12)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置文字的单击事件
            spannableString.setSpan(new MyClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(mContext, str.substring(start, end), Toast.LENGTH_SHORT).show();
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
            }
        }
    }
}
