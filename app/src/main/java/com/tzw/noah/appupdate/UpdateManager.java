package com.tzw.noah.appupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tzw.noah.MainActivity;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.SecretKey;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;

import okhttp3.Call;

public class UpdateManager {
    private Context mContext;

    private String updateMsg = "是否更新？";

    // private String apkUrl =
    // "http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk";

    private String apkUrl = "http://" + NetHelper.getInstance().ip + "/download/app-debug.apk";
    private String appover = "";

    private Dialog noticeDialog;

    private Dialog downloadDialog;

    private static final String savePath = Environment
            .getExternalStorageDirectory() + "/install/";

    private static final String saveFileName = savePath + "noah.apk";

    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private boolean isForcedUpdate = false;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context, String apkUrl, String appover) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.appover = appover;
    }

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public void checkUpdateInfo() {
//        int versionCode = 0;
//        try {
//            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        final int finalVersionCode = versionCode;
//        NetHelper.getInstance().getAppVersion(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                if (iMsg.isSucceed()) {
//                    Handler mDelivery = new Handler(Looper.getMainLooper());
//
//                    String data = (String) iMsg.Data;
//                    int remoteVersionCode = 0;
//                    try {
//                        remoteVersionCode = Integer.parseInt(data);
//                    } catch (Exception e) {
//
//                    }
//                    final int finalRemoteVersionCode = remoteVersionCode;
//
//                    mDelivery.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Toast.makeText(mContext, "本地版本:" + finalVersionCode + " 最新版本:" + finalRemoteVersionCode, Toast.LENGTH_SHORT).show();
//                            if (finalRemoteVersionCode > finalVersionCode) {
//                                showNoticeDialog();
//                            }
//                        }
//                    });
//
//                }
//            }
//        });
        NetHelper.getInstance().secretKeyDetails(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    SecretKey sk = SecretKey.load(iMsg);
                    String appid = sk.secretKeyId + "";
                    if (sk.forcedUpdate == 1) {
                        isForcedUpdate = true;
                        showNoticeDialog();
                    } else if (!appid.equals(UserCache.getAppId()))
                        showNoticeDialog();
                }
            }
        });
    }

    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("更新");
        builder.setMessage(updateMsg);
        if (isForcedUpdate)
            builder.setCancelable(false);
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        if (!isForcedUpdate)
            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        try {
            noticeDialog = builder.create();
            noticeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("更新");
        if (isForcedUpdate)
            builder.setCancelable(false);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.updateprogress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.updateprogress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                if(isForcedUpdate)
                    showNoticeDialog();
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                System.out.print("savePath=" + savePath);
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };


    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    public void installApk() {
        try {
            File apkfile = new File(saveFileName);
            if (!apkfile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(mContext, "com.tzw.noah.fileprovider", new File(saveFileName));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(new File(saveFileName)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            mContext.startActivity(intent);
            MainActivity.getInstance().finish();
        } catch (Exception e) {
            Toast.makeText(mContext, "安装失败, 请从台州网官网下载最新App", Toast.LENGTH_SHORT).show();
        }
    }
}
