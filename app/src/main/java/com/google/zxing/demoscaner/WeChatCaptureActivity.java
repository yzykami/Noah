package com.google.zxing.demoscaner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.tzw.noah.R;
import com.tzw.noah.ui.mine.ConfirmScanLoginActivity;
import com.tzw.noah.ui.mine.ScanResultActivity;
import com.tzw.noah.utils.Base64Coder;

/**
 * 模仿微信的扫描界面
 */
public class WeChatCaptureActivity extends BaseCaptureActivity {

    private static final String TAG = WeChatCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.cover_view);
//        String json = "{\"action\":\"qrScanLogin\",\"result\":{\"clientCode\":\"188E75DFEEFDD7AC5F4D59E5C9D917BC\"}}";
//        QrResult qrResult = QrResult.load(json);
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
