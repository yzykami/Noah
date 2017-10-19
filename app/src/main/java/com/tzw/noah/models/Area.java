package com.tzw.noah.models;

import com.tzw.noah.db.MyField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

//"areaId":110000.0,"areaPid":0.0,"areaName":"北京市","areaLevel":1.0,"areaCode":"","areaPostCode":"100000","areaTelCode":"010","areaSort":0.0,
// "areaShortName":"北京","areaSpell":"beijing","areaShortSpell":"bj","areaLng":"116.405285","areaLat":"39.904989"
public class Area {
    @MyField
    public String areaId = "";
    @MyField
    public String areaPid = "";
    @MyField
    public String areaName = "";
    @MyField
    public String areaLevel = "";
    @MyField
    public String areaCode = "";
    @MyField
    public String areaPostCode = "";
    @MyField
    public String areaTelCode = "";
    @MyField
    public String areaSort = "";
    @MyField
    public String areaShortName = "";
    @MyField
    public String areaSpell = "";
    @MyField
    public String areaShortSpell = "";
    @MyField
    public String areaLng = "";
    @MyField
    public String areaLat = "";

    public List<Area> sonAreaList;

    public Area() {
        sonAreaList = new ArrayList<>();
    }
}
