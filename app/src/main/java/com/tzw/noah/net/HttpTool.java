package com.tzw.noah.net;

import com.tzw.noah.logger.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * Created by yzy on 2017/6/12.
 */

public class HttpTool {
    static MyHttpConnectionImpl mhc;
    static HttpTool instance;
    private OkHttpClient mOkHttpClient;

    public static HttpTool getInstance() {
        if (instance == null) {
            synchronized (HttpTool.class) {
                instance = new HttpTool();
                mhc = MyHttpConnectionImpl.getInstance();
            }
        }
        return instance;
    }

    HttpTool() {
        mOkHttpClient = new OkHttpClient();
    }

    public void createNewClient() {
        mOkHttpClient = new OkHttpClient();
    }

    public static String Get(String url) {
        return mhc.Get("", url);
    }

    public static String Post(String url, Param params) {
        try {
            return mhc.post(url, params).body().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    //HttpGet同步请求
    public IMsg HttpGet(String url, Param[] headers) {

        Request.Builder builder = new Request.Builder();
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            String ret = execute.body().string();
            Log.httpcall(request, ret);
            IMsg imsg = null;
            if (execute.code() == 200)
                imsg = IMsg.getInstance(ret);
            else
                imsg = CreateErrorMsgResponse("服务器返回错误:" + execute.code());
            return imsg;
        } catch (Exception e) {
            Log.httpcall(request, e);
            return CreateErrorMsgResponse(e.getMessage());
        }
    }

    //HttpGet异步请求
    public void HttpGet(String url, Param[] headers, final Callback callback) {
        Request.Builder builder = new Request.Builder();
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                    Log.httpcall(request, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    IMsg imsg = null;
                    String ret = "";
                    try {
                        ret = response.body().string();
                        Log.httpcall(request, ret);
                        if (response.code() == 200)
                            imsg = IMsg.getInstance(ret);
                        else
                            imsg = CreateErrorMsgResponse("服务器返回错误:" + response.code());
                    } catch (Exception e) {
                        imsg = CreateErrorMsgResponse(e.getMessage());
                        Log.httpcall(request, e);
                    }
                    callback.onResponse(imsg);
                }
            }
        });
    }

    //HttpGet同步请求
    public IMsg HttpDelete(String url, Param[] headers) {

        Request.Builder builder = new Request.Builder();
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        final Request request = builder
                .url(url)
                .delete()
                .build();

        Call call = mOkHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            String ret = execute.body().string();
            Log.httpcall(request, ret);
            IMsg imsg = null;
            if (execute.code() == 200)
                imsg = IMsg.getInstance(ret);
            else
                imsg = CreateErrorMsgResponse("服务器返回错误:" + execute.code());
            return imsg;
        } catch (Exception e) {
            Log.httpcall(request, e);
            return CreateErrorMsgResponse(e.getMessage());
        }
    }

    //HttpGet异步请求
    public void HttpDelete(String url, Param[] headers, final Callback callback) {
        Request.Builder builder = new Request.Builder();
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        final Request request = builder
                .url(url)
                .delete()
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                    Log.httpcall(request, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    IMsg imsg = null;
                    String ret = "";
                    try {
                        ret = response.body().string();
                        Log.httpcall(request, ret);
                        if (response.code() == 200)
                            imsg = IMsg.getInstance(ret);
                        else
                            imsg = CreateErrorMsgResponse("服务器返回错误:" + response.code());
                    } catch (Exception e) {
                        imsg = CreateErrorMsgResponse(e.getMessage());
                        Log.httpcall(request, e);
                    }
                    callback.onResponse(imsg);
                }
            }
        });
    }

    public IMsg CreateErrorMsgResponse(String ex) {
        IMsg imsg = new IMsg();
        imsg.setSucceed(false);
        imsg.setMsg(ex);
        return imsg;
    }

    //HttpPOST异步请求
    public void HttpPost(String url, Param[] headers, final String json, final Callback callback) {

        Request.Builder builder = new Request.Builder();
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = null;
        if (json != null && !json.equals("")) {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
            builder.post(requestBody);
        } else {
            builder.post(Util.EMPTY_REQUEST);
        }

        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        builder.header("Content-Type", "application/json;charset=utf-8");
        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                    Log.httpcall(request, e ,json);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    IMsg imsg = null;
                    try {
                        String ret = response.body().string();
                        Log.httpcall(request, ret, json);
                        if (response.code() == 200)
                            imsg = IMsg.getInstance(ret);
                        else
                            imsg = CreateErrorMsgResponse("服务器返回错误:" + response.code());

                    } catch (Exception e) {
                        imsg = CreateErrorMsgResponse(e.getMessage());
                        Log.httpcall(request, e ,json);
                    }
                    callback.onResponse(imsg);
                }
            }
        });
    }

    //HttpPOST同步请求
    public IMsg HttpPost(String url, Param[] headers, String json) {
        Request.Builder builder = new Request.Builder();

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = null;
        if (json != null && !json.equals("")) {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
            builder.post(requestBody);
        } else {
            builder.post(Util.EMPTY_REQUEST);
        }
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        builder.header("Content-Type", "application/json;charset=utf-8");

        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        Response execute = null;
        try {
            execute = call.execute();
            String ret = execute.body().string();

            Log.httpcall(request, ret, json);
            IMsg imsg = null;
            if (execute.code() == 200)
                imsg = IMsg.getInstance(ret);
            else
                imsg = CreateErrorMsgResponse("服务器返回错误:" + execute.code());
            return imsg;
        } catch (Exception e) {

            Log.httpcall(request, e, json);
            return CreateErrorMsgResponse(e.getMessage());
        }
    }


    //HttpPut异步请求
    public void HttpPut(String url, Param[] headers, final String json, final Callback callback) {

        Request.Builder builder = new Request.Builder();
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = null;
        if (json != null && !json.equals("")) {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
            builder.put(requestBody);
        } else {
            builder.put(Util.EMPTY_REQUEST);
        }

        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        builder.header("Content-Type", "application/json;charset=utf-8");
        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                    Log.httpcall(request, e, json);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    IMsg imsg = null;
                    try {
                        String ret = response.body().string();
                        Log.httpcall(request, ret, json);
                        if (response.code() == 200)
                            imsg = IMsg.getInstance(ret);
                        else
                            imsg = CreateErrorMsgResponse("服务器返回错误:" + response.code());
                    } catch (Exception e) {
                        imsg = CreateErrorMsgResponse(e.getMessage());
                        Log.httpcall(request, e, json);
                    }
                    callback.onResponse(imsg);
                }
            }
        });
    }

    //HttpPUT同步请求
    public IMsg HttpPut(String url, Param[] headers, String json) {
        Request.Builder builder = new Request.Builder();

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = null;
        if (json != null && !json.equals("")) {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
            builder.put(requestBody);
        } else {
            builder.put(Util.EMPTY_REQUEST);
        }
        for (Param param : headers) {
            builder.header(param.key, (String) param.value);
        }
        builder.header("Content-Type", "application/json;charset=utf-8");

        final Request request = builder
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        Response execute = null;
        try {
            execute = call.execute();
            String ret = execute.body().string();

            Log.httpcall(request, ret, json);
            IMsg imsg = null;
            if (execute.code() == 200)
                imsg = IMsg.getInstance(ret);
            else
                imsg = CreateErrorMsgResponse("服务器返回错误:" + execute.code());
            return imsg;
        } catch (Exception e) {

            Log.httpcall(request, e, json);
            return CreateErrorMsgResponse(e.getMessage());
        }
    }
}
