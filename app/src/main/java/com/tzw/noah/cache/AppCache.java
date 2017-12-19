package com.tzw.noah.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.AppContext;
import com.tzw.noah.R;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.Configuration;
import com.tzw.noah.models.Dict;
import com.tzw.noah.models.DictList;
import com.tzw.noah.models.DictType;
import com.tzw.noah.models.SensitiveWords;
import com.tzw.noah.models.SensitiveWordsType;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.MapUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/15.
 */

public class AppCache {
    //缓存总版本
    public static int AllCache = 0;//18;
    //地区信息缓存
    public static int AreaCache = 0;//3;
    //系统配置缓存
    public static int ConfigurationCache = 0;//5;
    //字典信息缓存
    public static int DictionaryCache = 0;//2;
    //字典类型缓存
    public static int DictionaryTpyeCache = 0;//2;
    //敏感字缓存
    public static int SensitiveWordsCache = 0;//2;


    protected static final String PREFS_FILE = "appcache.xml";

    protected static final String PREFS_AllCache = "AllCache";
    protected static final String PREFS_AreaCache = "AreaCache";
    protected static final String PREFS_ConfigurationCache = "ConfigurationCache";
    protected static final String PREFS_DictionaryCache = "DictionaryCache";
    protected static final String PREFS_DictionaryTpyeCache = "DictionaryTpyeCache";
    protected static final String PREFS_SensitiveWordsCache = "SensitiveWordsCache";

    //
    protected static final String PREFS_IsInstall_DB = "IsInstall_DB";
    //
    protected static final String PREFS_IsInstall_Dict = "IsInstall_Dict";

    public interface UpdateCallback {
        void onComplete(boolean isSuccess, int version);
    }

