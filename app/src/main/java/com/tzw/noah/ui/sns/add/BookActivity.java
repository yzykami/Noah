package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/30.
 */

public class BookActivity extends MyBaseActivity {


    @BindView(R.id.list_view)
    ListView list_view;

    Context mContext = BookActivity.this;

    List<User> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_list);
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
        items = new ArrayList<>();

        Collections.sort(items, new MyCompare());

        AddAdapter adapter = new AddAdapter(mContext, items);

        list_view.setAdapter(adapter);

        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
//        TextView tv_name = (TextView) headSearchView.findViewById(R.id.tv_name);
//        tv_name.setText("手机号/微信号");
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


    private void doWorking() {
    }

}
