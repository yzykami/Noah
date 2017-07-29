package com.tzw.noah.net;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.utils.MyMD5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/13.
 */


//原来的WIRequest

public class WIRequest {

    private static String preUrl = "http://10.0.9.2:9094/";
    private static String AppId = "10101";
    private static String AppSecret = "FBC33F36A7146B21DF44EAA0D795D474";
    public static Long TimeOffset = -12345678l;
    private static Long CurTime = 0l;
    private static String Sign = "";
    public static String LoginKey = "";
    private static Handler mdelivery;
    HttpTool httptool;
    Param[] header;
    private String sign;
    private String jsonBody;

    public WIRequest() {
        if (LoginKey.isEmpty()) {
            LoginKey = UserCache.getLoginKey();
        }
        if(TimeOffset==-12345678l)
        {
            TimeOffset = UserCache.getTimeOffset();
        }
        httptool = HttpTool.getInstance();
        mdelivery = new Handler(Looper.getMainLooper());
    }

    //不含body不含loginkey
    void BuildHeader() {
        CurTime = System.currentTimeMillis() / 1000;
        String currentTime = String.valueOf(CurTime - TimeOffset);
        List<Param> list = new ArrayList<Param>();

        list.add(new Param("appid", AppId));
        list.add(new Param("sign", getMD5Sign(AppId + currentTime + AppSecret)));
        list.add(new Param("time", currentTime));
        header = list.toArray(new Param[3]);
    }

    //包含loginkey不含body
    void BuildHeaderLoginKey() {
        CurTime = System.currentTimeMillis() / 1000;
        String currentTime = String.valueOf(CurTime - TimeOffset);

        List<Param> list = new ArrayList<Param>();

        list.add(new Param("appid", AppId));
        list.add(new Param("sign", getMD5Sign(AppId + currentTime + LoginKey + AppSecret)));
        list.add(new Param("time", currentTime));
        list.add(new Param("loginKey", LoginKey));
        header = list.toArray(new Param[list.size()]);
    }

    //    body，loginkey都有
    void BuildHeaderLoginKey(List<Param> body) {
        BuildHeaderLoginKey(body, "");
    }

    //    body，loginkey都有
    void BuildHeaderLoginKey(List<Param> body, String bodyName) {

        if (body == null || body.size() == 0) {
            BuildHeaderLoginKey();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            for (Param p : body) {
                map.put(p.key, p.value);
            }
            if (bodyName.equals("")) {
                BuildHeaderLoginKey(map);
            } else {
                HashMap<String, Object> bodymap = new HashMap<>();
                bodymap.put(bodyName, map);
                BuildHeaderLoginKey(bodymap);
            }
        }
    }

    //    body，loginkey都有
    void BuildHeaderLoginKey(HashMap<String, Object> body) {
        CurTime = System.currentTimeMillis() / 1000;
        String currentTime = String.valueOf(CurTime - TimeOffset);

        List<Param> list = new ArrayList<Param>();

        Gson gson = new GsonBuilder().create();
        jsonBody = gson.toJson(body);

        list.add(new Param("appid", AppId));
        list.add(new Param("sign", getMD5Sign(AppId + currentTime + LoginKey + jsonBody + AppSecret)));
        list.add(new Param("time", currentTime));
        list.add(new Param("loginKey", LoginKey));

        header = list.toArray(new Param[list.size()]);
    }

    private String getMD5Sign(String s) {
        String ret = MyMD5.md5(s);
        return ret;
    }

    public void updateTimeoffset(IMsg mr) {
        TimeOffset = CurTime - mr.getServerTime();
        UserCache.setTimeOffset(TimeOffset);
    }

//    public IMsg GetNoLoginKey(String method) {
//        BuildHeader();
//        String url = preUrl + method;
//        IMsg iMsg = httptool.HttpGet(url, header);
//        updateTimeoffset(iMsg);
//        if (iMsg.getCode() == 3) {
//            BuildHeader();
//            httptool.createNewClient();
//            iMsg = httptool.HttpGet(url, header);
//        }
//        return iMsg;
//    }


//    public IMsg Get2(String method) {
//        BuildHeaderLoginKey();
//        String url = preUrl + method;
//        IMsg iMsg = httptool.HttpGet2(url, header);
//        updateTimeoffset(iMsg);
//        if (iMsg.getCode() == 3) {
//            BuildHeader();
//            httptool.createNewClient();
//            iMsg = httptool.HttpGet2(url, header);
//        }
//        return iMsg;
//    }


    public IMsg Get(String method) {
        BuildHeaderLoginKey();
        String url = preUrl + method;
        IMsg iMsg = httptool.HttpGet(url, header);
        updateTimeoffset(iMsg);
        if (iMsg.getCode() == 3) {
            updateTimeoffset(iMsg);
            BuildHeaderLoginKey();
            iMsg = httptool.HttpGet(url, header);
            if (iMsg.getCode() == 1041) {
                reGetLoginKey();
                BuildHeaderLoginKey();
                iMsg = httptool.HttpGet(url, header);
            }
        }
        if (iMsg.getCode() == 1041) {
            reGetLoginKey();
            BuildHeaderLoginKey();
            iMsg = httptool.HttpGet(url, header);
        }
        return iMsg;
    }

