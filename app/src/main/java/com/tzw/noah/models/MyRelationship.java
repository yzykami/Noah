package com.tzw.noah.models;

import com.tzw.noah.db.MyField;
import com.tzw.noah.net.IMsg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yzy on 2017/7/11.
 */

public class MyRelationship {

    @MyField(name = "id")
    public int memberNo;
    @MyField
    public String createTime = "";

    public static List<MyRelationship> createList(List<User> userList)
    {
        List<MyRelationship> list = new ArrayList<>();
        for (int i=0; i<userList.size(); i++) {
            MyRelationship mr=new MyRelationship();
            mr.memberNo=userList.get(i).memberNo;
            java.util.Date today=new java.util.Date();
            java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mr.createTime= dateTimeFormat.format(today);
            list.add(mr);
        }
        return list;
    }
}
