package com.tzw.noah.action;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.session.actions.PickImageAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {
    OnPickedListener onPickedListener;

    public interface OnPickedListener {
        void onPicked(File file);
    }

    public void setOnPickedListener(OnPickedListener onPickedListener) {
        this.onPickedListener = onPickedListener;
    }

    public ImageAction() {
        super(R.drawable.nim_message_plus_photo_selector, R.string.input_panel_photo, true);
    }

    @Override
    protected void onPicked(File file) {
        if (onPickedListener != null)
            onPicked(file);
    }
}

