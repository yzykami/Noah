package com.netease.nim.uikit.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ChoosePhotoAction extends PickImageAction2 {

    public ChoosePhotoAction() {
        super(R.drawable.sns_icon_picture, R.string.input_panel_photo, true);
    }

    @Override
    public void onClick() {
        super.onClick();
        choosePhoto();
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        sendMessage(message);
    }
}

