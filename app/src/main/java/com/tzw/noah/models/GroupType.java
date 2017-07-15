package com.tzw.noah.models;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/7/14.
 */

public class GroupType implements Serializable {
    public int groupTypeId;
    public int groupTypePid;
    public String groupTypeName = "";
    public String groupTypeRemark = "";
    public int groupTypeSort;
    public String createTime = "";

    public static List<GroupType> loadList(IMsg iMsg) {
        totalList = iMsg.getModelList("groupTypeRObj", new TypeToken<List<GroupType>>() {
        }.getType());
        return totalList;
    }

    public static List<GroupType> totalList;
    public List<GroupType> sonList;

    public GroupType() {
    }

    public static GroupType getFirstNode(IMsg iMsg) {
        totalList = loadList(iMsg);
        for (int i = 0; i < totalList.size(); i++) {
            if (totalList.get(i).groupTypePid == 0)
                return totalList.get(i);
        }
        return new GroupType();
    }

    public List<GroupType> getSonList()
    {
        sonList = new ArrayList<>();
        for (int i=0; i<totalList.size(); i++) {
            if(groupTypeId == totalList.get(i).groupTypePid)
                sonList.add(totalList.get(i));
        }
        return sonList;
    }
}
