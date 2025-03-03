package com.tzw.noah.utils;

/**
 * Created by yzy on 2017/6/8.
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.tzw.noah.ui.MyBaseActivity;

/**
 * <h3>全局捕获异常</h3>
 * <br>
 * 当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

    public static String TAG = "MyCrash";
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public static String startPrefix = "--CRASH_LOG_START--";

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        autoClear(7);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        if (!handleException(ex) && mDefaultHandler != null) {
//             如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {
        handleException(ex);
        SystemClock.sleep(500);
//            Application.finishAllActivities();
        // 退出程序
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);

        MyBaseActivity.exit();
//        try {
//            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
//            method.invoke(am, "com.tzw.noah");
//        } catch (Exception e) {
//            Toast.makeText(mContext, e.getMessage(),
//                    Toast.LENGTH_LONG).show();
//        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息
            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将关闭.",
                            Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            // 收集设备参数信息
//            collectDeviceInfo(mContext);
            // 保存日志文件
            com.tzw.noah.logger.Log.log("Crash", ex.getMessage());
            saveCrashInfoFile(ex);
//            SystemClock.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());

            sb.append(startPrefix + "\r\n" + date + "\r\n");
            //getDeviceInfo();

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result + "\r\n");

            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    public String getDeviceInfo() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        return sb.toString();
    }

    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".log";
        if (FileUtil.hasSdcard()) {
            String path = getGlobalpath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    public static String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "crash" + File.separator;
    }

    public static void setTag(String tag) {
        TAG = tag;
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(int autoClearDay) {
//        FileUtil.delete(getGlobalpath(), new FilenameFilter() {
//
//            @Override
//            public boolean accept(File file, String filename) {
//                String s = FileUtil.getFileNameWithoutExtension(filename);
//                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
//                String date = "crash-" + DateUtil.getOtherDay(day);
//                return date.compareTo(s) >= 0;
//            }
//        });

        File f = new File(getGlobalpath());
        if (!f.exists())
            return;
        File[] files = f.listFiles();// 列出所有文件
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            if (ff.isFile()) {
                String filename = ff.getName();
                String fileday = getFileDay(filename);
                if (needDelete(fileday, autoClearDay)) {
                    ff.delete();
                }
            }
        }
    }

    private boolean needDelete(String fileday, int autoClearDay) {
        if (fileday.equals("")) {
            return false;
        } else {
            Date now = new Date();
            String clearDay = formatter.format(new Date(now.getTime() - autoClearDay * 24 * 60 * 60 * 1000));
            return fileday.compareTo(clearDay) < 0;
        }
    }

    private String getFileDay(String filename) {
        if (filename.startsWith("crash-") && filename.length() >= 20) {
            return filename.substring(6, filename.length() - 4);
        }
        return "";
    }

}
