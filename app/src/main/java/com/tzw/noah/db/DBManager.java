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
import com.tzw.noah.models.Dict;
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
                        new Object[]{Utils.String2Int(Area.areaId), Utils.String2Int(Area.areaPid), Area.areaName, Utils.String2Int(Area.areaLevel), Area.areaCode, Area.areaPostCode, Area.areaTelCode, Area.areaSort, Area.areaShortName, Area.areaSpell, Area.areaShortSpell, Area.areaLng, Area.areaLat});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

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
            Area.areaId = c.getString(c.getColumnIndex("areaId"));
            Area.areaName = c.getString(c.getColumnIndex("areaName"));
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

    private List<Area> query(String sql) {
        //sql = "SELECT * FROM Area"
        ArrayList<Area> Areas = new ArrayList<Area>();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            Area Area = new Area();
            Area.areaId = c.getString(c.getColumnIndex("areaId"));
            Area.areaName = c.getString(c.getColumnIndex("areaName"));
            Areas.add(Area);
        }
        c.close();
        return Areas;
    }

    public List<Area> queryProvince() {
        String sql = "SELECT * FROM Area where areaLevel = '1'";
        return query(sql);
    }

    public List<Area> queryCity(String pid) {
        String sql = "SELECT * FROM Area where areaLevel = '2' and areaPid = " + pid;
        return query(sql);
    }

    public List<Area> queryTown(String pid) {
        String sql = "SELECT * FROM Area where areaLevel = '3' and areaPid = " + pid;
        return query(sql);
    }

    public Area queryProvinceByTownId(int townID) {
        String sql = "select * from area  where areaId = (select areaPid  from area  where areaId = (select areaPid from area where areaId = " + townID + ")) ";
        List<Area> list = query(sql);
        if (list.size() > 0)
            return list.get(0);
        return new Area();
    }

    public Area queryCityByTownId(int townID) {
        String sql = "select *  from area where areaId = (select areaPid from area where areaId = " + townID + ") ";
        List<Area> list = query(sql);
        if (list.size() > 0)
            return list.get(0);
        return new Area();
    }

    public Area queryTownByTownId(int townID) {
        String sql = "select * from area where areaId = " + townID;
        List<Area> list = query(sql);
        if (list.size() > 0)
            return list.get(0);
        return new Area();
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


    public List<Dict> selectInterestList() {
        String sql = "SELECT * FROM Dictionary where dictionaryType ='memberInterest'";
        return selectDictionaryList(sql);
    }
    public List<Dict> selectCharacterList() {
        String sql = "SELECT * FROM Dictionary where dictionaryType ='memberCharacter'";
        return selectDictionaryList(sql);
    }

    public List<Dict> selectJobtList() {
        String sql = "SELECT * FROM Dictionary where dictionaryType ='memberWork'";
        return selectDictionaryList(sql);
    }

    public List<Dict> selectFeedbacktList() {
        String sql = "SELECT * FROM Dictionary where dictionaryType ='feedBackType'";
        return selectDictionaryList(sql);
    }

    private List<Dict> selectDictionaryList(String sql) {
        //sql = "SELECT * FROM Area"
        ArrayList<Dict> Dicts = new ArrayList<Dict>();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            Dict dict = new Dict();
            dict.dictionaryId = c.getString(c.getColumnIndex("dictionaryId"));
            dict.dictionaryName = c.getString(c.getColumnIndex("dictionaryName"));
            dict.updateTime = c.getString(c.getColumnIndex("updateTime"));
            Dicts.add(dict);
        }
        c.close();
        return Dicts;
    }

}
