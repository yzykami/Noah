package com.tzw.noah.init;

import com.tzw.noah.cache.UserCache;
import com.tzw.noah.utils.FileUtil;

/**
 * Created by yzy on 2017/7/11.
 */

public class DBInit {
    //系统缓存初始化
    public void systemCacheInit() {

    }

    //  即时通讯相关数据库初始化，根据MemberNo新建相应的目录，然后将sns.db拷贝到相应位置
    public void snsInit() {
        FileUtil.copySnsDBFromRaw(UserCache.getUser().memberNo+"");
    }
}
