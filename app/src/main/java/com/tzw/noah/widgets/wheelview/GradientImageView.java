package com.tzw.noah.widgets.wheelview;

/**
 * Created by yzy on 2017-09-27.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created on 2016/3/13.
 */
@SuppressLint("AppCompatCustomView")
public class GradientImageView extends ImageView {
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;//渐变矩阵
    private Paint mPaint;//画笔
    private int mViewWidth = 0;//textView的宽
    private int mViewHeight = 0;//textView的gao
    private int mTranslate = 0;//平移量
    private boolean mAnimating = true;//是否动画
    private int delta = 10;//移动增量
    int interval = 100;
    int totaltime = 700;

    public GradientImageView(Context ctx) {
        this(ctx, null);
        init(ctx);
    }

    public GradientImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        mGradientMatrix = new Matrix();
        mPaint = new Paint();
//        mLinearGradient = new LinearGradient(0, 0, 400, 400,
//                new int[]{0x33ffffff, 0xffffffff, 0x33ffffff},
//                new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP); //边缘融合
//        mPaint.setShader(mLinearGradient);//设置渐变

//        mLinearGradient = new LinearGradient(-200, 0, 600, 0, 0x33000000, 0xffffff, Shader.TileMode.CLAMP);
//        mPaint.setShader(mLinearGradient);
//        mGradientMatrix = new Matrix();

//        mViewWidth = 600;
//        canvas.drawRect(100, 100, 500, 500, paint);
    }

    //    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (mViewWidth == 0) {
//            mViewWidth = getMeasuredWidth();
//            if (mViewWidth > 0) {
//                mPaint = getPaint();
//                String text = getText().toString();
//                int size;
//                if(text.length()>0)
//                {
//                    size = mViewWidth*2/text.length();
//                }else{
//                    size = mViewWidth;
//                }
//                mLinearGradient = new LinearGradient(-size, 0, 0, 0,
//                        new int[] { 0x33ffffff, 0xffffffff, 0x33ffffff },
//                        new float[] { 0, 0.5f, 1 }, Shader.TileMode.CLAMP); //边缘融合
//                mPaint.setShader(mLinearGradient);//设置渐变
//                mGradientMatrix = new Matrix();
//            }
//        }
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (1 == 1)
            return;
        if (mLinearGradient == null) {
            mViewWidth = getWidth();
            mViewHeight = getHeight();
            int h1 = (int) (mViewHeight * 0.6);
            int h2 = (int) (mViewHeight * 0.4);
            h1 = mViewWidth / 2;
            h2 = 0;
            mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0, 0xff000000, Color.GREEN, Shader.TileMode.CLAMP);
            mLinearGradient = new LinearGradient(-mViewWidth, h1, mViewWidth, h2,
                    new int[]{0x33ffffff, 0x20000000, 0x33ffffff},
                    new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP); //边缘融合
            int c1 = 0x30888888;
            int c2 = 0x20cccccc;
            c1 = 0x00000000;
            mLinearGradient = new LinearGradient(-1 * mViewWidth, h1, mViewWidth, h2,
                    new int[]{c1, c2, c1, c2, c1},
                    new float[]{0, 0.25f, 0.5f, 0.75f, 1}, Shader.TileMode.CLAMP); //边缘融合
            mPaint.setShader(mLinearGradient);
            mGradientMatrix = new Matrix();

            interval = totaltime / mViewWidth * delta;
        }
//        if (mAnimating && mGradientMatrix != null) {
//            if (mViewWidth == 0) {
//                mViewWidth = getMeasuredWidth();
        mTranslate += delta;//默认向右移动
        if (mTranslate > mViewWidth + delta || mTranslate < 0) {
//            delta = -delta;//向左移动
            mTranslate = 0;// -mViewWidth;
        }
        mGradientMatrix.setTranslate(mTranslate, 0);
        mLinearGradient.setLocalMatrix(mGradientMatrix);
//        }
        mPaint.setShader(mLinearGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        postInvalidateDelayed(interval);//刷新
    }
}