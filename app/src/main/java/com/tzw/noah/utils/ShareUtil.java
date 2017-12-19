package com.tzw.noah.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.ui.SharePopupWindow;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by yzy on 2017-12-08.
 */

public class ShareUtil implements SharePopupWindow.OnItemClickListener {
    Context mContext;
    Activity mActivity;

    String title;
    String imageUrl;
    String linkUrl;

    String[] platforms = new String[]{"WechatMoments", "Wechat", "QQ", "QZone", "SinaWeibo"};
    String[] plats = new String[]{"微信朋友圈", "微信好友", "手机QQ", "QQ空间", "新浪微博", "复制链接", "投诉"};
    int[] resIds = new int[]{R.drawable.share_icon_friend_circle,
            R.drawable.share_icon_wechat,
            R.drawable.share_icon_qq,
            R.drawable.share_icon_qq_zone,
            R.drawable.share_icon_weibo,
            R.drawable.share_icon_link,
            R.drawable.share_icon_complain
    };


    public ShareUtil(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void show() {
        SharePopupWindow spw = SharePopupWindow.create(mActivity, this);
        for (int i = 0; i < plats.length; i++) {
            spw.addItem(plats[i], resIds[i]);
        }
        spw.show();
    }

    @Override
    public void OnItemClick(int position, String title) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl("http://taizhouwang.oss-cn-beijing.aliyuncs.com/memberHeadPic/1503732300_539577.jpg");
        oks.setText("text");
        oks.setTitle("title");
        oks.setTitleUrl("http://10.0.9.2:7070/");
        oks.setUrl("http://10.0.9.2:7070/");
        if (position < platforms.length) {
            oks.setPlatform(platforms[position]);
            oks.show(mContext);
            Toast.makeText(mContext, "click " + plats[position], Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(mContext, "click " + plats[position], Toast.LENGTH_SHORT).show();
        }
    }
}
