package com.tzw.noah.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.Notification;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.notification.NotificationAdapter;
import com.tzw.noah.ui.sns.notification.NotificationCompare;
import com.tzw.noah.ui.sns.notification.NotificationDetailActivity;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.ShareUtil;
import com.tzw.noah.widgets.MyWebView;
import com.tzw.noah.widgets.VideoEnabledWebChromeClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class WebViewActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.webView)
    MyWebView webView;


    Context mContext = WebViewActivity.this;
    WebViewActivity instance;
    String Tag = "WebViewActivity";

    String title = "";
    String htmlContent = "";
    MediaArticle mediaArticle;
    private VideoEnabledWebChromeClient webChromeClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.bind(this);
        setStatusBarLightMode();
        instance = this;
        initdata();
        findview();
        initview();

    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
            mediaArticle = (MediaArticle) bu.getSerializable("DATA");
            htmlContent = mediaArticle.getContentString();
//            title = mediaArticle.articleTitle;
        }
        if (title.isEmpty())
            title = "网页浏览";
    }

    private void findview() {

    }

    private void initview() {
        tv_title.setText(title);
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
//        String htmlContent = FileUtil.readRawFile(mContext, R.raw.videohtml);
//        webView.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
        webView.loadUrl(mediaArticle.getContentString());
//        webView.
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            }
        });

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                startActivity(intent);
            }
        });

        abc();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    void abc() {
        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
                if (progress == 100) {
                    webView.loadUrl(getOutCss("file:///android_asset/video.css"));
                }
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {

                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                    setTheme(R.style.AppTheme_NoActionBarBlack);
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
    }

    public static String getOutCss(String url) {

        String js = "";
        js = "javascript:var d=document;" +
                "var s=d.createElement('link');" +
                "s.setAttribute('rel', 'stylesheet');" +
                "s.setAttribute('href', '" + url + "');" +
                "d.head.appendChild(s);";
        return js;
    }

    public void handle_more(View view) {
        new ShareUtil(mContext, this).show();
    }
}
