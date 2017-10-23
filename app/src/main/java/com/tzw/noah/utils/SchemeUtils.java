package com.tzw.noah.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tzw.noah.R;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.User;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.home.HomeDetailActivity;
import com.tzw.noah.ui.home.HomeMainActivity;
import com.tzw.noah.ui.home.SearchActivity;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017-10-19.
 */

public class SchemeUtils {

    private Context mContext;

    //    public SchemeUtils(Context context) {
//        mContext = context;
//    }
    final String MEDIA = "media";
    final String SNS = "sns";
    final String CIRCLE = "circle";

    public void parse(Context context, Uri uri) {
        mContext = context;
        String host = uri.getHost();
        String path = uri.getPath();
        String type = "";
        List<String> params = new ArrayList<>();
        if (!TextUtils.isEmpty(path)) {
            String[] ss = path.split("/");
            if (ss.length > 1)
                type = ss[1];
            for (int i = 2; i < ss.length; i++) {
                params.add(ss[i]);
            }
        }

        if (!TextUtils.isEmpty(host)) {
            if (host.equals(MEDIA)) {
                if (type.equals("channel")) {

                }
                if (type.equals("detail")) {
                    MediaArticle ma = new MediaArticle();
                    ma.articleId = Utils.String2Int(params.get(0));
                    lanuch(HomeDetailActivity.class, ma);
                }
                if (type.equals("channel")) {

                }
            } else if (host.equals(SNS)) {
                if (type.equals("people")) {

                    User user = new User();
                    user.memberNo = Utils.String2Int(params.get(0));
                    lanuch(PersonalActivity.class, user);
                } else if (type.equals("group")) {
                    Group group = new Group();
                    group.groupId = Utils.String2Int(params.get(0));
                    lanuch(GroupDetailActivity.class, group);
                }

            } else if (host.equals(CIRCLE)) {

            }
        }
    }

    private void lanuch(Class<?> cls, Bundle bu) {
        Activity activity = (Activity) mContext;
        Intent intent = new Intent(mContext, cls);
        if (bu != null)
            intent.putExtras(bu);
        activity.startActivity(intent);
        activity.getParent().overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
    }

    private void lanuch(Class<?> cls, Object o) {
        Activity activity = (Activity) mContext;
        Intent intent = new Intent(mContext, cls);
        if (o != null) {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", (Serializable) o);
            intent.putExtras(bu);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
    }
}