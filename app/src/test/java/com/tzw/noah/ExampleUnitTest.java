package com.tzw.noah;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.sdk.SnsManager;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public void o(String s) {
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println(ascii2native(s));
        System.out.println("***********************");
        System.out.println("***********************");
    }

    @Test
    public void addition_isCorrect() throws Exception {
//        MyNetworkManager m = MyNetworkManager.getInstance();
//        System.out.println("Test Login");
//
//        String s = m.GetNoLoginKey("http://10.0.9.2:9094/base/time");
//        o(s);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void json_test() throws Exception {
        List<Param> body = new ArrayList<>();
        body.add(new Param("key1", "1111"));
        body.add(new Param("key2", "2222"));
        body.add(new Param("key3", "3333"));
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(body);
        o(jsonBody);
        Map<String, String> bb = new HashMap<>();
        bb.put("haha1", "1111");
        bb.put("haha2", "1112");
        bb.put("haha3", "1113");
        bb.put("haha4", "1114");
        o(gson.toJson(bb));
        assertEquals(4, 2 + 2);
    }

    //    @Test
//    public void test_netapi_base_time() {
//        WIRequest net = new WIRequest();
//        IMsg IMsg = net.getBaseTime();
//        o(IMsg.toString());
//        IMsg = net.getBaseTime();
//        o(IMsg.toString());
//    }
//
//    void sysTime() {
//        WIRequest net = new WIRequest();
//        IMsg IMsg = net.getBaseTime();
//    }
//
//    @Test
//    public void test_netapi_base_deviceID() {
//        sysTime();
//        WIRequest net = new WIRequest();
//        IMsg IMsg = net.getBaseDeviceID();
//        o(IMsg.toString());
//    }
    @Test
    public void test_netapi_base_config() {
        NetHelper netHelper = NetHelper.getInstance();
        IMsg IMsg = netHelper.getBaseConfig();
        o(IMsg.toString());
    }

    @Test
    public void test_netapi_base_appache() {
        NetHelper netHelper = NetHelper.getInstance();
        IMsg IMsg = netHelper.getBaseDictType();
        o(IMsg.toString());
        IMsg = netHelper.getBaseDict();
        o(IMsg.toString());
        IMsg = netHelper.getBaseConfig();
        o(IMsg.toString());
    }


//    @Test
//    public void test_netapi_base_area() {
//        sysTime();
//        WIRequest net = new WIRequest();
//        IMsg IMsg = net.getBaseArea();
//        o(IMsg.toString());
//    }

    private static String ascii2native(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue;
    }


    @Test
    public void testRegister() {
        NetHelper netHelper = NetHelper.getInstance();
        netHelper.getBaseTime();
        List<Param> body = new ArrayList<>();
        body.add(new Param("memberMobile", "15858652110"));
        body.add(new Param("memberPasswd", "123456"));
        body.add(new Param("vcode", "999999"));
        final IMsg[] imsg1 = new IMsg[1];
        netHelper.memberRegister(body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(IMsg imsg) {

                o(imsg.toString());
                imsg1[0] = imsg;
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


    @Test
    public void testCompare() {
        String s = "";
        String b = "";
        assertEquals(s.compareTo(b), 0);
        s = "a";
        b = "b";
        assertEquals(s.compareTo(b), -1);
    }

    @Test
    public void testT() {

        List<User> list = getModelList(null, null);
        String a = "";
        String ab = "";
    }


    public <T> T getModelList(String path, Type t) {
        return (T) new ArrayList<Object>();
    }

    @Test
    public void aaa()
    {
        String path = "C:\\Users\\sysadmin\\Documents\\美工切图";
        List<String> name1=new ArrayList();
        List<String> name2=new ArrayList();

        name1.add("多边形-3-拷贝");
        name1.add("tab_mine_2_2-55");
        name1.add("形状-12-拷贝-3");
        name1.add("形状-12-拷贝-4");


        name2.add("media_input_fav_ed");
        name2.add("media_input_comment_ed");
        name2.add("media_comment_like_ed");
        name2.add("media_input_like_ed");

        for (int i = 0; i < name1.size(); i++) {

            String sname1=name1.get(i);
            String sname2=name2.get(i);
            changename(path,sname1,sname2);
        }
    }

    private static void changename(String path,String name1, String name2) {

        File dir = new File(path);
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {
            // 打印全路径名称
            System.out.println(fs[i].getAbsolutePath());
            String ss = fs[i].getName();
            if (fs[i].isDirectory()&&!ss.equals("ok")) {
                changename(fs[i].getAbsolutePath(),name1, name2);
            }
            if (ss.equals(name1+".png")) {
                System.out.println("修改前文件名称是：" + fs[i].getName());
                String rootPath = fs[i].getParent();
                System.out.println("根路径是：" + rootPath);
                File newFile = new File(rootPath + File.separator + name2 + ".png");
                System.out.println("修改后文件名称是：" + newFile.getName());
                if (fs[i].renameTo(newFile)) {
                    System.out.println("修改成功!");
                } else {
                    System.out.println("修改失败");
                }
            }
        }
    }
}