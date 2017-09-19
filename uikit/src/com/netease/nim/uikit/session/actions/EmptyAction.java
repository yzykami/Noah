package com.netease.nim.uikit.session.actions;

import com.netease.nim.uikit.LocationProvider;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class EmptyAction extends BaseAction {
    private final static String TAG = "LocationAction";

    public EmptyAction() {
        super(R.color.transparent, R.string.input_panel_empty);
    }

    @Override
    public void onClick() {

    }
}
