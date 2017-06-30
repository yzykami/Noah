package com.tzw.noah.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzw.noah.R;

/**
 * Created by yzy on 2017/6/30.
 */

public class ViewUtils {
    public static View getHeadView(LayoutInflater inflater, ViewGroup container, int drawableId, String title) {
        View headView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv = (ImageView) headView.findViewById(R.id.iv_head);
        TextView tv = (TextView) headView.findViewById(R.id.tv_name);
        iv.setImageResource(drawableId);
        tv.setText(title);
        return headView;
    }
}
