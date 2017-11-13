package com.tzw.noah.utils;

import android.app.Application;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.tzw.noah.AppContext;
import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;

/**
 * Created by yzy on 2017-11-03.
 */

public class VCodeCountDownTimer {
    static VCodeCountDownTimer instance;

    private OnCountDownListener mOnCountDownListener;

    CountDownTimer cdt;
    int remainTime;
    TextView tv_getcode;

    public interface OnCountDownListener {
        void onTick(long untilFinished);

        void onFinish();
    }

    public static VCodeCountDownTimer getInstance() {
        if (instance == null)
            instance = new VCodeCountDownTimer();
        return instance;
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        mOnCountDownListener = listener;
    }

    public boolean isClickable() {
        if (remainTime == 0)
            return true;
        return false;
    }

    public void start(TextView tv) {
        remainTime = 60 * 1000;
        run(tv);
    }

    public void start(TextView tv, int remainTime) {
        this.remainTime = remainTime * 1000;
        run(tv);
    }

    public void run(TextView tv) {
        tv_getcode = tv;
        if (remainTime > 0) {
            tv_getcode.setText("重新发送(" + remainTime + "s)");
            tv_getcode.setTextColor(AppContext.getContext().getResources().getColor(R.color.textLightGray));
            tv_getcode.setBackgroundResource(R.drawable.bg_gray_fill_round);
        } else {
            tv_getcode.setText("获取验证码");
            tv_getcode.setTextColor(AppContext.getContext().getResources().getColor(R.color.myRed));
            tv_getcode.setBackgroundResource(R.drawable.bg_red_border_round);
        }
        cdt = new CountDownTimer(remainTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainTime = (int) (millisUntilFinished / 1000);
                if (mOnCountDownListener != null) {
                    onTick(remainTime);
                }
                if (tv_getcode != null) {
                    tv_getcode.setText("重新发送(" + remainTime + "s)");
                    tv_getcode.setTextColor(AppContext.getContext().getResources().getColor(R.color.textLightGray));
                    tv_getcode.setBackgroundResource(R.drawable.bg_gray_fill_round);
                }
            }

            @Override
            public void onFinish() {
                remainTime = 0;
                if (mOnCountDownListener != null) {
                    onFinish();
                }
                if (tv_getcode != null) {
                    tv_getcode.setText("获取验证码");
                    tv_getcode.setTextColor(AppContext.getContext().getResources().getColor(R.color.myRed));
                    tv_getcode.setBackgroundResource(R.drawable.bg_red_border_round);
                }
            }
        }.start();
    }
}
