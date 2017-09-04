package com.tzw.noah.logger;

import com.tzw.noah.AppContext;

import cn.jesse.nativelogger.NLogger;
import cn.jesse.nativelogger.formatter.SimpleFormatter;
import cn.jesse.nativelogger.logger.LoggerLevel;
import cn.jesse.nativelogger.logger.base.IFileLogger;
import cn.jesse.nativelogger.util.CrashWatcher;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yzy on 2017/6/19.
 */

public class Log {
    public static void init() {
        NLogger.getInstance()
                .builder()
                .tag("APP")
                .loggerLevel(LoggerLevel.DEBUG)
                .fileLogger(true)
                .fileDirectory(AppContext.getContext().getFilesDir().getPath() + "/logs")
                .fileFormatter(new SimpleFormatter())
                .expiredPeriod(7 + 1)
//                .catchException(true, new CrashWatcher.UncaughtExceptionListener() {
//                    @Override
//                    public void uncaughtException(Thread thread, Throwable ex) {
//                        NLogger.e("uncaughtException", ex);
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                })
                .build();

        NLogger.zipLogs(new IFileLogger.OnZipListener() {
            @Override
            public void onZip(boolean succeed, String target) {
//                if (succeed)
//                    NLogger.i("zip", "succeed : " + target);
            }
        });
    }

    public static void log(String tag, String msg) {
        NLogger.d(tag, msg);
    }

    public static void log(String tag, Exception e) {
        NLogger.d(tag, e);
    }

    public static void httpcall(Request resp, String msg) {

        httpcall(resp, msg, "");
    }

    public static void httpcall(Request resp, String msg, String requestBody) {

        String ss = "--------------------------";
        String method = resp.method();
        String url = resp.url().toString();
        String header = resp.headers().toString();

        ss += "\r\nurl = " + url;
        ss += "\r\nmethod = " + method;
        ss += "\r\nheader\r\n" + header;
        if (requestBody != null && !requestBody.isEmpty())
            ss += "\r\nbody\r\n" + requestBody;
        ss += "\r\nresponse\r\n" + msg;
        NLogger.d("http-call", ss);
    }

    public static void httpcall(Request resp, Exception e) {
        httpcall(resp, e, "");
    }

    public static void httpcall(Request resp, Exception e, String requestBody) {
        String ss = "--------------------------";
        String method = resp.method();
        String url = resp.url().toString();
        String header = resp.headers().toString();

        ss += "\r\nurl = " + url;
        ss += "\r\nmethod = " + method;
        ss += "\r\nheader\r\n" + header;
        if (requestBody != null && !requestBody.isEmpty())
            ss += "\r\nbody\r\n" + requestBody;

        NLogger.d("http-exception-call", ss);
        NLogger.d("http-exception", e);
    }
}
