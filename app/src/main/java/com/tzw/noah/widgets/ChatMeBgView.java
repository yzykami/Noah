package com.tzw.noah.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.utils.Utils;

/**
 * Created by yzy on 2017/7/1.
 */

public class ChatMeBgView extends RelativeLayout {

    /*每一个字母的宽度*/
    private int itemWidth;
    /*每一个字母的高度*/
    private int itemHeight;

    Paint stokePaint = new Paint();
    float radius = 10;
    float border = radius / 2;
    float stokeWidth = 5;
    float lineWidth = 5;
    int stokeColor = Color.parseColor("#DA251C");

    int bgColor = Color.parseColor("#ffffff");
    private Paint bgPaint;

    public ChatMeBgView(Context context) {
        super(context);
    }

    public ChatMeBgView(Context context, AttributeSet attr) {
        super(context, attr);
        setBackgroundColor(Color.parseColor("#00000000"));
        float scale = context.getResources().getDisplayMetrics().density;
        stokeWidth = 1f * scale;
        radius = 10f * scale + 0.5f;
        border = radius / 2;
        stokeColor = getResources().getColor(R.color.myRed);
    }

    //得到画布的宽度和每一个字母所占的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        //使得边距好看一些
        itemHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        stokePaint = new Paint();
        stokePaint.setAntiAlias(true);
        stokePaint.setStyle(Paint.Style.STROKE);
        stokePaint.setStrokeWidth(stokeWidth);
        stokePaint.setColor(stokeColor);
        RectF rect1 = new RectF(stokeWidth / 2, stokeWidth / 2, radius + stokeWidth / 2, radius + stokeWidth / 2);
        RectF rect2 = new RectF(itemWidth - border - stokeWidth / 2, stokeWidth/2, itemWidth - border + radius - stokeWidth / 2, radius+stokeWidth/2);
        RectF rect3 = new RectF(itemWidth - border - radius, itemHeight - radius - stokeWidth / 2, itemWidth - border, itemHeight - stokeWidth / 2);
        RectF rect4 = new RectF(stokeWidth / 2, itemHeight - radius - stokeWidth / 2, radius + stokeWidth / 2, itemHeight - stokeWidth / 2);

//        canvas.drawArc(rect1, 180, 90, false, stokePaint);
//
//        canvas.drawArc(rect2, 270, 90, false, stokePaint);
//        canvas.drawArc(rect3, 90, 90, false, stokePaint);
//        canvas.drawArc(rect4, 0, 90, false, stokePaint);


//        RectF rect_round = new RectF(0, 0, itemWidth, itemHeight);
//        canvas.drawRoundRect(rect_round,radius,radius,stokePaint);


        Path path = new Path();
        path.moveTo(stokeWidth / 2, border);
        path.arcTo(rect1, 180, 90);
        path.lineTo(itemWidth, stokeWidth / 2);
        path.arcTo(rect2, 270, -90);
        path.lineTo(itemWidth - border, itemHeight - border);
        path.arcTo(rect3, 0, 90);
        path.lineTo(border, itemHeight - stokeWidth / 2);
        path.arcTo(rect4, 90, 90);
        path.lineTo(stokeWidth / 2, border);
        path.moveTo(stokeWidth / 2, border);
        path.close();


        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        canvas.drawPath(path, bgPaint);

//        path = new Path();
//        path.moveTo(stokeWidth / 2, border);
//        path.arcTo(rect1, 180, 90);
//        path.lineTo(border, stokeWidth / 2);
//
//        path.lineTo(itemWidth, stokeWidth / 2);
//        path.arcTo(rect2, 270, -90);
//        path.lineTo(itemWidth - border, itemHeight - border);
//        path.arcTo(rect3, 0, 90);
//        path.lineTo(itemWidth - radius, itemHeight - stokeWidth / 2);
//        path.lineTo(border, itemHeight - stokeWidth / 2);
//        path.arcTo(rect4, 90, 90);
//        path.lineTo(stokeWidth / 2 - radius, itemHeight - border);
//
//        path.lineTo(stokeWidth / 2, border);
////        path.moveTo(0, border);
//        path.close();
        canvas.drawPath(path, stokePaint);

    }
}
