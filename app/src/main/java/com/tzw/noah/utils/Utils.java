package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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


    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static long String2Timestamp(String dateString) {
        long timestamp=0l;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            timestamp=date.getTime()/1000;
            if(timestamp<0)
                timestamp=0l;
        } catch (ParseException e) {
            com.tzw.noah.logger.Log.log(tag,e);
        }
        return timestamp;
    }

    public static int String2Int(String idStr) {
        try
        {
            double d  = Double.parseDouble(idStr);
            int i = (int )d;
            return  i;
        }
        catch (Exception e)
        {

        }
        return 0;
    }
}
