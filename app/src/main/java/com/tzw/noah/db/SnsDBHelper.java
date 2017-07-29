package com.tzw.noah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Area;
import com.tzw.noah.utils.FileUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class SnsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sns.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;

    public SnsDBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = getWritableDatabase();
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

    public boolean tableIsExist(String tabName) {
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
            Log.log("tableIsExist", e);
        }
        return result;
    }

    public void createNewTable(String tablename) {
        if (tableIsExist(tablename))
            return;
        FileUtil.copyDBFromRaw();
    }

    //查询Sql,返回查询集合
    public <T> List<T> queryAll(Class<T> t, String sql) {
        //sql = "SELECT * FROM Area"
        List<T> list = new ArrayList<>();
        Cursor c = null;
        try {
            c = db.rawQuery(sql, null);
            Class tclass = Class.forName(t.getName());
            Field[] fields = tclass.getDeclaredFields();

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
            Log.log("queryAll", e);
        } finally {
            if (c != null)
                c.close();
        }
        return list;
    }

    //查询Sql,返回查询集合, 不判断MyField
    public <T> List<T> queryAllNoMyField(Class<T> t, String sql) {
        //sql = "SELECT * FROM Area"
        List<T> list = new ArrayList<>();
        Cursor c = null;
        try {
            c = db.rawQuery(sql, null);
            Class tclass = Class.forName(t.getName());
            Field[] fields = tclass.getDeclaredFields();

            while (c.moveToNext()) {
                T ins = t.newInstance();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
//                    if (a == null) {
//                        continue;
//                    }
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
            Log.log("queryAllNoMyField", e);
        } finally {
            if (c != null)
                c.close();
        }
        return list;
    }

    //查询Sql, 只返回第一行
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

    //插入列表, 如果已存在,先删除在插入
    public <T> void insertDeleteMode(List<T> tList, String tableName) {//condition
        if (tList == null || tList.size() == 0)
            return;
        T t = tList.get(0);
        db.beginTransaction();  //开始事务
        try {

            Class tclass = Class.forName(t.getClass().getName());

            Field[] fields = tclass.getDeclaredFields();
            db.execSQL("delete from " + tableName);

            String sql = "INSERT INTO " + tableName + " VALUES(null";
            for (T tt : tList) {
                String column = " (";
                String values = " (";
                List<Object> objs = new ArrayList<>();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();

                    column += fname + ",";
                    values += "?,";
                    objs.add(field.get(tt));
                }

                column = column.substring(0, column.length() - 1) + ") ";
                values = values.substring(0, values.length() - 1) + ") ";

                sql = "INSERT INTO " + tableName + column + " VALUES" + values;
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e) {
            Log.log("insertDeleteMode", e);
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    //插入或者更新对象,全部列
    public <T> void insertOrUpdate(T t, String tableName) {
        String sql = "";
        db.beginTransaction();  //开始事务
        try {
            String tableIdName = "";
            Object tableIdValue = null;
            Class tclass = Class.forName(t.getClass().getName());

            Field[] fields = tclass.getDeclaredFields();

            for (Field field : fields) {
                Annotation a = field.getAnnotation(MyField.class);
                if (a != null && a.toString().equals("@com.tzw.noah.db.MyField(name=id)")) {
                    tableIdName = field.getName();
                    tableIdValue = field.get(t);
                }
            }
            if (tableIdValue == null) {
                return;
            }
            sql = "select * from " + tableName + " where " + tableIdName + " = " + tableIdValue;
            List<T> list_select = queryAll(tclass, sql);

            //如果数据不存在 Insert
            if (list_select == null || list_select.size() == 0) {
                String column = " (";
                String values = " (";
                List<Object> objs = new ArrayList<>();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();

                    column += fname + ",";
                    values += "?,";
                    objs.add(field.get(t));
                }

                column = column.substring(0, column.length() - 1) + ") ";
                values = values.substring(0, values.length() - 1) + ") ";

                sql = "INSERT INTO " + tableName + column + " VALUES" + values;
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            //如果已经存在 Update
            else {
                sql = "UPDATE " + tableName + " SET ";//+//""memberNo = ?,
                List<Object> objs = new ArrayList<>();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();
                    objs.add(field.get(t));
                    sql += fname + "=?,";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += " where " + tableIdName + " = " + tableIdValue;
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e) {
            Log.log("insertOrUpdate", e);
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    //插入或者更新对象,部分列
    public <T> void insertOrUpdate(T t, List<String> columns, String tableName) {
        String sql = "";
        db.beginTransaction();  //开始事务
        try {
            String tableIdName = "";
            Object tableIdValue = null;
            Class tclass = Class.forName(t.getClass().getName());

            Field[] fields = tclass.getDeclaredFields();

            for (Field field : fields) {
                Annotation a = field.getAnnotation(MyField.class);
                if (a != null && a.toString().equals("@com.tzw.noah.db.MyField(name=id)")) {
                    tableIdName = field.getName();
                    tableIdValue = field.get(t);
                }
            }
            if (tableIdValue == null) {
                return;
            }
            sql = "select * from " + tableName + " where " + tableIdName + " = " + tableIdValue;
            List<T> list_select = queryAll(tclass, sql);

            //如果数据不存在 Insert
            if (list_select == null || list_select.size() == 0) {
                String column = " (";
                String values = " (";
                List<Object> objs = new ArrayList<>();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();

                    column += fname + ",";
                    values += "?,";
                    objs.add(field.get(t));
                }

                column = column.substring(0, column.length() - 1) + ") ";
                values = values.substring(0, values.length() - 1) + ") ";

                sql = "INSERT INTO " + tableName + column + " VALUES" + values;
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            //如果已经存在 Update
            else {
                sql = "UPDATE " + tableName + " SET ";//+//""memberNo = ?,
                List<Object> objs = new ArrayList<>();
                for (Field field : fields) {
                    Annotation a = field.getAnnotation(MyField.class);
                    if (a == null) {
                        continue;
                    }
                    String fname = field.getName();
                    Type ftype = field.getType();
                    if (!columns.contains(fname)) {
                        continue;
                    }
                    objs.add(field.get(t));
                    sql += fname + "=?,";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += " where " + tableIdName + " = " + tableIdValue;
                db.execSQL(sql, objs.toArray(new Object[objs.size()]));
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e) {
            Log.log("insertOrUpdate", e);
        } finally {
            db.endTransaction();    //结束事务
        }
    }
}
