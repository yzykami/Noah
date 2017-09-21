package com.tzw.noah.widgets;

/**
 * Created by yzy on 2017-09-20.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * 视频播放
 * 使用方式{@link #VideoWebChromeClient(View, ViewGroup, Context)}
 * 在全屏、普通模式下，添加屏幕旋转代码 {@link #setOnToggledFullscreen(ToggledFullscreenCallback)}
 * github地址：https://github.com/cprcrack/VideoEnabledWebView
 */
public class VideoWebChromeClient2 extends WebChromeClient implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private View mWebViewContainer;
    private ViewGroup mFullScreenContainer;
    private View loadingView;
    private boolean isVideoFullscreen; // Indicates if the video is being displayed using a custom view (typically full-screen)
    private FrameLayout videoViewContainer;
    private CustomViewCallback videoViewCallback;
    private ToggledFullscreenCallback toggledFullscreenCallback;

    /**
     * Never use this constructor alone.
     * This constructor allows this class to be defined as an inline inner class in which the user can override methods
     */
    @SuppressWarnings("unused")
    public VideoWebChromeClient2() {
    }


    /**
     * 创建一个video客户端
     *
     * @param webViewContainer    视频播放页面的容器
     * @param fullScreenContainer 全屏播放容器对象
     * @param context             持有对象，用于创建进度条
     */
    public VideoWebChromeClient2(View webViewContainer, ViewGroup fullScreenContainer, Context context) {
        this.mWebViewContainer = webViewContainer;
        this.mFullScreenContainer = fullScreenContainer;
        this.loadingView = null;
        this.isVideoFullscreen = false;
        loadingView = new ProgressBar(context);

    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     *
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    public boolean isVideoFullscreen() {
        return isVideoFullscreen;
    }

    /**
     * Set a callback that will be fired when the video starts or finishes displaying using a custom view (typically full-screen)
     *
     * @param callback A VideoEnabledWebChromeClient.ToggledFullscreenCallback callback
     */
    @SuppressWarnings("unused")
    public void setOnToggledFullscreen(ToggledFullscreenCallback callback) {
        this.toggledFullscreenCallback = callback;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (view instanceof FrameLayout) {
            // A video wants to be shown
            FrameLayout frameLayout = (FrameLayout) view;
            View focusedChild = frameLayout.getFocusedChild();

            // Save video related variables
            this.isVideoFullscreen = true;
            this.videoViewContainer = frameLayout;
            this.videoViewCallback = callback;
            // Hide the non-video view, add the video view, and show it
            mWebViewContainer.setVisibility(View.INVISIBLE);

            mFullScreenContainer.addView(videoViewContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenContainer.setVisibility(View.VISIBLE);

            if (focusedChild instanceof android.widget.VideoView) {
                // android.widget.VideoView (typically API level <11)
                android.widget.VideoView videoView = (android.widget.VideoView) focusedChild;

                // Handle all the required events
                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
            }

            // 下面是全屏加载完自动播放的代码，大部分手机上可以，但是在htc one上崩溃，因此不加入这个特性
//            String js = "javascript:";
//            js += "var _ytrp_html5_video = document.getElementsByTagName('video')[0];";
//            js += "if (_ytrp_html5_video != undefined) {";
//            {
//                js += " if(_ytrp_html5_video.paused) _ytrp_html5_video.play();";
//            }
//            js += "}";
//            webView.loadUrl(js);

            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback.toggledFullscreen(true);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) // Available in API level 14+, deprecated in API level 18+
    {
        onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        // This method should be manually called on video end in all cases because it's not always called automatically.
        // This method must be manually called on back key press (from this class' onBackPressed() method).
        if (isVideoFullscreen) {
            // Hide the video view, remove it, and show the non-video view
            mFullScreenContainer.setVisibility(View.INVISIBLE);
            mFullScreenContainer.removeView(videoViewContainer);
            mWebViewContainer.setVisibility(View.VISIBLE);

            // 全屏返回之后，视频状态不能衔接上，因为onCustomViewHidden很多情况下会奔溃
            //原因： Call back (only in API level <19, because in API level 19+ with chromium webview it crashes)
            if (videoViewCallback != null && !videoViewCallback.getClass().getName().contains(".chromium.")) {
                videoViewCallback.onCustomViewHidden();
            }

            // Reset video related variables
            isVideoFullscreen = false;
            videoViewContainer = null;
            videoViewCallback = null;

            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback.toggledFullscreen(false);
            }
        }
    }

    @Override
    public View getVideoLoadingProgressView() // Video will start loading
    {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            return loadingView;
        } else {
            return super.getVideoLoadingProgressView();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) // Video will start playing, only called in the case of android.widget.VideoView (typically API level <11)
    {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) // Video finished playing, only called in the case of android.widget.VideoView (typically API level <11)
    {
        onHideCustomView();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) // Error while playing video, only called in the case of android.widget.VideoView (typically API level <11)
    {
        return false; // By returning false, onCompletion() will be called
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity itself should handle it. Otherwise don't do anything.
     *
     * @return Returns true if the event was handled, and false if was not (video view is not visible)
     */
    @SuppressWarnings("unused")
    public boolean onBackPressed() {
        if (isVideoFullscreen) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }
    }

    public interface ToggledFullscreenCallback {
        void toggledFullscreen(boolean fullscreen);
    }

}