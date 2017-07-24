package com.tzw.noah.models;

import com.tzw.noah.db.MyField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yzy on 2017/7/11.
 */

public class MyGroups {

    @MyField(name = "id")
    public int groupId;
    @MyField
    public int groupAttribute;

    public static List<MyGroups> createList(List<Group> groupList)
    {
        List<MyGroups> list = new ArrayList<>();
        for (int i=0; i<groupList.size(); i++) {
            MyGroups mr=new MyGroups();
            mr.groupId=groupList.get(i).groupId;
            mr.groupAttribute=groupList.get(i).groupAttribute;
//            Date today=new Date();
//            java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            mr.createTime= dateTimeFormat.format(today);
            list.add(mr);
        }
        return list;
    }
}
