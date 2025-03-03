package com.netease.nim.uikit.session.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderText extends MsgViewHolderBase {

    public MsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
        layoutDirection();

        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
//        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.BLACK);
        int textColor = Color.parseColor("#666666");
        bodyTextView.setTextColor(textColor);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
    }

    private void layoutDirection() {
        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_left_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(16), ScreenUtil.dip2px(6), ScreenUtil.dip2px(10), ScreenUtil.dip2px(6));
//            bodyTextView.setPadding(ScreenUtil.dip2px(0), ScreenUtil.dip2px(6), ScreenUtil.dip2px(0), ScreenUtil.dip2px(6));
        } else {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_right_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(6), ScreenUtil.dip2px(16), ScreenUtil.dip2px(6));
//            bodyTextView.setPadding(ScreenUtil.dip2px(0), ScreenUtil.dip2px(6), ScreenUtil.dip2px(0), ScreenUtil.dip2px(6));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }
}
