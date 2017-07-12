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

public class CharacterActivity extends MyBaseActivity {

    String TAG = "CharacterActivity";
    CharacterActivity mycontext = CharacterActivity.this;
    private ListView list;

    List<Dict> characterList;
    List<Boolean> selected;
    List<String> items;
    private NickNameAdapter adapter;
    private ImageView iv_delete;
    private TextView tv_title;
    private String character = "";

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
        characterList = db.selectCharacterList();
        character = getCharacterbyID(UserCache.getUser().memberCharacter);
        selected = new ArrayList<>();
        items = new ArrayList<>();

        for (Dict dict : characterList) {
            items.add(dict.dictionaryName);
            if (character.contains(dict.dictionaryName))
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
        tv_title.setText("性格");


        adapter = new NickNameAdapter(mycontext, items, selected);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.set(position, !selected.get(position));
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
        body.add(new Param("memberCharacter", getSelectCharacter()));
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
                    user.memberCharacter = getSelectCharacter();
                    UserCache.setUser(user);
                    toast("性格修改成功");
                    finish();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    private String getSelectCharacter() {
        String s = "";
        for (int i = 0; i < characterList.size(); i++) {
            if (selected.get(i)) {
                s += characterList.get(i).dictionaryId + ",";
            }
        }
        return s;
    }

    private String getCharacterbyID(String memberCharacter) {
        String s = "";
        String[] ss = memberCharacter.split(",");
        for (String sss : ss) {
            if (!sss.isEmpty()) {
                for (Dict dict : characterList) {
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
