package com.tzw.noah.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.utils.Utils;

import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

/**
 * Created by yzy on 2017-10-18.
 */

public abstract class MediaListItemAssemblyRecyclerItem<T> extends BindAssemblyRecyclerItem<T> {
    private int mBjs, mBtnsize;
    private MediaListListener mMediaListListener;
    Context mContext;

    public MediaListItemAssemblyRecyclerItem(int itemLayoutId, ViewGroup parent) {
        super(itemLayoutId, parent);
        mContext = parent.getContext();
        mBtnsize = Utils.dp2px(parent.getContext(), 34);
        mBjs = (int) parent.getContext().getResources().getDimension(R.dimen.bjs);
    }


    protected void initEditMode(RelativeLayout container, final MediaArticle mediaArticle, final MediaListListener mMediaListListener) {
        if (mediaArticle.isEditMode) {
            View view = container.getChildAt(0);
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                lp.setMargins(mBjs, mBjs, mBjs, mBjs);
            view.setScrollX(mBjs - mBtnsize);
            final ImageView iv = new ImageView(mContext);
            RelativeLayout.LayoutParams iv_lp = new RelativeLayout.LayoutParams(mBtnsize, ViewGroup.LayoutParams.MATCH_PARENT);
            iv_lp.addRule(RelativeLayout.CENTER_VERTICAL);
//                iv_lp.setMargins(mBjs, mBjs, mBjs, mBjs);
            iv.setLayoutParams(iv_lp);
//                iv.setImageMatrix();
            iv.setPadding(mBjs, mBjs, mBjs, mBjs);
            iv.setCropToPadding(true);
//                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (mediaArticle.isSelected)
                iv.setImageResource(R.drawable.tab_circle_clicked_2);
            else
                iv.setImageResource(R.drawable.tab_circle_2);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaArticle.isSelected = !mediaArticle.isSelected;
                    if (mediaArticle.isSelected)
                        iv.setImageResource(R.drawable.tab_circle_clicked_2);
                    else
                        iv.setImageResource(R.drawable.tab_circle_2);

                    if (mMediaListListener != null) {
                        mMediaListListener.onItemClick(getAdapterPosition(), "isSelected");
                    }
                }
            });
            container.addView(iv);
        } else {
            View view = container.getChildAt(0);
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                lp.setMargins(mBjs, mBjs, mBjs, mBjs);
            view.setScrollX(0);
            if (container.getChildCount() > 1) {
                container.removeViewAt(1);
            }
        }
    }
}
