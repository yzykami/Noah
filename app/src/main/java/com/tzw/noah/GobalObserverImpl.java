package com.tzw.noah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.tzw_relative.GobalObserver;
import com.tzw.noah.models.User;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

/**
 * Created by yzy on 2017/7/21.
 */

public class GobalObserverImpl implements GobalObserver {
    @Override
    public void onShowUser(Context context, String acount) {
        Toast.makeText(context, "onShowUser", Toast.LENGTH_LONG).show();
        int netEaseId = Utils.String2Int(acount);

        if (netEaseId == 0) {

            Toast.makeText(context, "用户id不正确", Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User();
        user.netEaseId = netEaseId;
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", user);
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtras(bu);
        context.startActivity(intent);
    }

    @Override
    public void onShowTeam(Context context, String acount) {
        Toast.makeText(context, "onShowTeam", Toast.LENGTH_LONG).show();
    }
}
