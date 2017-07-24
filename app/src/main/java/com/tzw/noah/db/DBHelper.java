package com.tzw.noah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.R;
import com.tzw.noah.models.Area;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "systemcache.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
//        String sql = FileUtil.readRawFile(context, R.raw.systemcache);
//        db.execSQL(sql);
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

            String sql = "select count(*) as c from sqlite_master where notificationType ='table' and name ='" + tabName.trim() + "' ";
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
        FileUtil.copyDBFromRaw();
    }

    public <T> List<T> queryAll(Class<T> t, String sql) {
        //sql = "SELECT * FROM Area"
        List<T> list = new ArrayList<>();
        ArrayList<Area> Areas = new ArrayList<Area>();
        Cursor c = db.rawQuery(sql, null);
        try {
            Class tclass = Class.forName(t.getName());
            Field[] fields = tclass.getDeclaredFields();
            for (Field field : fields) {
                Annotation a = field.getAnnotation(MyField.class);
                if (a == null) {
                    continue;
                }
                String fname = field.getName();
                Type ftype = field.getType();
            }
            while (c.moveToNext()) {
                T ins = t.newInstance();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();
                    if (ftype.equals(new TypeToken<String>() {
                    }.getType())) {
                        field.set(ins, c.getString(c.getColumnIndex(fname)));
                    }
                    if (ftype.equals(int.class)) {
                        field.set(ins, c.getInt(c.getColumnIndex(fname)));
                    }
                    if (ftype.equals(double.class)) {
                        field.set(ins, c.getDouble(c.getColumnIndex(fname)));
                    }
                }
                list.add(ins);
            }
        } catch (Exception e) {
        } finally {
            if (c != null)
                c.close();
        }
        return list;
    }

    public <T> T query(Class<T> t, String sql) {
        List<T> list = queryAll(t, sql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        try {
            return t.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> void insert(List<T> tList, String tableName) {
        if (tList == null || tList.size() == 0)
            return;
        T t = tList.get(0);
        db.beginTransaction();  //开始事务
        try {

            Class tclass = Class.forName(t.getClass().getName());

            Field[] fields = tclass.getDeclaredFields();
            db.execSQL("delete from "+tableName);

            String sql = "INSERT INTO "+tableName+" VALUES(null";
            List<Object> objs = new ArrayList<>();
            for (T tt : tList) {
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();

                    if (a == null) {
                        continue;
                    }
                    sql += ",?";
                    objs.add(field.get(tt));
//                    if (ftype.equals(new TypeToken<String>() {
//                    }.getType())) {
//                    }
//                    if (ftype.equals(int.class)) {
//                    }
//                    if (ftype.equals(double.class)) {
//                    }
                }
                sql+=")";
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e) {
        } finally {
            db.endTransaction();    //结束事务
        }
    }
}
