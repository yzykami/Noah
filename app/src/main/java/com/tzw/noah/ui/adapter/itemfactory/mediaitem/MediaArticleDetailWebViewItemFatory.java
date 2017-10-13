package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.home.HomeDetailActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
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
        if(mMediaListListener instanceof  HomeDetailActivity)
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
        protected void onSetData(int i, MediaArticle content) {
            if (height != 0) {
                ViewGroup.LayoutParams lp = webView.getLayoutParams();
                if(lp.height!=0)
                    return;
                lp.height = height;
                webView.setLayoutParams(lp);
                return;
            }
            WebSettings wSet = webView.getSettings();
            wSet.setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL("about:blank", content.getContentString(), "text/html", "utf-8", null);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    // web 页面加载完成，添加监听图片的点击 js 函数
                    webView.setImageClickListner();
                    //解析 HTML
                    webView.parseHTML(view);

                    webView.loadUrl(getOutCss("file:///android_asset/video.css"));
//                    Toast.makeText(mContext, "old.height = " + height + ",height = " + webView.getHeight(), Toast.LENGTH_SHORT).show();

//                    if (mMediaListListener != null) {
//                        mMediaListListener.onWebViewLoadComplete();
//                    }
//                    height = webView.getHeight();
                }
            });

            View nonVideoLayout = webView;//findViewById(R.id.nonVideoLayout); // Your own view, read class comments
            ViewGroup videoLayout = (ViewGroup) mActivity.findViewById(R.id.videoLayout); // Your own view, read class comments
            //noinspection all
            View loadingView = mActivity.getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
            webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
            {
                // Subscribe to standard events, such as onProgressChanged()...
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    // Your code...
                    if (progress == 100) {
                        if (mMediaListListener != null) {
                            mMediaListListener.onWebViewLoadComplete();
                        }
                        height = webView.getHeight();
                    }
                }
            };
            webView.setWebChromeClient(webChromeClient);

            webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
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
