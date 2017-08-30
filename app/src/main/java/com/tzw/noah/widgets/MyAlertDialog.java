package com.tzw.noah.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.AppContext;
import com.tzw.noah.R;

/**
 * Created by yzy on 2017/8/30.
 */

public class MyAlertDialog {

    static final int BTN_OK_CANCEL = 0;
    static final int TYPE_DELETE = 0;
    private Activity mActivity;

    MyAlertDialog instance;
    int mode = 0;
    int type = 0;
    String title = "";
    String text="";

    int resId = R.layout.dialog_prompt_two_button;
    AlertDialog mAlertDialog;

    public interface OnBtnClickListener {
        public void OnOkClick(MyAlertDialog alertDialog);
    }

    OnBtnClickListener mOnBtnClickListener;

    public static MyAlertDialog getInstance(Activity activity) {
        MyAlertDialog ins =new MyAlertDialog();
        ins.mActivity =activity;
        return ins;
    }

    public MyAlertDialog setBtnMode(int mode) {
        this.mode = mode;
        return this;
    }

    public MyAlertDialog setType(int type) {
        this.type = type;
        return this;
    }

    public MyAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    public MyAlertDialog setText(String text) {
        this.text = text;
        return this;
    }

    public MyAlertDialog setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
        return this;
    }

    public void show() {
        instance = this;
        if (text.equals("")) {
            if (type == TYPE_DELETE) {
                text = "确认删除!";
            }
        }
        mAlertDialog = new AlertDialog.Builder(mActivity).create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setTitle("删除");
        mAlertDialog.getWindow().setContentView(resId);
        ((TextView) mAlertDialog.getWindow().findViewById(R.id.textView_two_prompt_text)).setText(text);

        mAlertDialog.getWindow().findViewById(R.id.textview_two_prompt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBtnClickListener != null)
                    mOnBtnClickListener.OnOkClick(instance);
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.getWindow().findViewById(R.id.textview_two_prompt_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });
//        return this;
    }

}
