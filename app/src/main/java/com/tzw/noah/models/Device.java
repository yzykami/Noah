package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.utils.Utils;

import java.util.List;

/**
 * Created by yzy on 2017/6/23.
 */

public class Device {

    public String loginKey;
    public String tokenCode;
    public int memberId;
    public int clientType;
    public String clientCode;
    public int clientIp;
    public int proxyIp;
    public int forwardIp;
    public String lastActiveTime;//datetime
    public String loginTime;//datetime

    public String getLastActiveTime() {

        return Utils.formatDatetime(lastActiveTime);
    }

    public String getLoginTime() {
        return Utils.formatDatetime(loginTime);
    }

    public static List<Device> load(IMsg iMsg)
    {
        return iMsg.getModelList("deviceRObj",new TypeToken<List<Device>>(){}.getType());
    }
}
