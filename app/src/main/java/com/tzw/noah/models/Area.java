package com.tzw.noah.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/13.
 */

//"areaId":110000.0,"areaPid":0.0,"areaName":"北京市","areaLevel":1.0,"areaCode":"","areaPostCode":"100000","areaTelCode":"010","areaSort":0.0,
// "areaShortName":"北京","areaSpell":"beijing","areaShortSpell":"bj","areaLng":"116.405285","areaLat":"39.904989"
public class Area {
    public String areaId="";
    public String areaPid="";
    public String areaName="";
    public String areaLevel="";
    public String areaCode="";
    public String areaPostCode="";
    public String areaTelCode="";
    public String areaSort="";
    public String areaShortName="";
    public String areaSpell="";
    public String areaShortSpell="";
    public String areaLng="";
    public String areaLat="";

    public List<Area> sonAreaList;

    public Area() {
        sonAreaList = new ArrayList<>();
    }
}
