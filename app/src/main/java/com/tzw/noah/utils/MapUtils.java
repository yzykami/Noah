package com.tzw.noah.utils;

import java.net.URLDecoder;
import java.util.Map;


public class MapUtils {
    public static String getString(Map<String, Object> map, String key) {
        if (map == null)
            return "";
        Object object = map.get(key);
        if (object == null)
            return "";
        else
            return object + "";
    }

    public static String getUrlString(Map<String, Object> map, String key) {
        if (map == null)
            return "";
        Object object = map.get(key);
        if (object == null)
            return "";
        else {
            return URLDecoder.decode(object + "");
        }
    }

    public static int getInt(Map<String, Object> map, String key) {
        if (map == null)
            return 0;
        Object object = map.get(key);
        if (object == null)
            return 0;
        else {
            int num = 0;
            try {
                num = Integer.parseInt(object + "");
            } catch (Exception e) {
                try {
                    num = (int) Double.parseDouble(object + "");
                } catch (Exception e1) {

                }
            }
            return num;
        }
    }

//	public static String getID(Map<String, Object> map, String key) {
//		return api.formatId(getString(map, key));
//	}

}
