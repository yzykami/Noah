package com.tzw.noah.models;

import com.tzw.noah.db.MyField;

import java.io.Serializable;

/**
 * Created by yzy on 2017-12-06.
 */

public class SensitiveWordsType implements Serializable {
    @MyField
    public int sensitiveWordsTypeId;
    @MyField
    public String sensitiveWordsTypeName = "";
    @MyField
    public int sensitiveWordsTypeSort;
    @MyField
    public String createTime = "";
}
