package com.tzw.noah.ui.adapter.itemfactory.media;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.MediaLike;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketch.util.SketchUtils;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class LikeListItemFactory extends AssemblyRecyclerItemFactory<LikeListItemFactory.Item> {

    private OnImageClickListener onImageClickListener;
    private int itemSize, bj, top;

    public LikeListItemFactory(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof MediaLike;
    }

    @Override
    public Item createAssemblyItem(ViewGroup viewGroup) {
        bj = viewGroup.getResources().getDimensionPixelSize(R.dimen.pt25);
        top = viewGroup.getResources().getDimensionPixelSize(R.dimen.pt20);
        if (itemSize == 0) {
            itemSize = -1;
            if (viewGroup instanceof RecyclerView) {
                int spanCount = 1;
                RecyclerView recyclerView = (RecyclerView) viewGroup;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    spanCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    spanCount = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
                }
                if (spanCount > 1) {
                    int screenWidth = viewGroup.getResources().getDisplayMetrics().widthPixels;
                    itemSize = (screenWidth - spanCount * bj - bj) / spanCount;
                }
            }
        }
        return new Item(R.layout.sns_group_member_item, viewGroup);
    }

    public interface OnImageClickListener {
        void onClickImage(int position, MediaLike optionsKey);
    }

    public class Item extends BindAssemblyRecyclerItem<MediaLike> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;
        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.view)
//        MyGroupCoverView view;

        public Item(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClickImage(getAdapterPosition(), getData());
                    }
                }
            });


        }

        @Override
        protected void onSetData(int i, MediaLike mediaLike) {
//            imageView.setNum(i);
            int realIndex = (i - 3) % 5;
            if (itemSize > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.width = itemSize;
                layoutParams.height = itemSize;
                int bj2 = bj / 5;
                layoutParams.setMargins(bj - realIndex * bj2, top, bj2 * realIndex, 15);
                imageView.setLayoutParams(layoutParams);
            }

            imageView.displayImage(mediaLike.memberHeadPic);
            tv_name.setText(mediaLike.memberNickName);
//            view.setMask(imageView.getDrawable());

        }
    }
}
