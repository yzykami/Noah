package com.tzw.noah.sdk;

import com.tzw.noah.db.MyField;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.WIRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/10.
 */

public class SnsManager {
    //添加关注
    //sns/attention
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
    public void snsFriends(Callback callback) {
        NetHelper.getInstance().snsFriends(callback);
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

    public <T> List<T> GetList(Class<T> t) {

        List<T> list = null;
        try {
            Class c = Class.forName(t.getName());
            Field[] fields = c.getDeclaredFields();
            for(Field field:fields)
            {
                Annotation a = field.getAnnotation(MyField.class);
                if(a==null)
                {
                    continue;
                }
                String fname = field.getName();
                Type ftype=field.getType();


            }
            T ins = t.newInstance();
            list.add(ins);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
