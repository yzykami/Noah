package com.tzw.noah.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzy on 2017/6/13.
 */

public class JsonTool {

    Gson gson;

    public JsonTool() {
        gson = new GsonBuilder().create();
    }

    public JsonTool(String jsonStr) {
        load(jsonStr);
    }

    public JsonTool load(String jsonStr) {
        gson = new GsonBuilder().create();
        return new JsonTool();
    }

//    public <T> T getModel(String path, Object o) {
//        return null;
//    }
//
//    public <T> T getModelList(String path, Object o) {
//        return null;
//    }
//
//    public Map<String, Object> getMap(String path, Object o) {
//        return new LinkedTreeMap<String, Object>();
//    }
//
//    public List<Map<String, Object>> getMapList(String path, Object o) {
//        return null;
//    }

    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
