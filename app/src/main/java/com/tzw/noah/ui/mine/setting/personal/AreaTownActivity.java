package com.tzw.noah.ui.mine.setting.personal;

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

public class AreaTownActivity extends MyBaseActivity {

    String TAG = "AreaTownActivity";
    AreaTownActivity mycontext = AreaTownActivity.this;
    private TextView tv_area;
    private ListView list;

    List<Boolean> selected;
    List<Area> items;
    private AreaAdapter adapter;
    private String pid;

    int selectedId=0;

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
        List<Area> arealist = db.queryTown(pid);
        items = arealist;

        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_twon);
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
        adapter = new AreaAdapter(mycontext, items, selected, AreaAdapter.mode_twon);
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
                selectedId = Utils.String2Int(items.get(position).areaId);
                handle_save(null);
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
        body.add(new Param("areaId", selectedId));
        body.add(new Param("memberIntroduce", user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    user.areaId=selectedId;
                    UserCache.setUser(user);
                    toast("地区修改成功");
                    setResult(100);
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
