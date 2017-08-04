package com.tzw.noah.ui.sns.relationrecord;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Notification;
import com.tzw.noah.models.RelationRecord;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.notification.NotificationAdapter;
import com.tzw.noah.ui.sns.notification.NotificationCompare;
import com.tzw.noah.ui.sns.notification.NotificationDetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/4.
 */

public class RelationRecordListActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_view)
    ListView list_view;

    List<RelationRecord> items = new ArrayList<>();

    RelationRecordAdapter adapter;

    Context mContext = RelationRecordListActivity.this;
    static RelationRecordListActivity instance;
    String Tag = "RelationRecordListActivity";
//    private AssemblyRecyclerAdapter adapter;

//    int selectPage;
//    Fragment[] fragmentList = null;

    String title = "个人系统消息";

    public static RelationRecordListActivity getInstance() {
        if (instance == null) {
            instance = new RelationRecordListActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_list);
        ButterKnife.bind(this);
        instance = this;
        initdata();
        findview();
        initview();

        refreshListView();
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

//        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bu = new Bundle();
//                bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
//                bu.putInt("DATA2", position - list_view.getHeaderViewsCount());
//                startActivity(NotificationDetailActivity.class, bu);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsRelationRecords(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<RelationRecord>) iMsg.Data;
                        else
                            items = RelationRecord.loadList(iMsg);
                        if (items == null)
                            items = new ArrayList<RelationRecord>();
//                        Collections.sort(items, new NotificationCompare());
//                        Collections.reverse(items);
                        adapter = new RelationRecordAdapter(mContext, items);
                        list_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    public void updateListItem(int pos, RelationRecord item) {
        adapter.updateItem(pos, item);
    }
}
