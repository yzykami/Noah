package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
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
 * Created by yzy on 2017/7/12.
 */

public class BlackListActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_view)
    ListView list_view;

    List<User> items = new ArrayList<>();

    FriendAdapter adapter;

    Context mContext = BlackListActivity.this;
    String Tag = "BlackListActivity";
//    private AssemblyRecyclerAdapter adapter;

//    int selectPage;
//    Fragment[] fragmentList = null;

    String title = "黑名单列表";

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

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
                startActivity(PersonalActivity.class, bu);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsBlacks(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<User>) iMsg.Data;
                        else
                            items = User.loadBlackList(iMsg);
                        items = Utils.processUser(items);
                        Collections.sort(items, new MyCompare());
                        if (items != null && items.size() > 0) {
                            adapter = new FriendAdapter(mContext, items);
                            list_view.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
