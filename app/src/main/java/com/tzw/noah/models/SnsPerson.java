package com.tzw.noah.models;

import java.util.List;

/**
 * Created by yzy on 2017/6/30.
 */

public class SnsPerson {

    public String shortCut = "";
    public String name = "";
    public List<Character> namePingyin;
    public String sign = "";
    public String headUrl = "";
    public String lastChatTime = "";
    public boolean isBlock = false;
    public int unReadNum = 0;
    public int type = 0;

    public static class Type {
        public static int Person = 0;
        public static int Group = 1;
    }
}
