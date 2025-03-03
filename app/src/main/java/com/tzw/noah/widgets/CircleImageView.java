package com.tzw.noah.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.tzw.noah.R;
import com.tzw.noah.utils.Utils;


public class CircleImageView extends AppCompatImageView {

	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	private static final boolean DEFAULT_BORDER_OVERLAY = false;

	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;

	private static final int DEFAULT_ROUND_RADIUS = 10;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderPaint = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	private float mDrawableRadius;
	private float mBorderRadius;

	private ColorFilter mColorFilter;

	private boolean mReady;
	private boolean mSetupPending;
	private boolean mBorderOverlay;

	private int type;

	private int mRoundRadius;

	private int num;
	//红色圆圈的半径
	private float radius;
	//圆圈内数字的半径
	private float textSize;
	//右边和上边内边距
	private int paddingRight;
	private int paddingTop;

	public void setMode(Context context, int dp, int type) {
		mRoundRadius = Utils.dp2px(context, dp);
		this.type = type;
	}

	public CircleImageView(Context context) {
		super(context);

		init();
	}
	//设置显示的数量
	public void setNum(int num) {
		this.num = num;
		//重新绘制画布
		invalidate();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

		mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
		mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
		mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_border_overlay, DEFAULT_BORDER_OVERLAY);

		// 获取圆角半径
		mRoundRadius = a.getDimensionPixelSize(R.styleable.CircleImageView_round_Radius, DEFAULT_ROUND_RADIUS);

		type = a.getInt(R.styleable.CircleImageView_type, TYPE_ROUND);
		a.recycle();

		init();
	}

	private void init() {
		super.setScaleType(SCALE_TYPE);
		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (adjustViewBounds) {
			throw new IllegalArgumentException("adjustViewBounds not supported.");
		}
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// if (getDrawable() == null) {
	// return;
	// }
	//
	// canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
	// mBitmapPaint);
	// if (mBorderWidth != 0) {
	// canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
	// mBorderPaint);
	// }
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		// 如果图片不存在就不画
		if (getDrawable() == null)
			return;

		if (type == TYPE_ROUND) {
			// 绘制内圆角矩形，参数矩形区域，圆角半径，图片画笔为mBitmapPaint
			canvas.drawRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, mBitmapPaint);
			if (mBorderWidth != 0) {
				// 如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆角矩形 参数矩形区域，圆角半径，边界画笔为mBorderPaint
				canvas.drawRoundRect(mBorderRect, mRoundRadius + mBorderWidth / 2, mRoundRadius + mBorderWidth / 2,
						mBorderPaint);
			}
		} else if (type == TYPE_CIRCLE) {
			// 绘制内圆形，参数圆心坐标，内圆半径，图片画笔为mBitmapPaint
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
			// 如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆形 参数圆心坐标，外圆半径，边界画笔为mBorderPaint
			if (mBorderWidth != 0) {
				canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
			}
		}

		if (num > 0) {
			//初始化半径
			radius = getWidth() / 6;
			//初始化字体大小
			textSize = num < 10 ? radius + 5 : radius;
			//初始化边距
			paddingRight = getPaddingRight();
			paddingTop = getPaddingTop();
			//初始化画笔
			Paint paint = new Paint();
			//设置抗锯齿
			paint.setAntiAlias(true);
			//设置颜色为红色
			paint.setColor(getResources().getColor(R.color.myRed));
			//设置填充样式为充满
			paint.setStyle(Paint.Style.FILL);
			//画圆
			canvas.drawCircle(getWidth() - radius - paddingRight/2, radius + paddingTop/2, radius, paint);
			//设置颜色为白色
			paint.setColor(0xffffffff);
			//设置字体大小
			paint.setTextSize(textSize);
			//画数字
			canvas.drawText("" + (num < 99 ? num : 99),
					num < 10 ? getWidth() - radius - textSize / 4 - paddingRight/2
							: getWidth() - radius - textSize / 2 - paddingRight/2,
					radius + textSize / 3 + paddingTop/2, paint);
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	public void setBorderColorResource(@ColorRes int borderColorRes) {
		setBorderColor(getContext().getResources().getColor(borderColorRes));
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	public boolean isBorderOverlay() {
		return mBorderOverlay;
	}

	public void setBorderOverlay(boolean borderOverlay) {
		if (borderOverlay == mBorderOverlay) {
			return;
		}

		mBorderOverlay = borderOverlay;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(@DrawableRes int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (cf == mColorFilter) {
			return;
		}

		mColorFilter = cf;
		mBitmapPaint.setColorFilter(mColorFilter);
		invalidate();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap
						.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		mBorderRect.set(0, 0, getWidth(), getHeight());
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

		mDrawableRect.set(mBorderRect);
		if (!mBorderOverlay) {
			mDrawableRect.inset(mBorderWidth, mBorderWidth);
		}
		mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

}