package com.google.zxing1.demoscaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing1.Result;
import com.google.zxing1.client.android.AutoScannerView;
import com.google.zxing1.client.android.BaseCaptureActivity;
import com.google.zxing1.demoscaner.decode.DecodeManager;
import com.tzw.noah.R;
import com.tzw.noah.ui.mine.ConfirmScanLoginActivity;
import com.tzw.noah.ui.mine.ScanResultActivity;
import com.tzw.noah.utils.Base64Coder;
import com.tzw.noah.utils.StatusBarUtil;

/**
 * 模仿微信的扫描界面
 */
public class WeChatCaptureActivity extends BaseCaptureActivity {

    private static final String TAG = WeChatCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private View statusBar;
    private int statusBarHeight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_capture);
        setTranslucentLightMode();
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.cover_view);
//        String json = "{\"action\":\"qrScanLogin\",\"result\":{\"clientCode\":\"188E75DFEEFDD7AC5F4D59E5C9D917BC\"}}";
//        QrResult qrResult = QrResult.load(json);
    }

    public void setTranslucentLightMode()
    {
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        setStatusBarHeight();
    }

    public void setStatusBarHeight() {
        if (statusBar == null)
            statusBar = findViewById(R.id.statusBar);
        if (statusBar == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = getStatusBarHeight();
            statusBar.setLayoutParams(layoutParams);
        }
    }

    public int getStatusBarHeight() {
        if (statusBarHeight == -1) {
            // 获取状态栏高度
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    public void handle_back(View view)
    {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
//        Log.i(TAG, "dealDecode ~~~~~ " + rawResult.getText() + " " + barcode + " " + scaleFactor);
        playBeepSoundAndVibrate(true, true);
//        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG).show();
//        对此次扫描结果不满意可以调用
//        reScan();
//        new DecodeManager().showResultDialog(this, rawResult.getText(), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                reScan();
//            }
//        });
        dellResultCode(rawResult.getText());
    }

    public void dellResultCode(String rawText) {
        com.tzw.noah.logger.Log.log("aaa", rawText);
        boolean isHandle = false;
        try {
            Uri uri = Uri.parse(rawText);
            if (uri != null) {
                String qrcodeValue = uri.getQueryParameter("qrcode");
                com.tzw.noah.logger.Log.log("aaa", qrcodeValue);

                String json = Base64Coder.decodeStr(qrcodeValue);
                com.tzw.noah.logger.Log.log("aaa", json);

                QrResult qrResult = QrResult.load(json);
                if (qrResult != null) {
                    if (qrResult.action.equals("qrScanLogin")) {
                        String code = qrResult.result.getAsJsonObject().get("clientCode").getAsString();
                        Intent intent = new Intent(WeChatCaptureActivity.this, ConfirmScanLoginActivity.class);
                        intent.putExtra("code", code);
//                        setResult(Activity.RESULT_OK, intent);
                        startActivity(intent);
                        isHandle = true;
                        this.finish();
                    }
                }
            }
        } catch (Exception e) {
            isHandle = false;
            com.tzw.noah.logger.Log.log("parseQrCode", e.getMessage());
        }
        if (!isHandle) {
//            Toast.makeText(this, "扫描结果 = " + rawText, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WeChatCaptureActivity.this, ScanResultActivity.class);
            intent.putExtra("code", rawText);
            startActivity(intent);
            this.finish();
        }
    }


}

class QrResult {
    public String action = "";
    public JsonElement result;

    public static QrResult load(String json) {
        Gson gson = new GsonBuilder().create();
        try {
            JsonParser parser = new JsonParser();
            JsonElement el = parser.parse(gson.toJson(json));

            QrResult qrResult = gson.fromJson(json, new TypeToken<QrResult>() {
            }.getType());
            return qrResult;
        } catch (Exception e) {
            com.tzw.noah.logger.Log.log("QrResult", e);
            return null;
        }
    }
}
