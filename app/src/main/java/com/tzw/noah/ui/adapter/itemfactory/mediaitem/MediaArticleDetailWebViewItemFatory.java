package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.ui.home.HomeDetailActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.ui.webview.WebViewActivity;
import com.tzw.noah.widgets.MyWebView;
import com.tzw.noah.widgets.VideoEnabledWebChromeClient;

import java.util.logging.Handler;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleDetailWebViewItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailWebViewItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int height;
    public GalleryItem item;
    HomeDetailActivity mActivity;

    public MediaArticleDetailWebViewItemFatory(MediaArticleDetailListener mMediaListListener) {
        if (mMediaListListener instanceof HomeDetailActivity)
            mActivity = (HomeDetailActivity) mMediaListListener;
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isWebContent();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {
        return item = new GalleryItem(R.layout.webview2, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        //        @BindView(R.id.container)
//        LinearLayout container;
        @BindView(R.id.webView)
        MyWebView webView;

        Context mContext;
        boolean isFirstLoad = true;
        private VideoEnabledWebChromeClient webChromeClient;

        public MyWebView getWebView() {
            return webView;
        }

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
        }

        @Override
        protected void onSetData(int i, final MediaArticle content) {
            if (height != 0) {
                ViewGroup.LayoutParams lp = webView.getLayoutParams();
                if (lp.height != 0)
                    return;
                lp.height = height;
                webView.setLayoutParams(lp);
                return;
            }
            WebSettings wSet = webView.getSettings();
            wSet.setJavaScriptEnabled(true);

//            if (content.isWebBrowserMode) {
//                webView.loadUrl(content.getContentString());
//                webView.setBackgroundResource(R.color.myBlue);
//                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) webView.getLayoutParams();
//                lp.setMargins(0, 10, 0, 0);
//                webView.setLayoutParams(lp);
//            } else {
//            if (content.articleId == 203)
//                webView.loadUrl("http://10.0.9.2:7072/home/media/detail/id/70.html");
//            else
                webView.loadDataWithBaseURL("about:blank", content.getContentString(), "text/html", "utf-8", null);
//            webView.loadUrl("http://10.0.12.226/b.html");
//            }

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        if (content.isWebBrowserMode) {
//                            view.loadUrl(url);
//                        } else {
                        MediaArticle ma = new MediaArticle();
                        Bundle bu = new Bundle();
                        bu.putSerializable("DATA", ma);
                        ma.articleContent = url;
//                            ma.isWebBrowserMode = true;
                        mActivity.startActivity2(WebViewActivity.class, bu);
//                    }
                        return true;
                    }
//                    else{// if (url.startsWith("tzw://")) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        mContext.startActivity(intent);
//                        return true;
//                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = "";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        url = request.getUrl().toString();
                    } else {
                        url = request.toString();
                    }
                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        if (content.isWebBrowserMode) {
//                            view.loadUrl(url);
//                        } else {
                        MediaArticle ma = new MediaArticle();
                        Bundle bu = new Bundle();
                        bu.putSerializable("DATA", ma);
                        ma.articleContent = url;
//                            ma.isWebBrowserMode = true;
                        mActivity.startActivity2(WebViewActivity.class, bu);
//                    }
                        return true;
                    }
//                    else{// if (url.startsWith("tzw://")) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        mContext.startActivity(intent);
//                        return true;
//                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
//                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    if(!isLoadUrl){
//                        isLoadUrl = true;
//                        view.loadUrl(url);

//                    }
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (isFirstLoad) {
                        isFirstLoad = false;
                        // web 页面加载完成，添加监听图片的点击 js 函数
                        webView.setImageClickListner();
                        //解析 HTML
                        webView.parseHTML(view);
                        webView.loadUrl("javascript:handleLogin(\"" + UserCache.getLoginKey() + "\")");
                        webView.loadUrl(getOutCss("file:///android_asset/video.css"));
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    //handler.cancel(); 默认的处理方式，WebView变成空白页
                    // 接受证书
                    handler.proceed();  // 接受所有网站的证书
//                    super.onReceivedSslError(view, handler, error);
                }
            });

            View nonVideoLayout = webView;//findViewById(R.id.nonVideoLayout); // Your own view, read class comments
            ViewGroup videoLayout = (ViewGroup) mActivity.findViewById(R.id.videoLayout); // Your own view, read class comments
            //noinspection all
            View loadingView = mActivity.getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
            webChromeClient = new

                    VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
                    {
                        // Subscribe to standard events, such as onProgressChanged()...
                        @Override
                        public void onProgressChanged(WebView view, int progress) {
                            // Your code...
                            if (progress == 100) {
                                if (mMediaListListener != null) {
                                    view.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMediaListListener.onWebViewLoadComplete();
                                        }
                                    }, 100);
                                }
                                height = webView.getHeight();
                            }
                        }
                    }

            ;
            webView.setWebChromeClient(webChromeClient);

            webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()

            {
                @Override
                public void toggledFullscreen(boolean fullscreen) {
                    if (mMediaListListener != null) {
                        mMediaListListener.toggledFullscreen(fullscreen);
                    }
                }
            });
        }

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
}
