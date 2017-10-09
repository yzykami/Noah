package com.tzw.noah.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * Created by yzy on 2017-09-18.
 */

public class MyViewPager extends ViewPager {

    private static final String TAG = "MyViewPager";

    int lastX = -1;
    int lastY = -1;
    private int curPosition;
//    private float xDistance;
//    private float yDistance;
//    private float xStart;
//    private float yStart;
//    private float xEnd;
//    private float yEnd;


    public interface HScrollListener {
        void onHScrollChanging(boolean isScrolling);
    }

    public void setHScrollListener(HScrollListener mHScrollListener) {
        this.mHScrollListener = mHScrollListener;
    }

    HScrollListener mHScrollListener;
    boolean isScrolling = false;

    public MyViewPager(Context context) {
        super(context);
        fixTouchSlop();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixTouchSlop();
    }

//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xStart = ev.getX();
//                yStart = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                xEnd = ev.getX();
//                yEnd = ev.getY();
//                break;
//            default:
//                break;
//        }
//        xDistance = Math.abs(xEnd-xStart);
//        yDistance = Math.abs(yEnd-yStart);
//        if(xDistance>yDistance)
//            return true;  //拦截事件向下分发
//        return super.onInterceptTouchEvent(ev);//默认值为false
//    }

    /**
     * 这个方法是通过反射，修改viewpager的触发切换的最小滑动速度（还是距离？姑
     * 且是速度吧！滑了10dp就给它切换）
     **/
    private void fixTouchSlop() {
        Field field = null;
        try {
            field = ViewPager.class.getDeclaredField("mMinimumVelocity");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            field.setInt(this, 15);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /****
     * 滑动距离及坐标 归还父控件焦点
     ****/
    private float xDistance, yDistance, xLast, yLast;
    /**
     * 是否是左右滑动
     **/
    private boolean mIsBeingDragged = true;

    /**
     * 重写这个方法纯属是为了告诉父View(Recyclerview)，什么时候不要拦截viewpager的滑动
     * 事件
     **/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//            return false;
////            return super.dispatchTouchEvent(ev);
        String motionEvent = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                motionEvent = "ACTION_DOWN";
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                mIsBeingDragged = true;
                if (mHScrollListener != null)
                    mHScrollListener.onHScrollChanging(true);
                break;
            case MotionEvent.ACTION_MOVE:
                motionEvent = "ACTION_MOVE";
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
//                if (!mIsBeingDragged) {
                if (yDistance <= xDistance * 0.5) {//小于30度都左右滑
                    mIsBeingDragged = true;
//                        getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    mIsBeingDragged = false;
//                        getParent().requestDisallowInterceptTouchEvent(false);
                }
//                }
                break;
            case MotionEvent.ACTION_UP:
                motionEvent = "ACTION_UP";
                mIsBeingDragged = false;
            case MotionEvent.ACTION_CANCEL:
                motionEvent = "ACTION_CANCEL";
                mIsBeingDragged = false;
                break;
        }
//        com.tzw.noah.logger.Log.log("aaa", motionEvent + " " + "isDragged = " + (mIsBeingDragged ? "true" : "false") + " x = " + xDistance + " ,y" + yDistance);
        if (isScrolling != mIsBeingDragged) {
            isScrolling = mIsBeingDragged;
            if (mHScrollListener != null)
                mHScrollListener.onHScrollChanging(isScrolling);
        }
        return super.dispatchTouchEvent(ev);
    }


//    int mx, my = 0;
//
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Boolean intercept = false;
//        //获取坐标点：
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                intercept = false;
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deletx = x - mx;
//                int delety = y - my;
//                if (Math.abs(deletx) > Math.abs(delety)) {
//                    intercept = true;
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                    Log.d("aaa", "拦截了" + "111");
//                } else {
//                    intercept = false;
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                    Log.d("aaa" ,  "没拦截" + "222");
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                intercept = false;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d("aaa" ,  "ACTION_CANCEL");
//                intercept = false;
//                break;
//            default:
//                break;
//        }
//        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
////        lastx=x;
////        lasty=y;
//        mx = x;
//        my = y;
//        return intercept;
//    }

}
