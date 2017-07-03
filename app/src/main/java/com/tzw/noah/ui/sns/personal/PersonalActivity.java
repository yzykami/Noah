package com.tzw.noah.ui.sns.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.ListenedScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

/**
 * Created by yzy on 2017/7/3.
 */

public class PersonalActivity extends MyBaseActivity {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.scrollView)
    ListenedScrollView scrollView;
    @BindView(R.id.iv_head)
    SampleImageViewHead iv_head;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;

    Context mContext = PersonalActivity.this;

    int touchHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_personal);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        float bgHeight = getResources().getDimension(R.dimen.sns_personal_bg_height);
        float titleHeight = getResources().getDimension(R.dimen.title_height);
        int headHeight = Utils.dp2px(mContext, 80 / 2);
        touchHeight = (int) (bgHeight - titleHeight - headHeight);
    }

    private void findview() {

    }

    private void initview() {
        scrollView.setOnScrollListener(new ListenedScrollView.OnScrollListener() {
                                           @Override
                                           public void onBottomArrived() {

                                           }

                                           @Override
                                           public void onScrollStateChanged(ListenedScrollView view, int scrollState) {

                                           }

                                           @Override
                                           public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                                               Log.log("bbb", "x = " + l + ",y = " + t + ",th = " + touchHeight);
//                                               if (t <= touchHeight && t > 10) {
//                                                   int c = getResources().getColor(R.color.myRed);
//                                                   float x = (float) t;
//                                                   float y = (float) touchHeight;
//                                                   int a = (int) ((x / y) * 255);
//                                                   a <<= 6;
//                                                   a ^= c;
//                                                   final int aa =a;
//                                                   scrollView.post(new Runnable() {
//                                                       @Override
//                                                       public void run() {
//                                                           rl_top.setBackgroundColor(aa);
//                                                       }
//                                                   });
//                                               } else if (t > touchHeight)
//                                                   rl_top.setBackgroundColor(getResources().getColor(R.color.myRed));
//                                               else
//                                                   rl_top.setBackgroundColor(getResources().getColor(R.color.transParent));
                                           }
                                       }
        );
    }

    public void handle_more(View view) {
        startActivity(PersonalMoreActivity.class);
    }
}
