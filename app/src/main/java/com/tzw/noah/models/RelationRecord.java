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
 * Created by yzy on 2017/8/4.
 */

public class RelationRecord implements Serializable {

    public int recordsId = 0;
    public int memberNo = 0;
    public String memberNickName = "";
    public String memberHeadPic = "";
    public int setupNo = 0;
    public String setupNickName = "";
    public String setupHeadPic = "";
    public int setAction = 0;
    public String createTime = "";


    public static List<RelationRecord> loadList(IMsg iMsg) {
        return iMsg.getModelList("informationRObj", new TypeToken<List<RelationRecord>>() {
        }.getType());
    }

    @Override
    public String toString() {
        String s = "";
        try {
            Class c = Class.forName("com.tzw.noah.models.RelationRecord");
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
