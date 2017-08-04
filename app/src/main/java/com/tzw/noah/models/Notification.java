package com.tzw.noah.models;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.AppContext;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.Utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/18.
 */

public class Notification implements Serializable {

    public String memberNickName = "";
    public String memberHeadPic = "";
    public int memberNo;
    public int groupId;
    public String groupName = "";

    public int sourceType;
    public int sourceNo;
    public String sourceNickName = "";
    public String sourceMemberHeadPic = "";
    public String requestInfo;
    public int handleType;
    public int handleNo;
    public String handleNickName = "";
    public String handleMemberHeadPic = "";
    public String handleInfo = "";
    public String handleTime = "";
    public String createTime = "";
    public int notificationType = 0;

    public static List<Notification> loadList(IMsg iMsg) {
        List<Notification> list = new ArrayList<>();
        List<Notification> list1 = load1(iMsg);
        List<Notification> list2 = load2(iMsg);

        for (int i = 0; i < list2.size(); i++) {
            list2.get(i).notificationType = 1;
        }
        Utils.listAdd(list, list1);
        Utils.listAdd(list, list2);
        return list;
    }

    public static List<Notification> load1(IMsg iMsg) {
        return iMsg.getModelList("groupNotificationRObj", new TypeToken<List<Notification>>() {
        }.getType());
    }

    public static List<Notification> load2(IMsg iMsg) {
        return iMsg.getModelList("groupNotificationManagerRObj", new TypeToken<List<Notification>>() {
        }.getType());
    }

    @Override
    public String toString() {
        String s = "";
        try {
            Class c = Class.forName("com.tzw.noah.models.Notification");
            Field[] fields = c.getDeclaredFields();
            Context context = AppContext.getContext();
            for (Field field : fields) {
                if (field.get(this) != null)
                    s += field.getName() + "" + field.get(this).toString() + "\r\n";
            }
        } catch (Exception e) {

        }
        return s;
    }
}
