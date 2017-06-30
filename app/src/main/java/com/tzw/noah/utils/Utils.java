package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yzy on 2017/6/8.
 */

public class Utils {
    static String tag = "Utils";

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = getScreenDensity(context);
        return (int) (dp * scale + 0.5);
    }


    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static long String2Timestamp(String dateString) {
        long timestamp = 0l;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            timestamp = date.getTime() / 1000;
            if (timestamp < 0)
                timestamp = 0l;
        } catch (ParseException e) {
            com.tzw.noah.logger.Log.log(tag, e);
        }
        return timestamp;
    }

    public static int String2Int(String idStr) {
        try {
            double d = Double.parseDouble(idStr);
            int i = (int) d;
            return i;
        } catch (Exception e) {

        }
        return 0;
    }

    public static String formatDatetime(String datetime) {
        if (datetime == null || datetime.isEmpty() || datetime.equals("0000-00-00 00:00:00")) {
            return "";
        }
        return datetime;
    }

    public static List<Character> getLetter(String word) {
        //47-57是0-9
//        65-90是A-Z
//        97-122是a-z
        List<Character> ret = new ArrayList<>();
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            char c = word.toCharArray()[i];
            if ((c >= 47 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                ret.add(c);
            } else {
                try {
                    String[] stemp = PinyinHelper.toHanyuPinyinStringArray(word.toCharArray()[i]);
                    for (int j = 0; j < stemp[0].length(); j++)
                        ret.add(stemp[0].toCharArray()[j]);
                } catch (Exception e) {
                    char a = c;
                    ret.add(a);
                }
            }
        }
//        com.tzw.noah.logger.Log.log("getLetter", "word = " + word + ",letter = " + ret.toString());
        return ret;
    }

    public static String getLetterShortCut(List<Character> pingying) {

        char ccc = pingying.get(0);
        if ((ccc >= 97 && ccc <= 122)) {
            return new String(new char[]{ccc}).toUpperCase();
        }
        return "#";
    }
}
