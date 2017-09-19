package com.netease.nim.uikit.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.TextUtils;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TZWTeamCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.tzw_relative.Group;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * UIKit默认的用户信息提供者
 * <p>
 * Created by hzchenkang on 2016/12/19.
 */

public class DefaultUserInfoProvider implements UserInfoProvider {

    private Context context;

    public DefaultUserInfoProvider(Context context) {
        this.context = context;
    }

    @Override
    public UserInfo getUserInfo(String account) {
        UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
        if (user == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
        }

        return user;
    }

    @Override
    public int getDefaultIconResId() {
        return R.drawable.nim_avatar_default;
    }

    @Override
    public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
        String nick = null;
        if (sessionType == SessionTypeEnum.P2P) {
            nick = NimUserInfoCache.getInstance().getUserDisplayName(account);
        } else if (sessionType == SessionTypeEnum.Team) {
            nick = TeamDataCache.getInstance().getDisplayNameWithoutMe(sessionId, account);
        }
        // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
        if (TextUtils.isEmpty(nick)) {
            return null;
        }

        return nick;
    }

    @Override
    public Bitmap getAvatarForMessageNotifier(String account) {
        /*
         * 注意：这里最好从缓存里拿，如果加载时间过长会导致通知栏延迟弹出！该函数在后台线程执行！
         */
        UserInfo user = getUserInfo(account);
        return (user != null) ? NimUIKit.getImageLoaderKit().getNotificationBitmapFromCache(user.getAvatar()) : null;
    }

    @Override
    public Bitmap getTeamIcon(String teamId) {
        /*
         * 注意：这里最好从缓存里拿，如果加载时间过长会导致通知栏延迟弹出！该函数在后台线程执行！
         */
        Team team = TeamDataCache.getInstance().getTeamById(teamId);
        if (team != null) {
            Bitmap bm = NimUIKit.getImageLoaderKit().getNotificationBitmapFromCache(team.getIcon());
            if (bm != null) {
                return getOvalBitmap(bm);
            }
        }

        int resId = R.drawable.nim_avatar_group;
        Group group =null;
        if(team!=null)
            group = TZWTeamCache.getInstance().getTeamByAccount(team.getId());
        if (group != null) {
            if (group.groupAttribute == Group.Type.GROUP) {
                resId = R.drawable.nim_avatar_group;
            } else {
                resId = R.drawable.sns_discuss_default;
            }
        }
        // 默认图
        Drawable drawable = context.getResources().getDrawable(resId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        return null;
    }

    public static Bitmap getOvalBitmap(Bitmap bitmap){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
