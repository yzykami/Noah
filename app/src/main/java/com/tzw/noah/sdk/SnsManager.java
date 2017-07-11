package com.tzw.noah.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tzw.noah.db.MyField;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.WIRequest;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.NetWorkUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/7/10.
 */

public class SnsManager {
    //添加关注
    //sns/attention
    Context mContext;
    SnsDBManager snsDBManager;

    private static Handler mdelivery;

    public SnsManager(Context mContext) {
        this.mContext = mContext;
        snsDBManager = new SnsDBManager(mContext);
        mdelivery = new Handler(Looper.getMainLooper());
    }


    private IMsg createImsg() {
        IMsg imsg = new IMsg();
        imsg.setSucceed(true);
        return imsg;
    }

    public void snsAttention(List<Param> body, Callback callback) {
        NetHelper.getInstance().snsAttention(body, callback);
    }

    //取消关注
    //sns/unfollow/{memberNo}
    public void snsUnfollow(String memberNo, Callback callback) {
        NetHelper.getInstance().snsUnfollow(memberNo, callback);
    }

    //移除粉丝
    //sns/removeFans/{memberNo}
    public void snsRemoveFans(String memberNo, Callback callback) {
        NetHelper.getInstance().snsRemoveFans(memberNo, callback);
    }

    //添加黑名单
    //sns/blacklist
    public void snsBlacklist(String userid, Callback callback) {
        NetHelper.getInstance().snsBlacklist(userid, callback);
    }

    //移除黑名单
    //sns/removeBlacklist/{memberNo}
    public void snsRemoveBlacklist(String memberNo, Callback callback) {
        NetHelper.getInstance().snsRemoveBlacklist(memberNo, callback);
    }

    //获取我的好友列表
    //sns/friends
    public void snsFriends(final Callback callback) {
        Log.log("SnsManager:snsFriends", "isNetWorkConnected = " + MyBaseActivity.isNetWorkConnected);
        if (NetWorkUtils.isNetworkAvailable(mContext))
            NetHelper.getInstance().snsFriends(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onAfter();
                        callback.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(IMsg iMsg) {
                    try {
                        //保存到本地数据库
                        List<User> userList = User.loadFriendList(iMsg);
                        iMsg.Data = userList;
                        snsDBManager.UpdateFriendList(userList);
                    } catch (Exception e) {

                    }
                    callback.onAfter();
                    callback.onResponse(iMsg);
                }
            });
        else {
            if (callback != null) {
                new Runnable() {
                    @Override
                    public void run() {
                        final IMsg iMsg = createImsg();
                        iMsg.Data = snsDBManager.getSnsFriendList();
                        mdelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onAfter();
                                callback.onResponse(iMsg);
                            }
                        });
                    }
                };
            }
        }
    }


    //获取我的关注列表
    //sns/concern
    public void snsConcern(Callback callback) {
        NetHelper.getInstance().snsConcern(callback);
    }

    //获取我的粉丝列表
    //sns/fans
    public void snsFans(Callback callback) {
        NetHelper.getInstance().snsFans(callback);
    }

    //获取我的黑名单列表
    //sns/blacks
    public void snsBlacks(Callback callback) {
        NetHelper.getInstance().snsBlacks(callback);
    }

    //获取我的好友,关注,粉丝,黑名单列表
    //sns/myList
    public void snsMyList(Callback callback) {
        NetHelper.getInstance().snsMyList(callback);
    }
}
