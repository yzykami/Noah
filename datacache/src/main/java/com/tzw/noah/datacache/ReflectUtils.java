package com.tzw.noah.datacache;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

/**
 * Created by yzy on 2017/7/21.
 */

public class ReflectUtils {

    private static Object getObject(Object o, String fieldname) {
        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (fieldname.equals(field.getName())) {
                try {
                    return field.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int getInt(Object o, String fieldname) {
        Object oo = getObject(o, fieldname);
        if (oo != null) {
            try {
                Integer.parseInt(oo.toString());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static String getStrng(Object o, String fieldname) {
        Object oo = getObject(o, fieldname);
        if (oo != null)
            return oo.toString();
        return "";
    }
}
