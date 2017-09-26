package com.tzw.noah.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.utils.Utils;

import java.util.List;

public class MyAdapter extends DragAdapter {

    private Context mContext;
    List<MediaCategory> list;
    int mode = 1;

    public interface ClickListener {
        void onItemClick(int position);

        void onButtonClick(int position);
    }

    ClickListener listener;

    public void setOnClickListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDataModelMove(int from, int to) {
        MediaCategory s = list.remove(from);
        list.add(to, s);
    }

    int width = 0, height = 0, width2 = 0, height2 = 0, dp_big = 0, dp_small = 0;

    public MyAdapter(List<MediaCategory> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MediaCategory getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        ImageView iv;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.channel_dragitem, null);
            if (width2 == 0) {
                int num = 4;
                int screenwidth = Utils.getSrceenWidth();
                width = screenwidth / 4;
//                height = width * 8 / 15;
                dp_small = dp2px(3);
                dp_big = dp2px(12);
                width2 = (screenwidth - 5 * dp_big) / 4;
                height2 = width2 * 8 / 15;
                height = height2 + dp_big - dp_small;
                width = width2 + dp_big;
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
            convertView.setLayoutParams(lp);
            convertView.setPadding(dp_big, dp_big - dp_small, 0, 0);
            RelativeLayout rl = (RelativeLayout) ((FrameLayout) convertView).getChildAt(0);
            rl.setLayoutParams(new FrameLayout.LayoutParams(width2, height2));

            textView = (TextView) convertView.findViewById(R.id.tv);
            iv = (ImageView) convertView.findViewById(R.id.iv);
            if (mode == 2)
                iv.setImageResource(R.drawable.media_channel_add);
        } else {
            textView = (TextView) convertView.findViewById(R.id.tv);
            iv = (ImageView) convertView.findViewById(R.id.iv);
        }
        textView.setText(getItem(position).channelName);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "position = " + position, Toast.LENGTH_SHORT).show();
                if (listener != null)
                    listener.onItemClick(position);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "position = " + position, Toast.LENGTH_SHORT).show();
                if (listener != null)
                    listener.onItemClick(position);
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onButtonClick(position);
            }
        });

        return convertView;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    public int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);
    }
}