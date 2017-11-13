package com.tzw.noah.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing1.demoscaner.QrCodeActivity;
import com.google.zxing1.demoscaner.WeChatCaptureActivity;
import com.tzw.noah.R;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.home.scan.ActivityScanerCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public static int TYPE_NETTOOL = 1001;

    public static int TYPE_SYSTEMPARAMS = 2001;

    public static int TYPE_SYSTEMCACHE = 3001;

    public static int TYPE_CRASH_LIST = 4001;
    public static int TYPE_CRASH_DETAIL_INDEX = 4002;
    public static int TYPE_CRASH_DETAIL_CONTENT = 4003;

    public static int TYPE_THIRDPARTY = 5001;

    public static int TYPE_DATEBASE_LIST = 6001;
    public static int TYPE_DATEBASE_TABLE_LIST = 6002;
    public static int TYPE_DATEBASE_TABLE = 6003;

    public static int TYPE_SYSTEMSWITCH = 7001;


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
        items.add("网络工具");
        items.add("系统参数");
        items.add("缓存相关");
        items.add("异常日志");
        items.add("第三方服务");
        items.add("数据库");
        items.add("系统配置");
//        items.add("扫码1");
//        items.add("扫码2");
//        items.add("扫码3");
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
                if (items.get(position).equals("网络工具")) {
                    startDetailActivity(mContext, "网络工具", TYPE_NETTOOL, null);
                }
                if (items.get(position).equals("系统参数")) {
                    startDetailActivity(mContext, "系统参数", TYPE_SYSTEMPARAMS, null);
                }
                if (items.get(position).equals("第三方服务")) {
                    startDetailActivity(mContext, "第三方服务", TYPE_THIRDPARTY, null);
                }
                if (items.get(position).equals("缓存相关")) {
                    startDetailActivity(mContext, "缓存相关", TYPE_SYSTEMCACHE, null);
                }
                if (items.get(position).equals("系统配置")) {
                    startDetailActivity(mContext, "系统配置", TYPE_SYSTEMSWITCH, null);
                }
//                if (items.get(position).equals("扫码1")) {
//                    startActivity2(ActivityScanerCode.class);
//                }
//                if (items.get(position).equals("扫码2")) {
//                    startActivity2(WeChatCaptureActivity.class);
//                }
//                if (items.get(position).equals("扫码3")) {
//                    startActivity2(QrCodeActivity.class);
//                }
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
