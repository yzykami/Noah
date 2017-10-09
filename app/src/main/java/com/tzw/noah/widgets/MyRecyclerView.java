package com.tzw.noah.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by yzy on 2017-10-08.
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int x, y;


//    @Override
//
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN://按下y
//                x = (int) ev.getX();
//                y = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE://移动
//                int new_x = (int) ev.getX();
//                int new_y = (int) ev.getY();
////                //判断有水平滑动的意向
////                int move_x = Math.abs(new_x - x);//x轴滑动的距离
////                int move_y = Math.abs(new_y - y);//y轴滑动的距离
////                if (move_x > (move_y + 10))//10的偏移量
////                {
////                    return false;//传递给字View
////                }
//                //判断有上下滑动的意向（用于字VIew是上下，parent是水平的）
//                int move_x = Math.abs(new_x - x);//x轴滑动的距离
//                int move_y = Math.abs(new_y - y);//y轴滑动的距离
//                if (move_y > (move_x + 10))//10的偏移量
//                {
//                    Log.d("aaa" ,  "recyclerView UpDown");
//                    return false;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
