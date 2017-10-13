package com.tzw.noah.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.tzw.noah.R;
import com.tzw.noah.widgets.swipeback.SwipeBackLayout;

/**
 * Created by yzy on 2017-10-10.
 */

public class MySwipeBackActivity extends MyBaseActivity implements SwipeBackLayout.SwipeBackListener {
    private static final SwipeBackLayout.DragEdge DEFAULT_DRAG_EDGE = SwipeBackLayout.DragEdge.LEFT;

    private SwipeBackLayout swipeBackLayout;
    private ImageView ivShadow;
    FrameLayout content;
    //是否开启滑动退出
    boolean isSwipeBack = false;

    @Override
    public void setContentView(int layoutResID) {
        if (isSwipeBack) {
            super.setContentView(getContainer());
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            swipeBackLayout.addView(view);
            setDragEdge(SwipeBackLayout.DragEdge.LEFT);
            content = (FrameLayout) findViewById(android.R.id.content);
        } else {
            super.setContentView(layoutResID);
        }
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setOnSwipeBackListener(this);
        ivShadow = new ImageView(this);
        ivShadow.setBackgroundColor(getResources().getColor(R.color.black_p50));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(swipeBackLayout);
        return container;
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        if (swipeBackLayout!=null) {
            swipeBackLayout.setDragEdge(dragEdge);
        }
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        ivShadow.setAlpha(1 - fractionScreen);
        if (fractionScreen > 0)
            content.setBackgroundResource(R.color.transparent);
        else
            content.setBackgroundResource(R.color.white);

    }

    @Override
    public void handle_back(View v) {
        super.handle_back(v);
        overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit);
    }
}
