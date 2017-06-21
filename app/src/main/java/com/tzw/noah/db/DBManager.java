package com.tzw.noah.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.R;
import com.tzw.noah.models.Area;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    Context context;

    public DBManager(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void createAreaTable() {
        helper.createNewTable("Area", db);
    }

    /**
     * add Areas
     *
     * @param Areas
     */
    public void add(List<Area> Areas) {
        db.beginTransaction();  //开始事务
        try {
            for (Area Area : Areas) {
                db.execSQL("INSERT INTO Area VALUES(null, ?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{Area.areaId, Area.areaPid, Area.areaName, Area.areaLevel, Area.areaCode, Area.areaPostCode, Area.areaTelCode, Area.areaSort, Area.areaShortName, Area.areaSpell, Area.areaShortSpell, Area.areaLng, Area.areaLat});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

//    /**
//     * update Area's age
//     * @param Area
//     */
//    public void updateAge(Area Area) {
//        ContentValues cv = new ContentValues();
//        cv.put("age", Area.age);
//        db.update("Area", cv, "name = ?", new String[]{Area.name});
//    }
//
//    /**
//     * delete old Area
//     * @param Area
//     */
//    public void deleteOldArea(Area Area) {
//        db.delete("Area", "age >= ?", new String[]{String.valueOf(Area.age)});
//    }

    /**
     * query all Areas, return list
     *
     * @return List<Area>
     */
    public List<Area> queryAll() {
        ArrayList<Area> Areas = new ArrayList<Area>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Area Area = new Area();
            Area.areaId = c.getString(c.getColumnIndex("Id"));
            Area.areaName = c.getString(c.getColumnIndex("Name"));
            Areas.add(Area);

        }
        c.close();
        return Areas;
    }

    //
//    /**
//     * query all Areas, return cursor
//     * @return  Cursor
//     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM Area", null);
        return c;
    }

    public List<Area> query(String sql) {
        //sql = "SELECT * FROM Area"
        ArrayList<Area> Areas = new ArrayList<Area>();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            Area Area = new Area();
            Area.areaId = c.getString(c.getColumnIndex("Id"));
            Area.areaName = c.getString(c.getColumnIndex("Name"));
            Areas.add(Area);
        }
        c.close();
        return Areas;
    }

    public List<Area> queryProvince()
    {
        String sql = "SELECT * FROM Area where Level = '1.0'";
        return query(sql);
    }

    public List<Area> queryCity(String pid)
    {
        String sql = "SELECT * FROM Area where Level = '2.0' and Pid = "+pid;
        return query(sql);
    }

    public List<Area> queryTown(String pid) {
        String sql = "SELECT * FROM Area where Level = '3.0' and Pid = "+pid;
        return query(sql);
    }

    /**
     * close database
     */

    public void dropTable() {
        if (helper.tableIsExist("Area", db)) {
            String sql = "DROP TABLE  Area;";
            db.execSQL(sql);
        }
    }

    public void initArea() {
        dropTable();
        createAreaTable();
        String json = FileUtil.readRawFile(context, R.raw.area);
        IMsg mr = IMsg.getInstance(json);
        List<Area> list = mr.getModelList("areaRObj", new TypeToken<List<Area>>() {
        }.getType());
        add(list);
    }

    public void initArea(IMsg iMsg) {
        List<Area> list = iMsg.getModelList("areaRObj", new TypeToken<List<Area>>() {
        }.getType());
        add(list);
    }

    public void deleteArea() {
        dropTable();
    }

    public void closeDB() {
        db.close();
    }


}
