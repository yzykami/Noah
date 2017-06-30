package com.tzw.noah.ui.sns.friendlist;

/**
 * Created by yzy on 2017/6/30.
 */

import com.tzw.noah.models.SnsPerson;

import java.util.Comparator;
import java.util.List;

public class MyCompare implements Comparator<SnsPerson> {
    @Override
    public int compare(SnsPerson o1, SnsPerson o2) {
        List<Character> lc1 = o1.namePingyin;
        List<Character> lc2 = o2.namePingyin;
        for (int i = 0; i < lc1.size() && i < lc2.size(); i++) {
            Character c1 = lc1.get(i);
            Character c2 = lc2.get(i);
            if (c1 != c2) {
                boolean isLetter1 = false;
                boolean isLetter2 = false;
                if ((c1 >= 97 && c1 <= 122)) {
                    isLetter1 = true;
                }
                if ((c2 >= 97 && c2 <= 122)) {
                    isLetter2 = true;
                }
                if (isLetter1) {
                    if (isLetter2) {
                        return c1.compareTo(c2);
                    } else
                        return -1;
                } else {
                    if (isLetter2) {
                        return 1;
                    }
                    return c1.compareTo(c2);
                }
            }
        }
        if (o1.name.length() < o2.name.length())
            return -1;
        else if (o1.name.length() == o2.name.length())
            return 0;
        return 1;
    }
}
