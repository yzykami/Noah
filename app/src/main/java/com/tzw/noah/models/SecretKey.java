package com.tzw.noah.models;

import com.tzw.noah.net.IMsg;

/**
 * Created by yzy on 2017-11-13.
 */

public class SecretKey {
    public int secretKeyId;
    public String secretKeyRemark="";
    public String publishTime="";
    public int forcedUpdate;
    public String subRemark="";
    public int applicationId;
    public int subVersion;

    public static SecretKey load(IMsg iMsg) {
        return iMsg.getModel("secretKeyDetailsRObj", new SecretKey());
    }
}
