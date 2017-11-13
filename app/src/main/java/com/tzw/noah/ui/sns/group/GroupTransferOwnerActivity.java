package com.tzw.noah.ui.sns.group;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.UserCache;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/17.
 */

public class GroupTransferOwnerActivity extends MyBaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.list_view)
    ListView list_view;
    GroupTransferOwnerActivity mContext = GroupTransferOwnerActivity.this;

    List<Boolean> selected;
    List<GroupMember> items;
    private GroupMemberSelectAdapter adapter;
    private ImageView iv_delete;
    String Tag = "GroupTransferOwnerActivity";
    Group group;
    Bundle bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_list);
        ButterKnife.bind(this);

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

        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
        list_view.addHeaderView(headSearchView);
    }

    private void initview() {

        tv_title.setText("转让群聊");
        tv_right.setText("确定");
        tv_right.setVisibility(View.VISIBLE);

        adapter = new GroupMemberSelectAdapter(mContext, items, selected);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < items.size(); i++) {
                    selected.set(i, Boolean.FALSE);
                }
                selected.set(position - 1, true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void doWorking() {

    }

    public void handle_save(View view) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                ids.add(items.get(i).memberNo);
            }
        }
        if (ids.size() == 0) {
            toast("请选择一个人");
            return;
        }
        new SnsManager(mContext).snsTransfer(group.groupId, ids.get(0), new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("群转让成功");
                        setResult(200);
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

                        items= DataCenter.getInstance().getGroupMemberList();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).memberNo == UserCache.getUser().memberNo)
                                items.remove(i);
                        }

                        selected = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            selected.add(Boolean.FALSE);
                        }
                        adapter = new GroupMemberSelectAdapter(mContext, items, selected);
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
}