    //如果是首次安装需要将raw的数据初始化到本地私有存储
    public static void firstInstall() {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);
        boolean isInstalldb = prefs.getBoolean(PREFS_IsInstall_DB, false);

//        if (!isInstalldb) {
        FileUtil.copyDBFromRaw();
//            prefs.edit().putBoolean(PREFS_IsInstall_DB, true).commit();
//        }
//        boolean isInstalldict = prefs.getBoolean(PREFS_IsInstall_Dict, false);
//        if (!isInstalldict) {
//            FileUtil.saveInternalFile("dict.txt", FileUtil.readRawFile(context, R.raw.dict));
//            prefs.edit().putBoolean(PREFS_IsInstall_Dict, true).commit();
//        }
    }

    public static void check(int version) {
        getLocalVersion();
        if (version > AllCache) {
            updateCache();
        }
    }

    private static void updateCache() {
        NetHelper net = new NetHelper();
        IMsg iMsg = net.getBaseAppCache();
        if (iMsg.isSucceed()) {
            List<Map<String, Object>> list = iMsg.getModelList("appCacheRObj", new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            boolean isUpdateAll = true;
            int _allcache = 0;
            for (Map<String, Object> map : list
                    ) {
                String keyName = MapUtils.getString(map, "appCacheId");
                int keyValue = MapUtils.getInt(map, "appCacheVersion");

                if (keyName.equals("AllCache")) {
                    _allcache = keyValue;
                }
                if (keyName.equals("AreaCache")) {
                    if (keyValue > AreaCache) {
                        isUpdateAll = updateAreaCache();
                        if (isUpdateAll)
                            AreaCache = keyValue;
                        else
                            break;
                    }
                }
                if (keyName.equals("ConfigurationCache")) {
                    if (keyValue > ConfigurationCache) {
                        isUpdateAll = updateConfigurationCache();
                        if (isUpdateAll)
                            ConfigurationCache = keyValue;
                        else
                            break;
                    }
                }
                if (keyName.equals("DictionaryCache")) {
                    if (keyValue > DictionaryCache) {
                        isUpdateAll = updateDictionaryCache();
                        if (isUpdateAll)
                            DictionaryCache = keyValue;
                        else
                            break;
                    }
                }
                if (keyName.equals("DictionaryTpyeCache")) {
                    if (keyValue > DictionaryTpyeCache) {
                        isUpdateAll = updateDictionaryTpyeCache();
                        if (isUpdateAll)
                            DictionaryTpyeCache = keyValue;
                        else
                            break;
                    }
                }
                if (keyName.equals("SensitiveWordsCache")) {
                    if (keyValue > SensitiveWordsCache) {
                        isUpdateAll = updateSensitiveWordsCache();
                        if (isUpdateAll)
                            SensitiveWordsCache = keyValue;
                        else
                            break;
                    }
                }
            }
            if (isUpdateAll) {
                AllCache = _allcache;
                setLocalVersion();
            }
        }
    }


    public static boolean updateSensitiveWordsCache() {
        String clazzName2 = new Throwable().getStackTrace()[1].getMethodName();
        System.out.println(clazzName2);
        return true;
    }

    public static boolean updateDictionaryTpyeCache() {
        String clazzName2 = new Throwable().getStackTrace()[1].getMethodName();
        System.out.println(clazzName2);
        return true;
    }

    public static boolean updateDictionaryCache() {
        String clazzName2 = new Throwable().getStackTrace()[1].getMethodName();
        System.out.println(clazzName2);
        return true;
    }

    public static boolean updateConfigurationCache() {
        String clazzName2 = new Throwable().getStackTrace()[1].getMethodName();
        System.out.println(clazzName2);
        return true;
    }

    public static boolean updateAreaCache() {
        Context context = AppContext.getContext();
        DBManager dbManager = new DBManager(context);
        dbManager.deleteArea();
        dbManager.createAreaTable();
        NetHelper netHelper = NetHelper.getInstance();
        IMsg iMsg = netHelper.getBaseArea();
        if (iMsg.isSucceed()) {
            dbManager.initArea(iMsg);
            return true;
        } else
            return false;
    }

    public static void updateAreaCache(final Context context, final int lastVersion, final UpdateCallback callback) {
        NetHelper.getInstance().getBaseArea(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<Area> list = iMsg.getModelList("areaRObj", new TypeToken<List<Area>>() {
                        }.getType());
                        new DBManager(context).updateArea(list, lastVersion);
                        callback.onComplete(true,lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateConfigCache(final Context context, final int lastVersion, final UpdateCallback callback) {
        NetHelper.getInstance().getBaseConfig(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<Configuration> list = iMsg.getModelList("configRObj", new TypeToken<List<Configuration>>() {
                        }.getType());
                        new DBManager(context).updateConfiguration(list, lastVersion);
                        callback.onComplete(true,lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateDictCache(final Context context, final int lastVersion, final UpdateCallback callback) {
        NetHelper.getInstance().getBaseDict(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<Dict> list = iMsg.getModelList("dictRObj", new TypeToken<List<Dict>>() {
                        }.getType());
                        new DBManager(context).updateDictionary(list, lastVersion);
                        callback.onComplete(true,lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateDictTypeCache(final Context context, final int lastVersion, final UpdateCallback callback) {
        NetHelper.getInstance().getBaseDictType(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<DictType> list = iMsg.getModelList("dictTypeRObj", new TypeToken<List<DictType>>() {
                        }.getType());
                        new DBManager(context).updateDictionaryTpye(list, lastVersion);
                        callback.onComplete(true,lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateWordsCache(final Context context, final int lastVersion, final UpdateCallback callback) {
//        new DBManager(context).updateSensitiveWords(null, lastVersion);
//        callback.onComplete(true, lastVersion);
        NetHelper.getInstance().getBaseSensitiveWords(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<SensitiveWords> list = iMsg.getModelList("sensitiveWordsRObj", new TypeToken<List<SensitiveWords>>() {
                        }.getType());
                        new DBManager(context).updateSensitiveWords(list, lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateSensitiveWordsType(final Context context, final int lastVersion, final UpdateCallback callback) {
        NetHelper.getInstance().getBaseSensitiveWordsType(new StringDialogCallback(context) {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(false, 0);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    try {
                        List<SensitiveWordsType> list = iMsg.getModelList("sensitiveWordsTypeRObj", new TypeToken<List<SensitiveWordsType>>() {
                        }.getType());
                        new DBManager(context).updateSensitiveWordsType(list, lastVersion);
                        callback.onComplete(true,lastVersion);
                    } catch (Exception e) {
                        callback.onComplete(false, 0);
                    }
                } else {
                    callback.onComplete(false, 0);
                }
            }
        });
    }

    public static void updateAllCache(Context context, int lastVersion) {
        new DBManager(context).updateAllCache(null, lastVersion);
//        callback.onComplete(true, lastVersion);
    }

    private static void setLocalVersion() {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);

        prefs.edit().putInt(PREFS_AllCache, AllCache).commit();
        prefs.edit().putInt(PREFS_AreaCache, AreaCache).commit();
        prefs.edit().putInt(PREFS_ConfigurationCache, ConfigurationCache).commit();
        prefs.edit().putInt(PREFS_DictionaryCache, DictionaryCache).commit();
        prefs.edit().putInt(PREFS_DictionaryTpyeCache, DictionaryTpyeCache).commit();
        prefs.edit().putInt(PREFS_SensitiveWordsCache, SensitiveWordsCache).commit();
    }

    private static void getLocalVersion() {
        Context context = AppContext.getContext();
        final SharedPreferences prefs = context
                .getSharedPreferences(PREFS_FILE, 0);
        int _AllCache = prefs.getInt(PREFS_AllCache, -1);
        int _AreaCache = prefs.getInt(PREFS_AreaCache, -1);
        int _ConfigurationCache = prefs.getInt(PREFS_ConfigurationCache, -1);
        int _DictionaryCache = prefs.getInt(PREFS_DictionaryCache, -1);
        int _DictionaryTpyeCache = prefs.getInt(PREFS_DictionaryTpyeCache, -1);
        int _SensitiveWordsCache = prefs.getInt(PREFS_SensitiveWordsCache, -1);

        AllCache = AllCache > _AllCache ? AllCache : _AllCache;
        AreaCache = AreaCache > _AreaCache ? AreaCache : _AreaCache;
        ConfigurationCache = ConfigurationCache > _ConfigurationCache ? ConfigurationCache : _ConfigurationCache;
        DictionaryCache = DictionaryCache > _DictionaryCache ? DictionaryCache : _DictionaryCache;
        DictionaryTpyeCache = DictionaryTpyeCache > _DictionaryTpyeCache ? DictionaryTpyeCache : _DictionaryTpyeCache;
        SensitiveWordsCache = SensitiveWordsCache > _SensitiveWordsCache ? SensitiveWordsCache : _SensitiveWordsCache;

    }

    public static String string() {
        String s = "";
        s += "AllCache=" + AllCache;
        s += ",AreaCache=" + AreaCache;
        s += ",ConfigurationCache=" + ConfigurationCache;
        s += ",DictionaryCache=" + DictionaryCache;
        s += ",DictionaryTpyeCache=" + DictionaryTpyeCache;
        s += ",SensitiveWordsCache=" + SensitiveWordsCache;
        return s;
    }

    public static DictList getDictList() {
        String s = FileUtil.readInternalFile("dict.txt");
        DictList dict = DictList.load(s);
        return dict;
    }
}