    public void Get(String method, final Callback callback) {
        BuildHeaderLoginKey();
        final String url = preUrl + method;
        httptool.HttpGet(url, header, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (callback != null) {
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onFailure(call, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (callback != null) {
                    updateTimeoffset(iMsg);
                    if (iMsg.getCode() == 3) {
                        updateTimeoffset(iMsg);
                        BuildHeaderLoginKey();
                        iMsg = httptool.HttpGet(url, header);
                        if (iMsg.getCode() == 1041) {
                            reGetLoginKey();
                            BuildHeaderLoginKey();
                            iMsg = httptool.HttpGet(url, header);
                        }
                    }
                    if (iMsg.getCode() == 1041) {
                        reGetLoginKey();
                        BuildHeaderLoginKey();
                        iMsg = httptool.HttpGet(url, header);
                    }
                    final IMsg finalImsg = iMsg;
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onResponse(finalImsg);
                        }
                    });
                }
            }
        });
    }

    public IMsg Delete(String method) {
        BuildHeaderLoginKey();
        String url = preUrl + method;
        IMsg iMsg = httptool.HttpDelete(url, header);
        updateTimeoffset(iMsg);
        if (iMsg.getCode() == 3) {
            updateTimeoffset(iMsg);
            BuildHeaderLoginKey();
            iMsg = httptool.HttpDelete(url, header);
            if (iMsg.getCode() == 1041) {
                reGetLoginKey();
                BuildHeaderLoginKey();
                iMsg = httptool.HttpDelete(url, header);
            }
        }
        if (iMsg.getCode() == 1041) {
            reGetLoginKey();
            BuildHeaderLoginKey();
            iMsg = httptool.HttpDelete(url, header);
        }
        return iMsg;
    }

    public void Delete(String method, final Callback callback) {
        BuildHeaderLoginKey();
        final String url = preUrl + method;
        httptool.HttpDelete(url, header, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (callback != null) {
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onFailure(call, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (callback != null) {
                    updateTimeoffset(iMsg);
                    if (iMsg.getCode() == 3) {
                        updateTimeoffset(iMsg);
                        BuildHeaderLoginKey();
                        iMsg = httptool.HttpDelete(url, header);
                        if (iMsg.getCode() == 1041) {
                            reGetLoginKey();
                            BuildHeaderLoginKey();
                            iMsg = httptool.HttpDelete(url, header);
                        }
                    }
                    if (iMsg.getCode() == 1041) {
                        reGetLoginKey();
                        BuildHeaderLoginKey();
                        iMsg = httptool.HttpDelete(url, header);
                    }
                    final IMsg finalImsg = iMsg;
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onResponse(finalImsg);
                        }
                    });
                }
            }
        });
    }

