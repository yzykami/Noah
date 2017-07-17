package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.models.Dict;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.personal.NickNameAdapter;
import com.tzw.noah.ui.sns.StringSelectAdapter;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/7/17.
 */

public class GroupEditJoinModeActivity extends MyBaseActivity {

    String TAG = "InterestActivity";
    GroupEditJoinModeActivity mycontext = GroupEditJoinModeActivity.this;
    private ListView list;

    List<Boolean> selected;
    List<String> items;
    private StringSelectAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;
    private String interest = "";
    Group group;
    Context mContext = GroupEditJoinModeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_mutiselect);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
        selected = new ArrayList<>();
        items = new ArrayList<>();
        items.add("不用验证");
        items.add("需要验证");
        items.add("不允许任何人加入");

        for (String s : items) {
            selected.add(Boolean.FALSE);
        }
        if (group.joinmode < 3)
            selected.set(group.joinmode, true);
        adapter = new StringSelectAdapter(mycontext, items, selected);
    }


    private void findview() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        list = (ListView) findViewById(R.id.list);

        list.setAdapter(adapter);
    }

    private void initview() {
        tv_title.setText("选择加群方式");


        adapter = new StringSelectAdapter(mycontext, items, selected);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < items.size(); i++) {
                    selected.set(i, Boolean.FALSE);
                }
                selected.set(position, true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void doWorking() {

    }

    public void handle_save(View view) {
        int selectedIndex = -1;
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                selectedIndex = i;
            }
        }
        if (selectedIndex == -1) {
            toast("请选择一种方式");
            return;
        }
        final int joinmode = selectedIndex;
        List<Param> body = new ArrayList<>();
        body.add(new Param("joinmode", selectedIndex));
        new SnsManager(mContext).snsSettingOfGroup(group.groupId, body, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    group.joinmode = joinmode;
                    toast("加群验证方式修改成功");
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", group);
                    Intent intent = new Intent();
                    intent.putExtras(bu);
                    setResult(100, intent);
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
