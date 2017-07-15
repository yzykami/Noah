package com.tzw.noah.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.net.Param;
import com.tzw.noah.ui.adapter.itemfactory.ChatListItemFactory;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/5.
 */

public class BottomPopupWindow extends PopupWindow {

    private TextView tv_cancel;
    LinearLayout ll;
    private OnItemClickListener onItemClickListener;
    Context mContext;
    Activity mActivity;
    View mView;
    WindowManager.LayoutParams param;
    List<Item> items;

    public static BottomPopupWindow create(Activity activity, OnItemClickListener onItemClickListener) {
        return new BottomPopupWindow(activity, onItemClickListener);
    }

    private BottomPopupWindow(Activity activity, OnItemClickListener onItemClickListener) {
        super(activity);
        mContext = activity;
        mActivity = activity;
        items = new ArrayList<>();

        this.onItemClickListener = onItemClickListener;
        param = mActivity.getWindow().getAttributes();

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.bottom_popupwindow, null);
        this.setContentView(mView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0666666);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());//dw);//
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.main).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                param.alpha = 1f;
                mActivity.getWindow().setAttributes(param);
            }
        });


        tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ll = (LinearLayout) mView.findViewById(R.id.ll);
    }

    public BottomPopupWindow addItem(String title) {
        return addItem(title, 0);
    }

    public BottomPopupWindow addItem(String title, int color) {
        Item item = new Item();
        item.title = title;
        item.color = color;
        items.add(item);
        return this;
    }

    public void show() {

        ll.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items.size(); i++) {
            View itemView = inflater.inflate(R.layout.bottom_popupwindow_item, null);
            TextView tv = (TextView) itemView.findViewById(R.id.tv);
            final Item item = items.get(i);
            tv.setText(item.title);
            if (item.color != 0) {
                tv.setTextColor(mContext.getResources().getColor(item.color));
            }
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(finalI, item.title);
                        dismiss();
                    }
                }
            });

            ll.addView(itemView);
            if (i != items.size() - 1) {
                View spanView = new View(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (mContext.getResources().getDimension(R.dimen.pt1) + 0.5f));
                spanView.setLayoutParams(layoutParams);
                spanView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_light));
                ll.addView(spanView);
            }
        }

        showAtLocation(mView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                param.alpha = 0.7f;
                mActivity.getWindow().setAttributes(param);
            }
        }, 200);
    }


    public interface OnItemClickListener {
        void OnItemClick(int position, String title);
    }

    public class Item {
        String title;
        int color;
    }
}
