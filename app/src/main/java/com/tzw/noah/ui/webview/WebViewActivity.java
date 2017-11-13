package com.tzw.noah.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.tzw.noah.widgets.MyWebView;

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
        title = "网页浏览";
    }

    private void findview() {

    }

    private void initview() {
        tv_title.setText(title);
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
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
}
