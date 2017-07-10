package com.tzw.noah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.R;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.Dict;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class SnsDBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    Context context;

    public SnsDBManager(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public List<User> getSnsFriendList() {
        String sql = "select * from memberInfo where memberNo in(select memberNo from MyFriend)";
        return helper.queryAll(User.class, sql);
    }

    public void UpdateFriendList(List<User> list) {
        helper.insert(list, "memberInfo");
    }
}
