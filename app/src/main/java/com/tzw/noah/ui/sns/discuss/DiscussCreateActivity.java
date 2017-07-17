package com.tzw.noah.ui.sns.discuss;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.UserItemSelectAdapter;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/7/13.
 */

public class DiscussCreateActivity extends MyBaseActivity {

    String Tag = "DiscussCreateActivity";
    DiscussCreateActivity mContext = DiscussCreateActivity.this;
    private ListView list;

    List<Boolean> selected;
    List<User> items;
    private UserItemSelectAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_discuss_create);


        initdata();
        findview();
        initview();
        refreshListView();
    }

    private void initdata() {
        DBManager db = new DBManager(mContext);
        selected = new ArrayList<>();
        items = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
    }


    private void findview() {
        list = (ListView) findViewById(R.id.list);

//        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
//        list.addHeaderView(headSearchView);
//
//        list.setAdapter(adapter);

    }

    private void initview() {

        adapter = new UserItemSelectAdapter(mContext, items, selected);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.set(position, !selected.get(position));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void handle_save(View view) {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                ids.add(items.get(i).memberNo + "");
            }
        }
        if (ids.size() == 0) {
            toast("需要至少选择一个人");
            return;
        }
        new SnsManager(mContext).snsCreateDiscuss(ids, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("多人会话创建成功");
                        setResult(100);
                        finish();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void refreshListView() {
        new SnsManager(mContext).snsFriends(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<User>) iMsg.Data;
                        else
                            items = User.loadFriendList(iMsg);
                        items = Utils.processUser(items);
                        Collections.sort(items, new MyCompare());
                        if (items != null && items.size() > 0) {
                            selected = new ArrayList<>();
                            for (int i = 0; i < items.size(); i++) {
                                selected.add(Boolean.FALSE);
                            }
                            adapter = new UserItemSelectAdapter(mContext, items, selected);
                            list.setAdapter(adapter);
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
