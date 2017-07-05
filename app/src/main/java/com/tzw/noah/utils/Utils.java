package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.tzw.noah.AppContext;
import com.tzw.noah.models.SnsPerson;

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

    public static int getSrceenWidth() {
        return AppContext.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getSrceenHeight() {
        return AppContext.getContext().getResources().getDisplayMetrics().heightPixels;
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


    public static List<SnsPerson> makeData()
    {
        List<String> contentlist = new ArrayList<>();
        contentlist.add("你111");
        contentlist.add("你好在");
        contentlist.add("耐111");
        contentlist.add("废柴");
        contentlist.add("风");
        contentlist.add("银");
        contentlist.add("辛巴");
        contentlist.add("2342辛巴");
        contentlist.add("啦啦");
        contentlist.add("❤啦啦");
        contentlist.add("OMG呵呵");
        contentlist.add("ddd呵呵");

        List<String> images = new ArrayList<String>();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646800199&di=e588dafd6e16678d08e8404c7f6a5651&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_2_1979295486D2113125476_23.jpg");
        images.add("http://v1.qzone.cc/avatar/201405/10/17/00/536deaa6c35a9512.jpg!200x200.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646872650&di=8c968f968b9423051048d1eec7c5d598&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_3520253239D3803949043_21.jpg");
        images.add("http://img17.3lian.com/d/file/201702/22/1005a2e0825ffe290b3f697404ee8038.jpg");
        images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=c89f6064a21ea8d38e227306a70b30cf/0824ab18972bd407ce9f04227f899e510eb30991.jpg");
        images.add("http://www.adquan.com/upload/20151223/1450838259813154.jpg");
        images.add("http://www.feizl.com/upload2007/2015_07/150720124522248.jpg");
        images.add("http://pic.iqshw.com/d/file/gexingqqziyuan/touxiang/2016/03/17/8581e320e98e541ed03a8fcab51068fd.jpg");
        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222436.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646757823&di=ceb2ef896125f0f5ead9140c5e68cef7&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-3%2F2016030111061053440.jpg");
        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222419.jpg");

        List<SnsPerson> list =new ArrayList<>();
        for (int i=0;i<contentlist.size()&&i<images.size();i++)
        {
            SnsPerson p =new SnsPerson();
            p.name=contentlist.get(i);
            p.headUrl=images.get(i);
            list.add(p);
        }
        return list;
    }
}
