package com.tzw.noah.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.friendlist.FriendAdapter;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/12.
 */

public class DebugActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_view)
    ListView list_view;

    StringAdapter adapter;

    Context mContext = DebugActivity.this;
    String Tag = "DebugActivity";

    String title = "调试工具";
    boolean isFirstLoad = true;

    List<String> items;

    public static int TYPE_CRASH_LIST = 1001;
    public static int TYPE_CRASH_DETAIL_INDEX = 1002;
    public static int TYPE_CRASH_DETAIL_CONTENT = 1003;

    public static int TYPE_DATEBASE_LIST = 2001;
    public static int TYPE_DATEBASE_TABLE_LIST = 2002;
    public static int TYPE_DATEBASE_TABLE = 2003;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_list);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
//        selectPage = 0;
//        fragmentList = new Fragment[4];
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
        }
    }

    private void findview() {

    }

    private void initview() {
        tv_title.setText(title);
        items = new ArrayList<>();
        items.add("异常日志");
        items.add("数据库");
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
//                startActivity(PersonalActivity.class, bu);
                if (items.get(position).equals("异常日志")) {
                    startDetailActivity(mContext, "异常日志", TYPE_CRASH_LIST, null);
                }
                if (items.get(position).equals("数据库")) {
                    startDetailActivity(mContext, "数据库", TYPE_DATEBASE_LIST, null);
                }
            }
        });
        adapter = new StringAdapter(mContext, items);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(isFirstLoad) {
//            isFirstLoad=false;
//            refreshListView();
//        }
//        else
//            refreshListView2();
    }

    private void refreshListView() {
    }

    private void refreshListView2() {
    }

    public static void startDetailActivity(Context context, String title, int type, Object o) {
        Bundle bu = new Bundle();
        bu.putString("title", title);
        bu.putInt("type", type);
        bu.putSerializable("object", new DataWrapper(o));

        Intent intent = new Intent(context, DebugDetailActivity.class);
        if (bu != null)
            intent.putExtras(bu);
        context.startActivity(intent);
    }

    public static class DataWrapper implements Serializable {
        public Object data;

        public DataWrapper(Object object) {
            this.data = object;
        }
    }
}
