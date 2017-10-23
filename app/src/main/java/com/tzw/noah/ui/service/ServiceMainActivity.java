package com.tzw.noah.ui.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.timeselector.Utils.ScreenUtil;

/**
 * Created by yzy on 2017/6/8.
 */

public class ServiceMainActivity extends MyBaseActivity {
    int index = 0;
    Bitmap bm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_main);
//        final ImageView iv = (ImageView) findViewById(R.id.iv);
//        final int[] ivResids = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11};
//        int screenWidth = Utils.getScreenWidth();
//        ViewGroup.LayoutParams lp = iv.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        iv.setLayoutParams(lp);
//
//        iv.setMaxWidth(screenWidth);
//        iv.setMaxHeight(screenWidth * 10);
//        final int maxcount = 2;//ivResids.length
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                index++;
//                if (index >= maxcount)
//                    index %= maxcount;
//                iv.setImageBitmap(null);
//                if (bm != null)
//                    bm.recycle();
//                bm = null;
//                bm = BitmapFactory.decodeResource(getResources(), ivResids[index]);
//                iv.setImageBitmap(bm);
//            }
//        });

    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }
}
