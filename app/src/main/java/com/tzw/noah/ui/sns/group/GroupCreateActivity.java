package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupType;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.BottomPopupWindow;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.friendlist.BlackListActivity;
import com.tzw.noah.ui.sns.friendlist.FriendAdapter;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/3.
 */

public class GroupCreateActivity extends MyBaseActivity {
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.list_view)
    ListView list_view;

    Context mContext = GroupCreateActivity.this;
    private List<GroupType> items;

    GroupTypeAdapter adapter;

    String Tag = "GroupCreateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_create);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
//        Bundle bu = getIntent().getExtras();
//        if (bu != null) {
//            items = (List<GroupType>) bu.getSerializable("DATA");
//        }
//        else
        items = new ArrayList<>();
        adapter = new GroupTypeAdapter(mContext, items);
        list_view.setAdapter(adapter);
    }

    private void findview() {

    }

    private void initview() {
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupType gt = items.get(position - list_view.getHeaderViewsCount());
                Bundle bu = new Bundle();
                if (gt.getSonList().size() == 0) {
                    Group group = new Group();
                    group.groupTypeId = gt.groupTypeId;
                    bu.putSerializable("DATA", group);
                    startActivityForResult(100, GroupCreateActivity2.class, bu);
                } else {
                    bu.putSerializable("DATA", gt);
                    startActivityForResult(100, GroupTypeSelectActivity.class, bu);
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            setResult(100);
            finish();
        }
    }

    public void handle_next(View view) {
        startActivityForResult(100, GroupCreateActivity2.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsGroupType(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        items = GroupType.getFirstNode(iMsg).getSonList();
                        if (items != null && items.size() > 0) {
                            adapter = new GroupTypeAdapter(mContext, items);
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
