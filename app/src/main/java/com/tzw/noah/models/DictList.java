package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzy on 2017/6/16.
 */

public class DictList {

    public List<Dict> list;

    public static DictList load(String json) {
        IMsg iMsg = new IMsg().load(json);
        DictList dict =new DictList();
        try {
            dict.list = iMsg.getModelList("dictRObj", new TypeToken<List<Dict>>(){}.getType());
        }
        catch (Exception e)
        {

        }
        return dict;
    }

    private List<Param> getByType(String type)
    {
        List<Param> ll=new ArrayList<>();
        for (Dict dm:list
             ) {
            if(dm.dictionaryType.equals(type))
            {
                Param pm=new Param();
                pm.key=dm.dictionaryId;
                pm.value=dm.dictionaryName;
                ll.add(pm);
            }
        }
        return ll;
    }

    public List<Param> getJob()
    {
        return getByType("memberWork");
    }

    public List<Param> getInterest()
    {
        return getByType("memberInterest");
    }
}
