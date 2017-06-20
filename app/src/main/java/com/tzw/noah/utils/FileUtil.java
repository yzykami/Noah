package com.tzw.noah.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.tzw.noah.AppContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;

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
        String content="";
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
            //Toast.makeText(WebviewTencentActivity.this, "成功保存到sd卡", Toast.LENGTH_LONG).show();
        }
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
//                        Log.e(tag, "close file", e);
                    }
                }
            }
            return content;
        }
        return "";
    }

    public static void saveDeviceID(String deviceId) {
        save2SdCard("noah", "config.txt", deviceId);
    }

    public static String readDeviceID() {
        return readFromSdCard("noah", "config.txt");
    }

    public static void saveInternalFile(String filename, String content) {
        FileOutputStream outputStream;
        try {
            //调用方法创建流，参数1：文件名参数2：文件类型为私有
            outputStream = AppContext.getContext().openFileOutput(filename, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_READABLE);
            //调用流的write方法
            outputStream.write(content.getBytes());
            //关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readInternalFile(String filename) {
        FileInputStream inputStream;
        String content = "";

        InputStream is = null;
        byte[] buf = new byte[1000*1024];
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
}
