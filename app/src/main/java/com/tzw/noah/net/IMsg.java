package com.tzw.noah.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.lang.reflect.Type;


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

    private JsonElement result;
    private JsonElement json;
    private JsonElement jobj;

    String jsonStr="";


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
//        Type t = o.getClass();
        Gson gson = new GsonBuilder().create();
//        System.out.println(result.getAsJsonObject().getAsJsonArray(path).toString());
        return gson.fromJson(result.getAsJsonObject().getAsJsonArray(path), t);
    }

    public String getValue(String path) {
        Type t = String.class;
        return result.getAsJsonObject().getAsJsonPrimitive(path).getAsString();
    }

    public IMsg getJsonObject(String path)
    {
        IMsg iMsg = new IMsg();
        iMsg.result=this.result.getAsJsonObject().getAsJsonObject(path);
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
}
