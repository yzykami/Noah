package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.widgets.MyWebView;

import java.util.logging.Handler;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleDetailWebViewItemFatory extends AssemblyRecyclerItemFactory<MediaArticleDetailWebViewItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int height;

    public MediaArticleDetailWebViewItemFatory(MediaArticleDetailListener mMediaListListener) {
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

        return new GalleryItem(R.layout.webview2, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        //        @BindView(R.id.container)
//        LinearLayout container;
        @BindView(R.id.webView)
        MyWebView webView;

        Context mContext;

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
                lp.height = height;
                webView.setLayoutParams(lp);
                return;
            }
            WebSettings wSet = webView.getSettings();
            wSet.setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL("about:blank", content.articleContent, "text/html", "utf-8", null);
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
//                    Toast.makeText(mContext, "old.height = " + height + ",height = " + webView.getHeight(), Toast.LENGTH_SHORT).show();

//                    if (mMediaListListener != null) {
//                        mMediaListListener.onWebViewLoadComplete();
//                    }
//                    height = webView.getHeight();
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        if (mMediaListListener != null) {
                            mMediaListListener.onWebViewLoadComplete();
                        }
                        height = webView.getHeight();
                    } else {
                    }

                }
            });
        }
    }
}
