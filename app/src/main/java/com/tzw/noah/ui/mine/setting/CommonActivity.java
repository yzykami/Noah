package com.tzw.noah.ui.mine.setting;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.LoginActivity;
import com.tzw.noah.widgets.MyAlertDialog;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.DiskCache;

/**
 * Created by yzy on 2017/6/9.
 */

public class CommonActivity extends MyBaseActivity {

    String TAG = "CommonActivity";
    CommonActivity mycontext = CommonActivity.this;
    private TextView tv_size;
    private TextView tv_cache;
    private ImageView iv_msg;
    private ImageView iv_wifi;
    boolean msgbtn=true;
    boolean wifibtn=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_common);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        tv_size =(TextView)findViewById(R.id.tv_size);
        tv_cache =(TextView)findViewById(R.id.tv_cache);
        iv_msg =(ImageView)findViewById(R.id.iv_msg);
        iv_wifi =(ImageView)findViewById(R.id.iv_wifi);
    }

    private void initview() {
        final float[] floats = new float[]{0.85f, 1f, 1.15f, 1.3f};
        String[] ss = new String[]{"小", "标准", "大", "特大"};
        Configuration configuration = getResources().getConfiguration();
        float curFontScale = configuration.fontScale;
        for(int i=0;i<4;i++)
        {
            if(curFontScale==floats[i])
            {
                tv_size.setText(ss[i]);
            }
        }
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msgbtn)
                {
                    iv_msg.setImageResource(R.drawable.btn1_unselect);
                }
                else
                {
                    iv_msg.setImageResource(R.drawable.btn1_selected);
                }
                msgbtn=!msgbtn;
            }
        });

        iv_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifibtn)
                {
                    iv_wifi.setImageResource(R.drawable.btn1_unselect);
                }
                else
                {
                    iv_wifi.setImageResource(R.drawable.btn1_selected);
                }
                wifibtn=!wifibtn;
            }
        });

        DiskCache diskCache = Sketch.with(getBaseContext()).getConfiguration().getDiskCache();
        String usedSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getSize());
//        maxSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getMaxSize());
//        s3 = usedSizeFormat + "/" + maxSizeFormat;
        tv_cache.setText(usedSizeFormat);
    }

    private void doWorking() {

    }

    public void handle_login(View view) {
        Intent intent = new Intent(mycontext, LoginActivity.class);
        startActivity(intent);
    }

    public void handle_size(View view) {
        startActivity(CommonSizeActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initview();
    }

    public void handle_clean(View view) {

        MyAlertDialog.getInstance(this).setOnBtnClickListener(new MyAlertDialog.OnBtnClickListener() {
            @Override
            public void OnOkClick(MyAlertDialog alertDialog) {
                Sketch.with(getBaseContext()).getConfiguration().getDiskCache().clear();
                toast("缓存已清除");
                DiskCache diskCache = Sketch.with(getBaseContext()).getConfiguration().getDiskCache();
                String usedSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getSize());
                tv_cache.setText(usedSizeFormat);
            }
        }).show();
    }
}