//    public void GetNoLoginKey(String method, final Callback callback) {
//        BuildHeader();
//        String url = preUrl + method;
//        httptool.HttpGet(url, header, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (callback != null) {
//                    callback.onFailure(call, e);
//                }
//            }
//
//            @Override
//            public void onResponse(IMsg imsg) {
//                if (callback != null) {
//                    updateTimeoffset(imsg);
//                    callback.onResponse(imsg);
//                }
//            }
//        });
//    }

    /**
     * 异步调用
     *
     * @param method   接口名称
     * @param body     请求参数
     * @param bodyName 请求参数SOBJ的名称
     * @param callback 回调接口
     */
    public void Post(final String method, final List<Param> body, Map<String,File> fileBody, final String bodyName, final Callback callback) {
        BuildHeaderLoginKey(body, bodyName);
        final String url = preUrl + method;
        callback.onBefore();
        httptool.HttpPost(url, header, jsonBody,fileBody, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (callback != null) {
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onFailure(call, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (callback != null) {
                    updateTimeoffset(iMsg);
                    //时间不对进行一次重新调用
                    if (iMsg.getCode() == 3) {
                        updateTimeoffset(iMsg);
                        BuildHeaderLoginKey(body, bodyName);
                        iMsg = httptool.HttpPost(url, header, jsonBody);
                        if (iMsg.getCode() == 1041) {
                            reGetLoginKey();
                            BuildHeaderLoginKey(body, bodyName);
                            iMsg = httptool.HttpPost(url, header, jsonBody);
                        }
                    }
                    if (iMsg.getCode() == 1041) {
                        reGetLoginKey();
                        BuildHeaderLoginKey(body, bodyName);
                        iMsg = httptool.HttpPost(url, header, jsonBody);
                    }
                    final IMsg finalImsg = iMsg;
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onResponse(finalImsg);
                        }
                    });
                }
            }
        });
    }

    /*异步调用，body不包含SOBJ名称,不含文件
     *
     * @param method 接口名称
     * @param body 请求参数
     * @param callback 回调接口
     */
    public void Post(String method, List<Param> body, Callback callback) {
        Post(method, body, null, "", callback);
    }

    /*异步调用，body不包含SOBJ名称,不含文件
     *
     * @param method 接口名称
     * @param body 请求参数
     * @param callback 回调接口
     */
    public void Post(String method, List<Param> body,String bodyName, Callback callback) {
        Post(method, body, null, bodyName, callback);
    }




    /*同步调用
     *
     * @param method   接口名称
     * @param body     请求参数
     * @param bodyName 请求参数SOBJ的名称
     * */
    public IMsg Post(String method, List<Param> body, String bodyName) {
        BuildHeaderLoginKey(body, bodyName);
        String url = preUrl + method;
        IMsg iMsg = httptool.HttpPost(url, header, jsonBody);
        if (iMsg.getCode() == 3) {
            updateTimeoffset(iMsg);
            BuildHeaderLoginKey(body, bodyName);
            iMsg = httptool.HttpPost(url, header, jsonBody);
            if (iMsg.getCode() == 1041) {
                reGetLoginKey();
                BuildHeaderLoginKey(body, bodyName);
                iMsg = httptool.HttpPost(url, header, jsonBody);
            }
        }
        if (iMsg.getCode() == 1041) {
            reGetLoginKey();
            BuildHeaderLoginKey(body, bodyName);
            iMsg = httptool.HttpPost(url, header, jsonBody);
        }
        return iMsg;
    }

    /*同步调用，body不包含SOBJ名称
     *
     * @param method   接口名称
     * @param body     请求参数
     * */
    public IMsg Post(String method, List<Param> body) {
        return Post(method, body, "");
    }


    //如果loginkey超时，重新获取loginkey
    private IMsg reGetLoginKey() {
        List<Param> p = new ArrayList<>();
        String token = UserCache.getToken();
        p.add(new Param("tokenCode", token));
        IMsg iMsg = NetHelper.getInstance().memberLoginKey(p);
        if (iMsg.isSucceed()) {
            String loginKey = iMsg.getJsonObject("loginKeyRObj").getValue("loginKey");
            UserCache.setLoginkey(loginKey);
        }
        return iMsg;
    }



    /**
     * 异步调用
     *
     * @param method   接口名称
     * @param body     请求参数
     * @param bodyName 请求参数SOBJ的名称
     * @param callback 回调接口
     */
    public void Put(final String method, final List<Param> body, final String bodyName, final Callback callback) {
        BuildHeaderLoginKey(body, bodyName);
        final String url = preUrl + method;
        callback.onBefore();
        Log.log("HttpPut",jsonBody);
        httptool.HttpPut(url, header, jsonBody, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (callback != null) {
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onFailure(call, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (callback != null) {
                    updateTimeoffset(iMsg);
                    //时间不对进行一次重新调用
                    if (iMsg.getCode() == 3) {
                        updateTimeoffset(iMsg);
                        BuildHeaderLoginKey(body, bodyName);
                        iMsg = httptool.HttpPut(url, header, jsonBody);
                        if (iMsg.getCode() == 1041) {
                            reGetLoginKey();
                            BuildHeaderLoginKey(body, bodyName);
                            iMsg = httptool.HttpPut(url, header, jsonBody);
                        }
                    }
                    if (iMsg.getCode() == 1041) {
                        reGetLoginKey();
                        BuildHeaderLoginKey(body, bodyName);
                        iMsg = httptool.HttpPut(url, header, jsonBody);
                    }
                    final IMsg finalImsg = iMsg;
                    mdelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onAfter();
                            callback.onResponse(finalImsg);
                        }
                    });
                }
            }
        });
    }

    /*异步调用，body不包含SOBJ名称
     *
     * @param method 接口名称
     * @param body 请求参数
     * @param callback 回调接口
     */
    public void Put(String method, List<Param> body, Callback callback) {
        Put(method, body, "", callback);
    }


    /*同步调用
     *
     * @param method   接口名称
     * @param body     请求参数
     * @param bodyName 请求参数SOBJ的名称
     * */
    public IMsg Put(String method, List<Param> body, String bodyName) {
        BuildHeaderLoginKey(body, bodyName);
        String url = preUrl + method;
        IMsg iMsg = httptool.HttpPut(url, header, jsonBody);
        if (iMsg.getCode() == 3) {
            updateTimeoffset(iMsg);
            BuildHeaderLoginKey(body, bodyName);
            iMsg = httptool.HttpPut(url, header, jsonBody);
            if (iMsg.getCode() == 1041) {
                reGetLoginKey();
                BuildHeaderLoginKey(body, bodyName);
                iMsg = httptool.HttpPut(url, header, jsonBody);
            }
        }
        if (iMsg.getCode() == 1041) {
            reGetLoginKey();
            BuildHeaderLoginKey(body, bodyName);
            iMsg = httptool.HttpPut(url, header, jsonBody);
        }
        return iMsg;
    }

    /*同步调用，body不包含SOBJ名称
     *
     * @param method   接口名称
     * @param body     请求参数
     * */
    public IMsg Put(String method, List<Param> body) {
        return Put(method, body, "");
    }
}
