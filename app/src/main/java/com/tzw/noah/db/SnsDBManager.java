package com.tzw.noah.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tzw.noah.models.MyRelationship;
import com.tzw.noah.models.User;

import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class SnsDBManager {
    private SnsDBHelper helper;
    private SQLiteDatabase db;
    Context context;
    String UserTableName = "MemberInfo";
    String FriendListTableName = "MyFriends";

    public SnsDBManager(Context context) {
        this.context = context;
        helper = new SnsDBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public List<User> getSnsFriendList() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from MyFriends)";
        return helper.queryAll(User.class, sql);
    }

    public void UpdateFriendList(List<User> list) {
        for (User user : list)
            updateUser(user);
        helper.insert(MyRelationship.createList(list), FriendListTableName);
    }

    public void updateUser(User user) {
        helper.insertAndUpdate(user, UserTableName);
    }
}
