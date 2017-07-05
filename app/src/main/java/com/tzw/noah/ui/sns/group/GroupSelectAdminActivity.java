package com.tzw.noah.ui.sns.group;

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
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.setting.personal.NickNameAdapter;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/22.
 */

public class GroupSelectAdminActivity extends MyBaseActivity {

    String TAG = "GroupSelectAdminActivity";
    GroupSelectAdminActivity mContext = GroupSelectAdminActivity.this;
    private ListView list;

    List<Boolean> selected;
    List<SnsPerson> items;
    private GroupSelectAdminAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;

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
        items = Utils.makeData();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new GroupSelectAdminAdapter(mContext, items, selected);
    }


    private void findview() {
        list = (ListView) findViewById(R.id.list);

        View headSearchView = getLayoutInflater().inflate(R.layout.sns_search_head, null, false);
        list.addHeaderView(headSearchView);

        list.setAdapter(adapter);

    }

    private void initview() {

        adapter = new GroupSelectAdminAdapter(mContext, items, selected);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.set(position-1, !selected.get(position-1));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void doWorking() {

    }

    public void handle_save(View view) {
//        final User user = UserCache.getUser();
//        List<Param> body = new ArrayList<>();
//        body.add(new Param("memberNickName", user.memberNickName));
//        body.add(new Param("memberSex", user.memberSex));
//        body.add(new Param("memberInterest", user.memberInterest));
//        body.add(new Param("memberCharacter", getSelectCharacter()));
//        body.add(new Param("memberWork", user.memberWork));
//        body.add(new Param("areaId", user.areaId));
//        body.add(new Param("memberIntroduce", user.memberIntroduce));
//        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
//        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                toast(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                if (iMsg.isSucceed()) {
//                    user.memberCharacter = getSelectCharacter();
//                    UserCache.setUser(user);
//                    toast("性格修改成功");
//                    finish();
//                } else {
//                    toast(iMsg.getMsg());
//                }
//            }
//        });
    }
}
