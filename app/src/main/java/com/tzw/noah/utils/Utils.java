package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.tzw.noah.AppContext;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.ByteArrayOutputStream;
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
//        com.tzw.systemcache.logger.Log.log("getLetter", "word = " + word + ",letter = " + ret.toString());
        return ret;
    }

    public static String getLetterShortCut(List<Character> pingying) {

        char ccc = pingying.get(0);
        if ((ccc >= 97 && ccc <= 122)) {
            return new String(new char[]{ccc}).toUpperCase();
        }
        return "#";
    }


    public static List<SnsPerson> makeData() {
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

        List<SnsPerson> list = new ArrayList<>();
        for (int i = 0; i < contentlist.size() && i < images.size(); i++) {
            SnsPerson p = new SnsPerson();
            p.name = contentlist.get(i);
            p.headUrl = images.get(i);
            list.add(p);
        }
        return list;
    }

    public static List<String> getImageList() {
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
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358314210&di=c13978e7c2007292e70c63e441dfb3d4&imgtype=0&src=http%3A%2F%2Fpic67.nipic.com%2Ffile%2F20150514%2F21036787_181947848862_2.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358314208&di=35d0bbde5e6102ce3dbda268ec17c7e4&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F09%2F64%2F39i58PICmgE.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358357689&di=1bc5e30cd999cee0b0ca4fba4e7dc966&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2Ff1d7bdf9d72a6059e78e5fcf2834349b013bbaa6.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358428996&di=cdda43084cf31ca96448dd504fb58624&imgtype=0&src=http%3A%2F%2Fbizhi.zhuoku.com%2F2013%2F05%2F23%2Fxiaoqingxin%2Fxiaoqingxin021.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1226504803,606513985&fm=27&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=4b3dbebc9b004df6361129ff7b66ca85&src=http://cdn.duitang.com/uploads/item/201404/07/20140407205935_RfBQQ.jpeg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=28c19605a014fa6f0b0541da82156bd4&src=http://dl.bizhi.sogou.com/images/2012/03/16/43517.jpg");
        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3308758682,671689542&fm=27&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=0d71b4ac734645e7ad8d2d0ab9948b6c&src=http://pic1.win4000.com/wallpaper/b/5488fa61b7d6a.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358595922&di=190cf56ca643a7507664afa4c317627f&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201201%2F25%2F20120125215809_wWLBV.thumb.600_0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358595921&di=dd7d1b87850c0601e8a3d502a805dfcd&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201412%2F04%2F20141204205535_GWBCH.thumb.700_0.jpeg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358646705&di=ab83c91ebd58d2e171ab59ad1446d7ec&imgtype=jpg&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F6159252dd42a28348dcd92c952b5c9ea14cebf8d.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358655703&di=e1962f210d72420015387a68a64be6ea&imgtype=jpg&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9825bc315c6034a83986a140c21349540823768e.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358646527&di=7085916b2b8f8c6c9cafda79d69a9c91&imgtype=0&src=http%3A%2F%2Fimg.article.pchome.net%2F00%2F28%2F93%2F86%2Fpic_lib%2Fwm%2Feurope038.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358712323&di=d23256855a12f14a848222abc98e2137&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Ff%2F52665cb5a0b5d.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358792608&di=a7ca66ae5bd2ef326c4bb539635f0c14&imgtype=0&src=http%3A%2F%2Fimg22.mtime.cn%2Fup%2F2010%2F07%2F14%2F093346.94291758_o.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358829280&di=50e016857977dc6b7024948bc3218ec5&imgtype=0&src=http%3A%2F%2Fpic23.nipic.com%2F20120809%2F10436117_161557283110_2.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358843244&di=a1bce78535b6683f3670b286c0aff9a1&imgtype=0&src=http%3A%2F%2Fpic12.nipic.com%2F20110103%2F5089137_130401034157_2.jpg");
        return images;
    }

    public static List<User> processUser(List<User> items) {
        for (int i = 0; i < items.size(); i++) {
            User u = items.get(i);

            u.namePingyin = Utils.getLetter(u.getName());
            u.nameFirstChar = Utils.getLetterShortCut(u.namePingyin);
        }
        return items;
    }

    public static List<User> processUser2(List<User> items, String tag) {
        for (int i = 0; i < items.size(); i++) {
            User u = items.get(i);

            u.namePingyin = Utils.getLetter(u.getName());
            u.nameFirstChar = tag;
        }
        return items;
    }

    public static List<User> processUserStar(List<User> items) {
        List<User> starList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            User u = items.get(i);
            if(u.ifStar==0) {
                User clone = User.Clone(u);
                clone.nameFirstChar = "星标";
                starList.add(clone);
            }
        }
        starList.addAll(items);
        return starList;
    }

    public static void listAdd(List list, List list1) {
        if (list == null)
            return;
        if (list1 == null || list1.size() == 0)
            return;
        list.addAll(list1);
    }


    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 400, 400);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();

            bm = BitmapFactory.decodeByteArray(b, 0, b.length);
//            Log.d("debug", b.length+"");
//
//            baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
//            b=baos.toByteArray();
//
//            bm=BitmapFactory.decodeByteArray(b, 0, b.length);
            // Log.d("debug", "第二遍  = "+b.length+"");
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // Out.print("reqWidth=" + reqWidth + ",reqHeight=" + reqHeight);
        // Out.print("width=" + width + ",height=" + height);

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
}
