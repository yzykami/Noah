package com.tzw.noah.ui.sns.group;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/22.
 */

public class GroupSelectAdminActivity extends MyBaseActivity {

    GroupSelectAdminActivity mContext = GroupSelectAdminActivity.this;
    private ListView list;

    List<Boolean> selected;
    List<GroupMember> items;
    private GroupMemberSelectAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;
    String Tag = "GroupSetAdminActivity";
    Group group;
    Bundle bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_selectadmin);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        DBManager db = new DBManager(mContext);
        selected = new ArrayList<>();
        items = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new GroupMemberSelectAdapter(mContext, items, selected);

        bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
    }


    private void findview() {
        list = (ListView) findViewById(R.id.list);

        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
        list.addHeaderView(headSearchView);
    }

    private void initview() {

        adapter = new GroupMemberSelectAdapter(mContext, items, selected);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.set(position - 1, !selected.get(position - 1));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void doWorking() {

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
        new SnsManager(mContext).snsAddManagersToGroup(group.groupId, ids, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("设置管理员成功");
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

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        items= DataCenter.getInstance().getMemberList();

                        selected = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            selected.add(Boolean.FALSE);
                        }
                        adapter = new GroupMemberSelectAdapter(mContext, items, selected);
                        list.setAdapter(adapter);
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
}
