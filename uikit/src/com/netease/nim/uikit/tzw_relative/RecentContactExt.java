package com.netease.nim.uikit.tzw_relative;

import com.netease.nimlib.sdk.msg.model.RecentContact;

/**
 * Created by yzy on 2017/7/24.
 */

public class RecentContactExt {
    RecentContact recentContact;

    int no;
    int nimId;
    String name = "";
    String remarkName = "";

    public String getDisplayName() {

        if (remarkName.isEmpty())
            return name;
        return remarkName;
    }
}
