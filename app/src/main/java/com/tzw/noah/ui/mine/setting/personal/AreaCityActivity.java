package com.tzw.noah.ui.mine.setting.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class AreaCityActivity extends MyBaseActivity {

    String TAG = "AreaCityActivity";
    AreaCityActivity mycontext = AreaCityActivity.this;
    private TextView tv_area;
    private ListView list;

    List<Boolean> selected;
    List<Area> items;
    private AreaAdapter adapter;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_areacity);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {

        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            pid = bu.getString("ID");
        }


        items = new ArrayList<>();
        selected = new ArrayList<>();

        DBManager db = new DBManager(mycontext);
        List<Area> arealist = db.queryCity(pid);
        items = arealist;

        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_city);
    }

    private void findview() {
//        tv_area = (TextView) findViewById(R.id.tv_area);
        list = (ListView) findViewById(R.id.list);

        list.setAdapter(adapter);
    }

    private void initview() {
//        tv_area.setText(getArea(UserCache.getUser().areaId));

        selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_city);
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
                Bundle bu = new Bundle();
                bu.putString("ID", items.get(position).areaId);
                startActivityForResult(100, AreaTownActivity.class, bu);
            }
        });
    }

    private String getArea(int areaId) {
        String area = "";
        return area;
    }


    private void doWorking() {

    }


    public void handle_login(View view) {
    }

    public void handle_save(View view) {
        final User user = UserCache.getUser();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNickName", user.memberNickName));
        body.add(new Param("memberSex", user.memberSex));
        body.add(new Param("memberInterest", user.memberInterest));
        body.add(new Param("memberCharacter", user.memberCharacter));
        body.add(new Param("memberWork", user.memberWork));
        body.add(new Param("areaId", user.areaId));
        body.add(new Param("memberIntroduce", user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    UserCache.setUser(user);
                    toast("昵称修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
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
}
