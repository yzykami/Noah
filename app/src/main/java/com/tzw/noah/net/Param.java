package com.tzw.noah.net;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

public class Param {
    public Param() {
    }

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Param(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    public Param(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public Param(String key, boolean value) {
        this.key = key;
        this.value = value;
    }

    public String key;

    public Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static List<Param> makeSingleParam(String key, String value) {
        List<Param> list = new ArrayList<>();
        list.add(new Param(key, value));
        return list;
    }

    public static List<Param> makeSingleParam(String key, int value) {
        List<Param> list = new ArrayList<>();
        list.add(new Param(key, value));
        return list;
    }
}
