package com.tzw.noah.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.tzw.noah.R;

/**
 * Created by yzy on 2017-10-11.
 */

public class MyGSYVideoPlayer extends NormalGSYVideoPlayer{

    public MyGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyGSYVideoPlayer(Context context) {
        super(context);
        init();
    }

    public MyGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init()
    {
        setEnlargeImageRes(R.drawable.media_video_max);
        setShrinkImageRes(R.drawable.media_video_min);
    }

    @Override
    public int getLayoutId() {
        return com.shuyu.gsyvideoplayer.R.layout.video_layout_normal;
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.media_video_pause);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.media_video_play);
            } else {
                imageView.setImageResource(R.drawable.media_video_play);
            }
        }
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        setViewShowState(mThumbImageViewLayout, VISIBLE);
    }
}
