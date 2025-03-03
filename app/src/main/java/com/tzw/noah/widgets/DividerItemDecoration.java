package com.tzw.noah.widgets;

/**
 * Created by yzy on 2017-09-16.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tzw.noah.R;

/*
这个是v7 中的一个sample中的
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * RecyclerView分割线
 * 暂时对StaggeredGridLayoutManager错序不支持，其他情况均支持
 * 自定义LayoutManager暂不做考虑
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "tianbin";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    /**
     * 设置是否显示边界线，即上下左右分割线
     * 当为网格布局时上下左右均有效，当为线性布局时，只有上下或者左右分别有效
     */
    private boolean drawBorderLine = false;

    public DividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId           分割线图片
     */
    public DividerItemDecoration(Context context, @DrawableRes int drawableId) {
        mDivider = ContextCompat.getDrawable(context, drawableId);
    }

    /**
     * 垂直滚动，item宽度充满，高度自适应
     * 水平滚动,item高度充满，宽度自适应
     * 在itemView绘制完成之前调用，也就是说此方法draw出来的效果将会在itemView的下面
     * onDrawOver方法draw出来的效果将叠加在itemView的上面
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    /**
     * 滚动方向为垂直（VERTICAL_LIST），画水平分割线
     * @param c
     * @param parent
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int allChildCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top=0,bottom=0,left=0,right=0;
            left = child.getLeft() - params.leftMargin;
            right = child.getRight() + params.rightMargin;
            if(drawBorderLine){
                if(isFirstRaw(parent,params.getViewLayoutPosition(),spanCount)){
                    top=child.getTop()-params.topMargin-mDivider.getIntrinsicHeight();
                    bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }else{
                if(isLastRaw(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                    continue;
                }
            }
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 滚动方向为水平，画垂直分割线
     * @param c
     * @param parent
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int allChildCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left=0,right=0,top=0,bottom=0;
            top=child.getTop()-params.topMargin;
            bottom=child.getBottom()+params.bottomMargin;
            if(drawBorderLine){
                //加上第一条
                if(isFirstColumn(parent,params.getViewLayoutPosition(),spanCount)){
                    left=child.getLeft()-params.leftMargin-mDivider.getIntrinsicWidth();
                    right = left + mDivider.getIntrinsicWidth();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }else{
                if(isLastColum(parent,params.getViewLayoutPosition(),spanCount,allChildCount)){
                    continue;
                }
            }

            left = child.getRight() + params.rightMargin;
            right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition=((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        boolean isLastRaw=isLastRaw(parent, itemPosition, spanCount, childCount);
        boolean isLastColum=isLastColum(parent, itemPosition, spanCount, childCount);
        boolean isFirstRaw=isFirstRaw(parent,itemPosition,spanCount);
        boolean isFirstColumn=isFirstColumn(parent,itemPosition,spanCount);
        int left=0,top=0,right=0,bottom=0;
        //画线的规则是按照右边，下边，所以每个item默认只需设置右边，下边，边框处理按以下规则
        right=mDivider.getIntrinsicWidth();
        bottom=mDivider.getIntrinsicHeight();
        if(drawBorderLine){
            if(isFirstRaw){/**第一行：分为第一列和最后一列两种情况*/
                top=mDivider.getIntrinsicHeight();
            }
            if(isFirstColumn){
                left=mDivider.getIntrinsicWidth();
            }
        }else{
            if(isLastColum){
                right=0;//不画线，最后一列右边不留偏移
            }
            if(isLastRaw){
                bottom=0;//不画线，最后一行底部不留偏移
            }
        }
        outRect.set(left,top,right,bottom);
    }

    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = (layoutManager instanceof GridLayoutManager)?((GridLayoutManager) layoutManager).getOrientation():((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                if(pos<spanCount){
                    return true;
                }
            }else{
                if(pos%spanCount==0){
                    return true;
                }
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                if(pos==0){
                    return true;
                }
            }else{
                //每一个都是第一行，也是最后一行
                return true;
            }
        }
        return false;
    }

    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = (layoutManager instanceof GridLayoutManager)?((GridLayoutManager) layoutManager).getOrientation():((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                if(pos%spanCount==0){
                    return true;
                }
            }else{
                if(pos<spanCount){
                    return true;
                }
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                //每一个都是第一列，也是最后一列
                return true;
            }else{
                if(pos==0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = (layoutManager instanceof GridLayoutManager)?((GridLayoutManager) layoutManager).getOrientation():((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                //最后一列或者不能整除的情况下最后一个
                if ((pos + 1) % spanCount == 0 /**|| pos==childCount-1*/){// 如果是最后一列
                    return true;
                }
            }else{
                if(pos>=childCount-spanCount && childCount%spanCount==0){
                    //整除的情况判断最后一整列
                    return true;
                }else if(childCount%spanCount!=0 && pos>=spanCount*(childCount/spanCount)){
                    //不能整除的情况只判断最后几个
                    return true;
                }
//                if(pos>=childCount-spanCount){
//                    return true;
//                }
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                //每一个都是第一列，也是最后一列
                return true;
            }else{
                if(pos==childCount-1){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = (layoutManager instanceof GridLayoutManager)?((GridLayoutManager) layoutManager).getOrientation():((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                if(pos>=childCount-spanCount && childCount%spanCount==0){
                    //整除的情况判断最后一整行
                    return true;
                }else if(childCount%spanCount!=0 && pos>=spanCount*(childCount/spanCount)){
                    //不能整除的情况只判断最后几个
                    return true;
                }
//                if(pos>=childCount-spanCount){
//                    return true;
//                }
            }else{
                //最后一行或者不能整除的情况下最后一个
                if ((pos + 1) % spanCount == 0 /**|| pos==childCount-1*/){// 如果是最后一行
                    return true;
                }
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                if(pos==childCount-1){
                    return true;
                }
            }else{
                //每一个都是第一行，也是最后一行
                return true;
            }
        }
        return false;
    }


    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public boolean isDrawBorderLine() {
        return drawBorderLine;
    }

    public void setDrawBorderLine(boolean drawBorderLine) {
        this.drawBorderLine = drawBorderLine;
    }
}
