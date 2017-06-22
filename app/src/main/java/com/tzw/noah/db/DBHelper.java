package com.tzw.noah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tzw.noah.R;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.Utils;

/**
 * Created by yzy on 2017/6/13.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "noah.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = FileUtil.readRawFile(context, R.raw.noah);
        db.execSQL(sql);
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }

    public boolean tableIsExist(String tabName, SQLiteDatabase db) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {

            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public void createNewTable(String tablename, SQLiteDatabase db) {
        if (tableIsExist(tablename, db))
            return;
        FileUtil.copyDBFromRaw(context);
    }
}
