package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.net.IMsg;

import java.util.List;

/**
 * Created by yzy on 2017/8/30.
 */

public class MediaCategory {

    public int channelId;
    public int channelGroupId;
    public String channelName = "";
    public int channelPid;
    public String channelRemark = "";
    public int channelSort;
    public int channelState;
    public int channelType;
    public String channelUrl = "";
    public String templateIndex = "";
    public String templateList = "";
    public String templateDetail = "";
    public String createTime = "";

    public List<MediaCategory> children;

    public static MediaCategory load(IMsg iMsg) {
        MediaCategory m = new MediaCategory();

        m.children = iMsg.getModelList("categoryRObj", new TypeToken<List<MediaCategory>>() {
        }.getType());
        if (m.children != null && m.children.size() > 0)
            return m.children.get(0);
        return null;
    }
}
