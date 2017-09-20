package com.tzw.noah.init;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tzw.noah.AppContext;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.utils.FileUtil;

/**
 * Created by yzy on 2017/7/11.
 */

public class DBInit {
    //系统缓存初始化
    public void systemCacheInit() {
        FileUtil.copyDBFromRaw();
        int version = new DBManager(AppContext.getContext()).getSystemCacheVersion();
        int rawVersion = getRawVersion();
        Log.log("systemcache" ,"version = "+ version +",rawVersion = "+rawVersion);
        if(version<rawVersion)
            FileUtil.overwriteDBFromRaw();
    }

    //  即时通讯相关数据库初始化，根据MemberNo新建相应的目录，然后将sns.db拷贝到相应位置
    public void snsInit() {
        FileUtil.copySnsDBFromRaw(UserCache.getUser().memberNo+"");
    }

    public int getRawVersion() {
        int rawVersion = 0;
        String s =FileUtil.readRawFile(AppContext.getContext(),R.raw.systemcacheversion);
        try {
            rawVersion =Integer.parseInt(s);
        }
        catch (Exception e)
        {

        }
//        String databasePath = Uri.parse("android.resource://com.tzw.noah/"+ R.raw.systemcache).toString();
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
//                databasePath, null);
//        if(db!=null)
//        {
//            Cursor c = null;
//            try {
//                String sql= "select appCacheVersion from appcache where appcacheid='AllCache'";
//                c = db.rawQuery(sql, null);
//                while (c.moveToNext()) {
//                    rawVersion = c.getInt(0);
//                }
//            } catch (Exception e) {
//                Log.log("DBManager", e);
//            } finally {
//                if (c != null)
//                    c.close();
//            }
//        }
        return rawVersion;
    }
}
