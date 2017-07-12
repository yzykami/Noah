package com.tzw.noah.ui.mine.setting.personal;

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
 * Created by yzy on 2017/6/22.
 */

public class WorkActivity extends MyBaseActivity {

    String TAG = "WorkActivity";
    WorkActivity mycontext = WorkActivity.this;
    private ListView list;

    List<Dict> workList;
    List<Boolean> selected;
    List<String> items;
    private NickNameAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;
    private String work = "";

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
        DBManager db = new DBManager(mycontext);
        workList = db.selectJobtList();
        work = getWorkbyID(UserCache.getUser().memberWork);
        selected = new ArrayList<>();
        items = new ArrayList<>();

        for (Dict dict : workList) {
            items.add(dict.dictionaryName);
            if (work.contains(dict.dictionaryName))
                selected.add(Boolean.TRUE);
            else
                selected.add(Boolean.FALSE);
        }
        adapter = new NickNameAdapter(mycontext, items, selected);
    }


    private void findview() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        list = (ListView) findViewById(R.id.list);

        list.setAdapter(adapter);
    }

    private void initview() {
        tv_title.setText("工作");


        adapter = new NickNameAdapter(mycontext, items, selected);
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
        final User user = UserCache.getUser();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberNickName", user.memberNickName));
        body.add(new Param("memberSex", user.memberSex));
        body.add(new Param("memberInterest", user.memberInterest));
        body.add(new Param("memberCharacter", user.memberCharacter));
        body.add(new Param("memberWork", getSelectWork()));
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
                    user.memberWork = getSelectWork();
                    UserCache.setUser(user);
                    toast("工作修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    private String getSelectWork() {
        String s = "";
        for (int i = 0; i < workList.size(); i++) {
            if (selected.get(i)) {
                s += workList.get(i).dictionaryId + ",";
            }
        }
        return s;
    }

    private String getWorkbyID(String memberInterest) {
        String s = "";
        String[] ss = memberInterest.split(",");
        for (String sss : ss) {
            if (!sss.isEmpty()) {
                for (Dict dict : workList) {
                    if (dict.dictionaryId.equals(sss)) {
                        if (s.isEmpty()) {
                            s += dict.dictionaryName;
                        } else
                            s += " " + dict.dictionaryName;
                    }
                }
            }
        }
        return s;
    }

}
