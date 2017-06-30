package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;

/**
 * Created by yzy on 2017/6/30.
 */

public class BookActivity extends MyBaseActivity {


    @BindView(R.id.list_view)
    ListView list_view;

    Context mContext = BookActivity.this;

    List<SnsPerson> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_book);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
    }

    private void findview() {

    }

    private void initview() {
        List<String> namelist = new ArrayList<>();
        namelist.add("你111");
        namelist.add("你好在");
        namelist.add("耐111");
        namelist.add("废柴");
        namelist.add("风");
        namelist.add("银");
        namelist.add("辛巴");
        namelist.add("2342辛巴");
        namelist.add("啦啦");
        namelist.add("❤啦啦");
        namelist.add("OMG呵呵");
        namelist.add("ddd呵呵");

        items = new ArrayList<>();
        for (String name : namelist) {
            SnsPerson p = new SnsPerson();
            p.name = name;
            p.namePingyin = Utils.getLetter(name);
            p.shortCut = "5个好友待添加";
            items.add(p);
        }

        Collections.sort(items, new MyCompare());

        AddAdapter adapter = new AddAdapter(mContext, items);

        list_view.setAdapter(adapter);

        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
//        TextView tv = (TextView) headSearchView.findViewById(R.id.tv);
//        tv.setText("手机号/微信号");
        list_view.addHeaderView(headSearchView);

        View headSpanView = new View(mContext);
        headSpanView.setBackgroundColor(getResources().getColor(R.color.textLightGray));
        ViewGroup.LayoutParams params = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);

        headSpanView.setLayoutParams(params);
        list_view.addHeaderView(headSpanView);


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.log("itemclick", "view = " + view + ",position = " + position + ",id = " + id);
                //search
                if (position == 0) {

                }
                //附近的人
                else if (position == 1) {

                }
                //通讯录
                else if (position == 2) {

                }
                //扫一扫
                else if (position == 3) {

                } else if (position >= 4) {

                }
            }
        });
    }


    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }


    private void doWorking() {
    }

}
