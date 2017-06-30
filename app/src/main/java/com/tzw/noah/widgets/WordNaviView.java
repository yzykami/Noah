package com.tzw.noah.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.tzw.noah.R;

/**
 * Created by yzy on 2017/6/29.
 */

public class WordNaviView extends LinearLayout {
    /*绘制的列表导航字母*/
    private String words[] = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    /*字母画笔*/
    private Paint wordsPaint;
    /*字母背景画笔*/
    private Paint bgPaint;
    /*每一个字母的宽度*/
    private int itemWidth;
    /*每一个字母的高度*/
    private int itemHeight;
    /*手指按下的字母索引*/
    private int touchIndex = 0;
    /*手指按下的字母改变接口*/
    private onWordsChangeListener listener;

    /*手指按下了哪个字母的回调接口*/
    public interface onWordsChangeListener {
        void wordsChange(String words);
    }

    /*设置手指按下字母改变监听*/
    public void setOnWordsChangeListener(onWordsChangeListener listener) {
        this.listener = listener;
    }

    public WordNaviView(Context context) {
        super(context);
    }

    public WordNaviView(Context context, AttributeSet attr) {
        super(context, attr);

    }


    //得到画布的宽度和每一个字母所占的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        //使得边距好看一些
        int height = getMeasuredHeight();
        itemHeight = height / words.length;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgPaint = new Paint();
        //设置抗锯齿
        bgPaint.setAntiAlias(true);
        //设置颜色为textLightGray
        bgPaint.setColor(getResources().getColor(R.color.myRed));
        //设置填充样式为充满
        bgPaint.setStyle(Paint.Style.STROKE);

        int itemContentHeight = itemHeight * 5 / 6;
        wordsPaint = new Paint();
        wordsPaint.setAntiAlias(true);
        wordsPaint.setColor(getResources().getColor(R.color.textDarkGray));
        wordsPaint.setStyle(Paint.Style.FILL);
        wordsPaint.setTextSize(itemContentHeight);
        for (int i = 0; i < words.length; i++) {

            if (i == 0) {
                Paint mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mBitPaint.setFilterBitmap(true);
                mBitPaint.setDither(true);
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sns_search_little)).getBitmap();
                int mBitWidth = itemContentHeight;
                int mBitHeight = itemContentHeight;
                ;
                Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect mDestRect = new Rect(itemWidth / 2 - mBitHeight / 2, 0, mBitHeight + itemWidth / 2 - mBitHeight / 2, mBitHeight);
                canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mBitPaint);
            } else {
                //获取文字的宽高
                Rect rect = new Rect();
                wordsPaint.getTextBounds(words[i], 0, 1, rect);
                int wordWidth = rect.width();
                //绘制字母
                float wordX = itemWidth / 2 - wordWidth / 2;
                float wordY = i * itemHeight + itemHeight / 2 + itemHeight / 5;//itemHeight / 2 +
                //判断是不是我们按下的当前字母
                if (touchIndex == i) {
                    //绘制文字圆形背景
                    //canvas.drawCircle(itemWidth / 2, itemHeight / 2 + i * itemHeight, itemHeight-5, bgPaint);
//                wordsPaint.setColor(Color.WHITE);
                } else {
//                wordsPaint.setColor(Color.LTGRAY);
                }
                canvas.drawText(words[i], wordX, wordY, wordsPaint);
            }
        }
    }

    /**
     * 当手指触摸按下的时候改变字母背景颜色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //关键点===获得我们按下的是那个索引(字母)
                int index = (int) (y / itemHeight);
                if (index != touchIndex)
                    touchIndex = index;
                //防止数组越界
                if (listener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    //回调按下的字母
                    listener.wordsChange(words[touchIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起,不做任何操作
                break;
        }
        return true;
    }
}
