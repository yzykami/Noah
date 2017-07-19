package com.tzw.noah.ui.sns.notification;

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
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.add.AddAdapter;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/13.
 */

public class NotificationListActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_view)
    ListView list_view;

    List<Notification> items = new ArrayList<>();

    NotificationAdapter adapter;

    Context mContext = NotificationListActivity.this;
    static NotificationListActivity instance;
    String Tag = "NotificationListActivity";
//    private AssemblyRecyclerAdapter adapter;

//    int selectPage;
//    Fragment[] fragmentList = null;

    String title = "我的群组信息通知";

    public static NotificationListActivity getInstance() {
        if (instance == null) {
            instance = new NotificationListActivity();
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

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
                bu.putInt("DATA2", position - list_view.getHeaderViewsCount());
                startActivity(NotificationDetailActivity.class, bu);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsGroupNotification(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<Notification>) iMsg.Data;
                        else
                            items = Notification.loadList(iMsg);
                        if (items == null)
                            items = new ArrayList<Notification>();
                        Collections.sort(items, new NotificationCompare());
                        Collections.reverse(items);
                        adapter = new NotificationAdapter(mContext, items);
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

    public void updateListItem(int pos, Notification item) {
        adapter.updateItem(pos, item);
    }
}
