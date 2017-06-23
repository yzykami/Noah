package com.tzw.noah.ui.mine.setting;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/22.
 */

public class CommonSizeActivity extends MyBaseActivity {

    String TAG = "CommonSizeActivity";
    CommonSizeActivity mycontext = CommonSizeActivity.this;

    private LinearLayout ll;
    float curSize;
    private float curFontScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_commonsize);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
//        sex =UserCache.getUser().memberSex;
        Configuration configuration = getResources().getConfiguration();
        curFontScale = configuration.fontScale;
    }

    private void findview() {
        ll = (LinearLayout) findViewById(R.id.ll);

    }

    private void initview() {

        for (int i = 0; i < 4; i++) {
            ll.addView(getItemView(i));
        }

    }

    View getItemView(final int index) {
        final float[] floats = new float[]{0.85f, 1f, 1.15f, 1.3f};
        String[] ss = new String[]{"小", "标准", "大", "特大"};
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(mycontext).inflate(R.layout.mine_settting_personal_nickname_item, null);
        TextView tv = (TextView) rl.findViewById(R.id.tv);
        ImageView iv = (ImageView) rl.findViewById(R.id.iv);
        tv.setText(ss[index]);
        iv.setVisibility(View.GONE);
        if(floats[index]==curFontScale)
        {
            iv.setVisibility(View.VISIBLE);
        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFontScale(index);
                finish();
//                mconfig.fontScale = floats[index];
//                setFontSize();
            }
        });
        return rl;
    }

    private void doWorking() {

    }

    public void handle_save(View view) {

    }

    private void initFontScale(int index) {
        float[] floats = new float[]{0.85f, 1f, 1.15f, 1.3f};
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = floats[index];
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

}
