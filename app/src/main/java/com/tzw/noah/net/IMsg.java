package com.tzw.noah.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Types;
import com.tzw.noah.logger.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by yzy on 2017/6/12.
 */

//原来的IMSG

public class IMsg extends JsonTool implements Serializable {
    private boolean succeed = false;
    private boolean have = false;
    private int cacheVersion = -1;
    private int code = -1;
    private String msg = "";
    private Long serverTime = -1l;

    public JsonElement result;
    private JsonElement json;
    private JsonElement jobj;

    String jsonStr = "";

    public Object Data;


    public IMsg() {
        super();
    }

    //    IMsg instance;
    public static IMsg getInstance(String json) {
        return new IMsg().load(json);
    }

    public <T> T getModel(String path, Object o) {
        Type t = o.getClass();
        return gson.fromJson(result.getAsJsonObject().getAsJsonObject(path).getAsJsonObject(), t);
    }

    public <T> T getModel(String path, Type t) {
        return gson.fromJson(result.getAsJsonObject().getAsJsonObject(path).getAsJsonObject(), t);
    }

    public <T> T getModelList(String path, Type t) {
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(result.getAsJsonObject().getAsJsonArray(path), t);
        } catch (Exception e) {
            Log.log("MediaComment",e);
            return (T) new ArrayList<Object>();
        }
    }

    public String getValue(String path) {
        Type t = String.class;
        return result.getAsJsonObject().getAsJsonPrimitive(path).getAsString();
    }

    public IMsg getJsonObject(String path) {
        IMsg iMsg = new IMsg();
        iMsg.result = this.result.getAsJsonObject().getAsJsonObject(path);
        return iMsg;
    }

    public IMsg load(String json) {
        IMsg imsg = gson.fromJson(json, IMsg.class);
        imsg.jsonStr = json;
        return imsg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public boolean isHave() {
        return have;
    }

    public void setHave(boolean have) {
        this.have = have;
    }

    public int getCacheVersion() {
        return cacheVersion;
    }

    public void setCacheVersion(int cacheVersion) {
        this.cacheVersion = cacheVersion;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(JsonElement json) {
        this.json = json;
    }

    public Object getJobj() {
        return jobj;
    }

    public void setJobj(JsonElement jobj) {
        this.jobj = jobj;
    }

    public String toString() {
        return jsonStr;
    }

    public void systemOut() {
        int maxlength = 4000;
        int index = 0;
        while (jsonStr.length() > index) {
            String s = "";
            if (jsonStr.length() - index > maxlength) {
                s = jsonStr.substring(index, maxlength + index);
                index += maxlength;
            } else {
                s = jsonStr.substring(index, jsonStr.length());
                index = jsonStr.length();
            }
            System.out.println("yzy_test_article_" + s);
        }
    }
}
