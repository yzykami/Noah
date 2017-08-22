package com.tzw.noah.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tzw.noah.R;


/**
 * Created by yzy on 2017/8/21.
 */

public class MyGroupCoverView extends View {
    public MyGroupCoverView(Context context) {
        super(context);
        paintMask.setColor(getResources().getColor(R.color.myRed));
    }

    public MyGroupCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintMask.setColor(getResources().getColor(R.color.myRed));
        paintMask.setAlpha(120);
    }

    public MyGroupCoverView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Drawable mask; // blend mask drawable
    private static final Paint paintMask = createMaskPaint();
    private int maskHeight = 40;

    private static Paint createMaskPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(Color.GRAY);
        paint.setAlpha(180);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //mask = getResources().getDrawable(R.drawable.sns_user_default);
        if(mask!=null) {
            maskHeight= ScreenUtil.dip2px(16);
            // bounds
            int width = getWidth();
            int height = getHeight();

            // create blend layer
            canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);


            Paint bgPaint = new Paint();
            //设置抗锯齿
            bgPaint.setAntiAlias(true);
            //设置颜色为textLightGray
//        bgPaint.setColor(getResources().getColor(R.color.myRed));
//        bgPaint.setAlpha(1);

//        canvas.drawCircle(height / 2, height / 2, height / 2, bgPaint);

            mask.setBounds(0, 0, width, height);
            mask.draw(canvas);

            RectF rectF = new RectF(0, height - maskHeight, width, height);
            canvas.drawRect(rectF, paintMask);
//        canvas.saveLayer(0, 0, width, height, paintMask, Canvas.ALL_SAVE_FLAG);
            super.onDraw(canvas);

            // apply blend layer
            canvas.restore();
        }
        else{
            super.onDraw(canvas);
        }
    }

    public void setMask(Drawable drawable)
    {
        mask = drawable;
    }
}
