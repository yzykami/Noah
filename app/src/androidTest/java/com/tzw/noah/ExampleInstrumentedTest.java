package com.tzw.noah;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tzw.noah.cache.AppCache;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.DictList;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.utils.DeviceUuidFactory;
import com.tzw.noah.utils.FileUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        DBManager dbm = new DBManager(appContext);

        List<Area> list = new ArrayList<>();
        Area a = new Area();
        a.areaId = "2";
        a.areaPid = "1";
        a.areaName = "1";
        a.areaLevel = "1";
        a.areaCode = "1";
        a.areaPostCode = "1";
        a.areaTelCode = "1";
        a.areaSort = "1";
        a.areaShortName = "1";
        a.areaSpell = "1";
        a.areaShortSpell = "1";
        a.areaLng = "1";
        a.areaLat = "1";
        list.add(a);
        dbm.add(list);
        list = dbm.queryAll();
        for (int i = 0; i < list.size(); i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId);
        }
        assertEquals("com.tzw.noah", appContext.getPackageName());
    }

    @Test
    public void testInitAreaTable() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBManager dbm = new DBManager(appContext);
        dbm.deleteArea();
        dbm.createAreaTable();
        dbm.initArea();
        List<Area> list = dbm.queryAll();
        for (int i = 0; i < list.size() && i < 50; i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId);
        }
    }

    //ios模式 ， 问服务器取
    @Test
    public void testDeviceToken() {
        String token = FileUtil.readDeviceID();
        System.out.println("token" + token);
        if (token.isEmpty()) {
            NetHelper.getInstance().getBaseTime();
            IMsg iMsg = NetHelper.getInstance().getBaseDeviceID();
            String deviceid = iMsg.getValue("deviceID");

            System.out.println(iMsg);
            System.out.println("token" + deviceid);
            FileUtil.saveDeviceID(deviceid);
        }

    }


    void o(String o) {
        System.out.println("******************");
        System.out.println("******************");
        System.out.println("******************" + o + "******************");
        System.out.println("******************");
        System.out.println("******************");
    }
    void o2(String o) {
        System.out.println("****************            " + o + "             ****************");
    }

    //Android模式， 本地生成，然后上传
    @Test
    public void testDeviceToken2() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;

        NetHelper helper = NetHelper.getInstance();
        IMsg iMsg = helper.checkDeviceId();

        DeviceUuidFactory d = new DeviceUuidFactory(appContext);
        o("isupdate = " + d.isIsupdated() + " deviceid = " + d.getDeviceUuidString());

//        assertEquals("true", d.getDeviceUuidString());
        assertEquals("true", d.isIsupdated());
    }

    @Test
    public void test_appacheUpdate() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;

        o(AppCache.string());
        AppCache.check(20);
        o(AppCache.string());
    }

    @Test
    public void test_updateAreaCache() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;

        DBManager dbm = new DBManager(appContext);

        AppCache.updateAreaCache();

        List<Area> list = dbm.queryAll();
        for (int i = 0; i < list.size() && i < 50; i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId + "areaname = " + list.get(i).areaName);
        }
    }

    @Test
    public void test_AreaCache() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;

        NetHelper netHelper = NetHelper.getInstance();
//        netHelper.getBaseTime();

        AppCache.updateAreaCache();

        DBManager dbm = new DBManager(appContext);

        List<Area> list = dbm.queryProvince();
        for (int i = 0; i < list.size() && i < 50; i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId + "areaname = " + list.get(i).areaName);
        }

        list = dbm.queryCity(list.get(10).areaId);
        for (int i = 0; i < list.size() && i < 50; i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId + "areaname = " + list.get(i).areaName);
        }
        list = dbm.queryTown(list.get(0).areaId);
        for (int i = 0; i < list.size() && i < 50; i++) {
            System.out.println("index = " + i + "areaid = " + list.get(i).areaId + "areaname = " + list.get(i).areaName);
        }
//        o(Utils.readRawFile(appContext,R.raw.area));
    }

    @Test
    public void test_bigString() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;
        o(FileUtil.readRawFile(appContext, R.raw.area));
    }

    @Test
    public void test_firstInstall() {
        AppCache.firstInstall();
    }

    void init() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AppContext.instance = appContext;
    }

    @Test
    public void test_initDict() {
        init();
        String s = FileUtil.readInternalFile("dict.txt");
        o(s);
        DictList dict = DictList.load(s);
        if (dict.list == null)
            o("dict = null");
        else {
            for (Param dm : dict.getInterest()
                    ) {
                o("id = " + dm.key + " , name = " + dm.value);
            }
            for (Param dm : dict.getJob()
                    ) {
                o("id = " + dm.key + " , name = " + dm.value);
            }
        }
    }


    @Test
    public void test_UserCache() throws Exception {
        init();
        User u = new User();
        u.areaId=1;
        u.createTime="123123";
//        u.memberId=10;
        u.totalBonus=0.12;
        u.memberNickName="haha";

        Class c  = Class.forName("com.tzw.noah.models.User");
        Field[] fields= c.getDeclaredFields();

        for (Field field:fields
                ) {
            o2(field.getName()+" = "+field.get(u).toString());
        }

        UserCache.setUser(u);

        UserCache.user=null;

        u = UserCache.getUser();

//        Class c  = Class.forName("com.tzw.systemcache.models.User");
//        Field[] fields= c.getDeclaredFields();

        o("");
        for (Field field:fields
                ) {
            o2(field.getName()+" = "+field.get(u).toString());
        }
    }

    @Test
    public void test_NLogger()
    {
        init();
        Log.init();
        o(AppContext.getContext().getFilesDir().getPath() + "/logs");
        NetHelper.getInstance().getBaseConfig();
    }

    @Test
    public void test_grouptype()
    {
        final IMsg[] imsg1 = new IMsg[1];
        NetHelper.getInstance().snsGroupType(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(IMsg iMsg) {
                o(iMsg.toString());
                imsg1[0]=iMsg;
            }
        });

        while (imsg1[0] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
