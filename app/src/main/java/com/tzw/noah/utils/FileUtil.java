package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.tzw.noah.AppContext;
import com.tzw.noah.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by yzy on 2017/6/8.
 */

public class FileUtil {


    static String tag = "FileUtil";

    public static boolean hasSdcard() {
        //// TODO: 2017/6/8  
        return true;
    }

    public static String readRawFile(Context context, int id) {
        String content = "";
        Resources resources = context.getResources();
        InputStream is = null;
        try {
            is = resources.openRawResource(id);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            content = new String(buffer);
            Log.i(tag, "read:" + content);
        } catch (IOException e) {
            Log.e(tag, "write file", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(tag, "close file", e);
                }
            }
        }
        return content;
    }

    public static void save2SdCard(String dir, String filename, String content) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String sdCardDir = Environment.getExternalStorageDirectory().getPath();//获取SDCard目录
            String dd = sdCardDir + File.separator + dir;
            File foder = new File(dd);
            if (!foder.exists()) {

                System.out.println(foder.getPath());
                foder.mkdirs();
            }
            try {
                File sdFile = new File(dd, filename);
                System.out.println(sdFile.getPath());
                if (!sdFile.exists()) {
                    sdFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeBytes(content);
                fos.close(); // 关闭输出流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            com.tzw.noah.logger.Log.log("save2sdcard", "succeed save file = " + filename);
        } else
            com.tzw.noah.logger.Log.log("save2sdcard", "fail save file = " + filename);
    }

    public static String readFromSdCard(String dir, String filename) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String sdCardDir = Environment.getExternalStorageDirectory().getPath();//获取SDCard目录
            String dd = sdCardDir + File.separator + dir;
            File foder = new File(dd);
            if (!foder.exists()) {
                return "";
            }
            File sdFile = new File(dd, filename);
            ObjectInputStream ois = null;
            String content = "";
            try {
                FileInputStream fis = new FileInputStream(sdFile);   //获得输入流
                ois = new ObjectInputStream(fis);
                byte buffer[] = new byte[ois.available()];
                ois.read(buffer);
                content = new String(buffer);
                ois.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
//                        Log.e(tv_time, "close file", e);
                    }
                }
            }
            return content;
        }
        return "";
    }

    public static void saveDeviceID(String deviceId) {
        save2SdCard("systemcache", "config.txt", deviceId);
    }

    public static String readDeviceID() {
        return readFromSdCard("systemcache", "config.txt");
    }

    public static void saveInternalFile(String filename, String content) {
        FileOutputStream outputStream;
        try {
            //调用方法创建流，参数1：文件名参数2：文件类型为私有
            outputStream = AppContext.getContext().openFileOutput(filename, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_READABLE);
            //调用流的write方法
            outputStream.write(content.getBytes());
            //关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyDBFromRaw(Context context, int id, String dbname,String dbDir) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/data/data/");
            stringBuffer.append(context.getPackageName());
            stringBuffer.append("/databases");
            if(!dbDir.isEmpty())
                stringBuffer.append("/"+dbDir);
            File dir = new File(stringBuffer.toString());
            if (!dir.exists()) {//防止databases文件夹不存在，不然，会报ENOENT (No such file or directory)的异常
                dir.mkdirs();
            }
            stringBuffer.append("/");
            stringBuffer.append(dbname);
            File file = new File(stringBuffer.toString());
            if (file == null || !file.exists()) {//数据库不存在，则进行拷贝数据库的操作。
                inputStream = context.getResources().openRawResource(id);
                outputStream = new FileOutputStream(file.getAbsolutePath());
                byte[] b = new byte[1024];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    outputStream.write(b, 0, length);
                }
                //写完后刷新
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {//关闭流，释放资源
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyDBFromRaw() {
        copyDBFromRaw(AppContext.getContext(), R.raw.systemcache, "systemcache.db","");
    }

    public static void copySnsDBFromRaw(String dbDir) {
        copyDBFromRaw(AppContext.getContext(), R.raw.sns, "sns.db",dbDir);
    }

    public static String readInternalFile(String filename) {
        FileInputStream inputStream;
        String content = "";

        InputStream is = null;
        byte[] buf = new byte[1000 * 1024];
        int len = 0;
        try {
            is = AppContext.getContext().openFileInput(filename);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            content = new String(byteArrayOutputStream.toByteArray());
        } catch (Throwable e) {
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
        }

        return content;
    }


    public static void save2InternalSdCard(String dir, String filename, String content) {

        String path = AppContext.getContext().getExternalFilesDir(dir).getPath();//获取SDCard内部存储目录,
        String dd = path;
        try {
            File sdFile = new File(dd, filename);
            System.out.println(sdFile.getPath());
            if (!sdFile.exists()) {
//                    sdFile.delete();
                sdFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(sdFile);
            byte[] bytes = content.getBytes("utf-8");
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close(); // 关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        com.tzw.noah.logger.Log.log("save2sdcard", "succeed save file = " + dd);

    }

    public static String readFromInternalSdCard(String dir, String filename) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String path = AppContext.getContext().getExternalFilesDir(dir).getPath();//获取SDCard目录
            String dd = path;//+ File.separator + dir;

            File sdFile = new File(dd, filename);
            FileInputStream fis = null;
            String content = "";
            try {
                fis = new FileInputStream(sdFile);   //获得输入流
                int len = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                content = new String(byteArrayOutputStream.toByteArray(), "utf-8");
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
            com.tzw.noah.logger.Log.log("read", content);
            return content;
        }
        return "";
    }

}
