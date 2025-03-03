package com.tzw.noah.widgets;

/**
 * Created by yzy on 2017-09-27.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * Created on 2016/3/13.
 */
@SuppressLint("AppCompatCustomView")
public class GradientHorizontalTextView extends TextView {
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;//渐变矩阵
    private Paint mPaint;//画笔
    private int mViewWidth = 0;//textView的宽
    private int mTranslate = 0;//平移量
    private boolean mAnimating = true;//是否动画
    private int delta = 15;//移动增量
    public GradientHorizontalTextView(Context ctx)
    {
        this(ctx,null);
    }

    public GradientHorizontalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                String text = getText().toString();
                int size;
                if(text.length()>0)
                {
                    size = mViewWidth*2/text.length();
                }else{
                    size = mViewWidth;
                }
                mLinearGradient = new LinearGradient(-size, 0, 0, 0,
                        new int[] { 0x33ffffff, 0xffffffff, 0x33ffffff },
                        new float[] { 0, 0.5f, 1 }, Shader.TileMode.CLAMP); //边缘融合
                mPaint.setShader(mLinearGradient);//设置渐变
                mGradientMatrix = new Matrix();
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            float mTextWidth = getPaint().measureText(getText().toString());//获得文字宽
            mTranslate += delta;//默认向右移动
            if (mTranslate > mTextWidth+1 || mTranslate<1) {
                delta = -delta;//向左移动
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(30);//刷新
        }
    }
}