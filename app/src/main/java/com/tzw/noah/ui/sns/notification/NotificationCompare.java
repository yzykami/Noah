package com.tzw.noah.ui.sns.notification;

import com.tzw.noah.models.Notification;
import com.tzw.noah.models.User;

import java.util.Comparator;

/**
 * Created by yzy on 2017/7/18.
 */
public class NotificationCompare implements Comparator<Notification> {
    @Override
    public int compare(Notification o1, Notification o2) {
        return o1.createTime.compareTo(o2.createTime);
    }
}
