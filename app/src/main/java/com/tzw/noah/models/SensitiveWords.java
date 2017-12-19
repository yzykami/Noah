package com.tzw.noah.models;

import com.tzw.noah.db.MyField;

import java.io.Serializable;

/**
 * Created by yzy on 2017-12-06.
 */

public class SensitiveWords implements Serializable {
    @MyField
    public int sensitiveWordsId;
    @MyField
    public String sensitiveWords = "";
    @MyField
    public int holdNum;
    @MyField
    public String replaceCh = "";
    @MyField
    public int sensitiveWordsTypeId;
    @MyField
    public int ifEnabled;
    @MyField
    public String sensitiveWordsRemark = "";
    @MyField
    public String createTime = "";
}
