package com.tzw.noah.ui.sns.group;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.MultiSelectAdapter;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/15.
 */

public class GroupAddMemberActivity extends MyBaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    String Tag = "GroupAddMemberActivity";
    GroupAddMemberActivity mContext = GroupAddMemberActivity.this;
    private ListView list;

    List<Boolean> selected;
    List<User> items;
    Map<Integer, User> memberItems;
    private MultiSelectAdapter adapter;
    private ImageView iv_delete;
    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_discuss_create);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        refreshListView();
    }

    private void initdata() {
        DBManager db = new DBManager(mContext);
        List<GroupMember> items2 = null;
        selected = new ArrayList<>();
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            items2 = (List<GroupMember>) bu.getSerializable("DATA");
            group = (Group) bu.getSerializable("DATA2");
        }
        if (items2 == null) {
            items2 = new ArrayList<>();
            group = new Group();
        }

        memberItems = new HashMap<>();
        for (int i = 0; i < items2.size(); i++) {
            GroupMember gm = items2.get(i);
            User u = new User();
            u.memberNo = gm.memberNo;
            u.memberNickName = gm.getMemberName();
            u.memberHeadPic = gm.memberHeadUrl;
            memberItems.put(u.memberNo, u);
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
        tv_title.setText("邀请群成员");
        tv_right.setText("确定");
        adapter = new MultiSelectAdapter(mContext, items, selected);
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
        new SnsManager(mContext).snsAddUsersToGroup(group.groupId, ids, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("邀请已发送");
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
                        List<User> friendList;
                        if (iMsg.Data != null)
                            friendList = (List<User>) iMsg.Data;
                        else
                            friendList = User.loadFriendList(iMsg);
                        friendList = Utils.processUser(friendList);
                        Collections.sort(friendList, new MyCompare());
                        if (friendList != null && friendList.size() > 0) {
                            selected = new ArrayList<>();
                            items = new ArrayList<User>();
                            for (int i = 0; i < friendList.size(); i++) {
                                if (memberItems.containsKey(friendList.get(i).memberNo)) {
                                    continue;
                                }
                                items.add(friendList.get(i));
                                selected.add(Boolean.FALSE);
                            }
                            adapter = new MultiSelectAdapter(mContext, items, selected);
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
