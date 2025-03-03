package com.tzw.noah.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.ui.MyBaseActivity;

/**
 * Created by yzy on 2017-09-19.
 */

public class SoftHideKeyBoardUtil {
    public static SoftHideKeyBoardUtil assistActivity(Activity activity) {
        return new SoftHideKeyBoardUtil(activity);
    }

    public interface KeyBoardListener {
        /**
         * call back
         *
         * @param isShow         true is show else hidden
         * @param keyboardHeight keyboard height
         */
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }

    KeyBoardListener mKeyBoardListener;
    FrameLayout content;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    //为适应华为小米等手机键盘上方出现黑条或不适配
    private int contentHeight;//获取setContentView本来view的高度
    private boolean isfirst = true;//只用获取一次
    private int statusBarHeight;//状态栏高度
    Activity activity;

    private SoftHideKeyBoardUtil(Activity activity) {
        this.activity = activity;
        //1､找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        content = (FrameLayout) activity.findViewById(android.R.id.content);
        content.setBackgroundResource(R.color.white);
        //2､获取到setContentView放进去的View
        mChildOfContent = content.getChildAt(0);
        //3､给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //4､软键盘弹起会使GlobalLayout发生变化
            public void onGlobalLayout() {
                if (isfirst) {
                    contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                    isfirst = false;
                }
                //5､当前布局发生变化时，对Activity的xml布局进行重绘
                possiblyResizeChildOfContent();
            }
        });
//        mChildOfContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
////                Log.log("aaa", "addOnLayoutChangeListener : bottom = " + bottom + ",oldBottom" + oldBottom);
//                if(bottom==oldBottom&&bottom!=contentHeight)
//                {
//                    if (mKeyBoardListener != null) {
//                        mKeyBoardListener.onKeyboardChange(false, 0);
//                    }
//                }
//            }
//        });
        //6､获取到Activity的xml布局的放置参数
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    public SoftHideKeyBoardUtil setOnKeyboardChange(KeyBoardListener listener) {
        mKeyBoardListener = listener;
        return this;
    }

    // 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
    private void possiblyResizeChildOfContent() {
        content.setBackgroundResource(R.color.white);
        //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow = computeUsableHeight();
        //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3､获取Activity中xml中布局在当前界面显示的高度
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            //4､Activity中xml布局的高度-当前可用高度
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            //5､高度差大于屏幕1/4时，说明键盘弹出
            boolean isShow = true;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (activity instanceof MyBaseActivity) {
                        statusBarHeight = ((MyBaseActivity) activity).getStatusBarHeight();
                        if (((MyBaseActivity) activity).getStatusBar() == null || ((MyBaseActivity) activity).getStatusBar().getVisibility() != View.VISIBLE)
                            statusBarHeight = 0;
                        frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                    }
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
                if (mKeyBoardListener != null) {
                    mKeyBoardListener.onKeyboardChange(true, heightDifference);
                }
                isShow = true;
            } else {
                frameLayoutParams.height = contentHeight;
                if (mKeyBoardListener != null) {
                    mKeyBoardListener.onKeyboardChange(false, 0);
                }
                isShow = false;
            }
            //7､ 重绘Activity的xml布局
            int h1 = 0;
//            if (isShow) {
//                h1 = frameLayoutParams.height;
//                frameLayoutParams.height = contentHeight;
//                mChildOfContent.requestLayout();
//                final int finalH = h1;
//                mChildOfContent.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        frameLayoutParams.height = finalH;
//                        mChildOfContent.requestLayout();
//                    }
//                }, 150);
//            } else
            mChildOfContent.requestLayout();


            usableHeightPrevious = usableHeightNow;
            Log.log("aaa", "usableHeightNow=" + usableHeightNow + ",usableHeightPrevious = " + usableHeightPrevious + ",usableHeightSansKeyboard" + usableHeightSansKeyboard + ",heightDifference" + heightDifference + ",contentHeight" + frameLayoutParams.height + ",h1=" + h1);
        }

    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        return (r.bottom - r.top);
    }

    public void setFullScreen() {
//        if (contentHeight == 0)
//            return;
//        frameLayoutParams.height = contentHeight;
//        mChildOfContent.requestLayout();
    }
}