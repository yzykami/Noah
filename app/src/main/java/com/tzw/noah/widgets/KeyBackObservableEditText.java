package com.tzw.noah.widgets;

/**
 * Created by yzy on 2017-10-10.
 */

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class KeyBackObservableEditText extends AppCompatEditText {

    private OnBackPressedListener mListener;

    public KeyBackObservableEditText(Context context) {
        super(context);
    }

    public KeyBackObservableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyBackObservableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mListener != null) {
                if (mListener.onBackPressed()) {
                    return false;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }
}