package com.tzw.noah.ui.sns.friendlist;


import android.support.v4.app.Fragment;

import com.tzw.noah.models.User;
import com.tzw.noah.utils.Utils;

import java.util.List;

/**
 * Created by yzy on 2017/7/11.
 */

public class MyFragment extends Fragment {

    public void initPinyin(List<User> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).namePingyin = Utils.getLetter(list.get(i).getName());
        }
    }
}
