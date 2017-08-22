package com.tzw.noah.ui.sns.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.add.AddAdapter;
import com.tzw.noah.ui.sns.add.AddGroupAdapter;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/17.
 */

public class GroupSearchActivity extends MyBaseActivity implements AddGroupAdapter.OnAddClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_view)
    ListView list_view;

    List<Group> items = new ArrayList<>();

    AddGroupAdapter adapter;

    Context mContext = GroupSearchActivity.this;
    GroupSearchActivity instance;
    String Tag = "GroupSearchActivity";
//    private AssemblyRecyclerAdapter adapter;

//    int selectPage;
//    Fragment[] fragmentList = null;

    String title = "好友搜索";
    private View headSearchView;
    private EditText et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_list);
        ButterKnife.bind(this);
        instance = this;
        initdata();
        findview();
        initview();

//        refreshListView();
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
        headSearchView = View.inflate(this,R.layout.sns_search_head2, null);
        list_view.addHeaderView(headSearchView);
        et = (EditText) headSearchView.findViewById(R.id.et);

    }

    private void initview() {
        tv_title.setText(title);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
                startActivity(GroupDetailActivity.class, bu);

            }
        });
        items=new ArrayList<>();
        adapter = new AddGroupAdapter(mContext, items);
        adapter.setOnAddClickListener(instance);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    doSearch();
                    showKeyboard(false);
                    return true;
                } else {
                    return false;
                }
            }
        });
        showKeyboardDelayed(et);
    }

    private void doSearch() {
        String searchkey = et.getText().toString();
        NetHelper.getInstance().snsFindGroup(searchkey, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    List<Group> groups =  Group.loadSearchList(iMsg);
                    items = new ArrayList<Group>();
                    items.addAll(groups);
//                    items = Utils.processUser(items);
//                    Collections.sort(items, new MyCompare());
                    adapter = new AddGroupAdapter(mContext, items);
                    List<Group> groupList = DataCenter.getInstance().getGroupList();
                    for (Group g : groupList) {
                        for (int i=0; i<groups.size(); i++) {
                            Group curUser = groups.get(i);
                            if (g.groupId == curUser.groupId)
                                curUser.isJoined = true;
                        }
                    }
                    adapter.setOnAddClickListener(instance);
                    list_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onAddClick(View v, int position) {
        toast("sdda" + position);
    }
}
