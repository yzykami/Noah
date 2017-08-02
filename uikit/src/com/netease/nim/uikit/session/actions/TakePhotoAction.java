package com.netease.nim.uikit.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class TakePhotoAction extends PickImageAction2 {

    public TakePhotoAction() {
        super(R.drawable.sns_icon_takephoto, R.string.input_panel_take, true);
    }

    @Override
    public void onClick() {
        super.onClick();
        takePhoto();
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        sendMessage(message);
    }
}

