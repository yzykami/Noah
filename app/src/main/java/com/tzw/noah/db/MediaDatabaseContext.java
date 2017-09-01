package com.tzw.noah.db;

/**
 * Created by yzy on 2017/8/30.
 */

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.tzw.noah.AppContext;
import com.tzw.noah.cache.UserCache;

import java.io.File;

class MediaDatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "MediaDatabaseContext";

    public MediaDatabaseContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String name) {
        File sdcard = Environment.getExternalStorageDirectory();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/data/data/");
        stringBuffer.append(AppContext.getContext().getPackageName());
        stringBuffer.append("/databases");
        String dbDir = "";//UserCache.getUser().memberNo + "";
        if (!dbDir.isEmpty())
            stringBuffer.append("/" + dbDir);
        File dir = new File(stringBuffer.toString());
        if (!dir.exists()) {//防止databases文件夹不存在，不然，会报ENOENT (No such file or directory)的异常
            dir.mkdirs();
        }
        stringBuffer.append("/");
        String dbfile = stringBuffer.toString() + name;

        if (!dbfile.endsWith(".db")) {
            dbfile += ".db";
        }

        File result = new File(dbfile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        return result;
    }

    /*
     * this version is called for android devices >= api-11. thank to @damccull
     * for fixing this.
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name, mode, factory);
    }

    /* this version is called for android devices < api-11 */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);

        return result;
    }
}