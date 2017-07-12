package com.tzw.noah.ui.mine.setting.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
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

public class NickNameActivity extends MyBaseActivity {

    String TAG = "NickNameActivity";
    NickNameActivity mycontext = NickNameActivity.this;
    private EditText et_nickname;
    private ListView list;

    List<Boolean> selected;
    List<String> items;
    private NickNameAdapter adapter;
    private ImageView iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_settting_personal_nickname);


        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        items = new ArrayList<>();
        selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new NickNameAdapter(mycontext, items, selected);
    }

    private void findview() {
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        iv_delete = (ImageView) findViewById(R.id.iv_male);
        list = (ListView) findViewById(R.id.list);

        list.setAdapter(adapter);
    }

    private void initview() {
        et_nickname.setText(UserCache.getUser().memberNickName);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nickname.setText("");
            }
        });

        selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            selected.add(Boolean.FALSE);
        }
        adapter = new NickNameAdapter(mycontext, items, selected);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < items.size(); i++) {
                    selected.set(i,Boolean.FALSE);
                }
                et_nickname.setText(items.get(position));
                selected.set(position,true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void doWorking() {

        for (int i = 0; i < 1; i++)
            getNickName();
    }

    private void getNickName() {
        NetHelper.getInstance().memberNicknames(new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                return;
            }

            @Override
            public void onResponse(IMsg iMsg) {
                String nickname = iMsg.getValue("nickName");
                items.add(nickname);
                initview();
            }
        });
    }

    public void handle_login(View view) {
    }

    public void handle_save(View view) {
        final User user =UserCache.getUser();
        final String nickname=et_nickname.getText().toString();
        List<Param> body=new ArrayList<>();
        body.add(new Param("memberNickName",nickname));
        body.add(new Param("memberSex",user.memberSex));
        body.add(new Param("memberInterest",user.memberInterest));
        body.add(new Param("memberCharacter",user.memberCharacter));
        body.add(new Param("memberWork",user.memberWork));
        body.add(new Param("areaId",user.areaId));
        body.add(new Param("memberIntroduce",user.memberIntroduce));
        body.add(new Param("memberBirthday", Utils.String2Timestamp(user.memberBirthday)));
        NetHelper.getInstance().setUserInfo(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if(iMsg.isSucceed())
                {
                    user.memberNickName=nickname;
                    UserCache.setUser(user);
                    toast("昵称修改成功");
                    finish();
                }
                else
                {
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
