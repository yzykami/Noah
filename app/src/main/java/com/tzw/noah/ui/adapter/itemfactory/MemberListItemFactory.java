package com.tzw.noah.ui.adapter.itemfactory;

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

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketch.util.SketchUtils;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class MemberListItemFactory extends AssemblyRecyclerItemFactory<MemberListItemFactory.MemberItem> {

    private OnImageClickListener onImageClickListener;
    private int itemSize;

    public MemberListItemFactory(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof GroupMember;
    }

    @Override
    public MemberItem createAssemblyItem(ViewGroup viewGroup) {
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
                    itemSize = (screenWidth - (SketchUtils.dp2px(viewGroup.getContext(), 0) * (spanCount + 1))) / spanCount;
                }
            }
        }
        return new MemberItem(R.layout.sns_group_member_item, viewGroup);
    }

    public interface OnImageClickListener {
        void onClickImage(int position, GroupMember optionsKey);
    }

    public class MemberItem extends BindAssemblyRecyclerItem<GroupMember> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public MemberItem(int itemLayoutId, ViewGroup parent) {
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

            if (itemSize > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                int nn = SketchUtils.dp2px(context, 10);
                layoutParams.width = itemSize - nn * 2;
                layoutParams.height = itemSize - nn * 2;
                layoutParams.setMargins(nn, nn * 2, nn, nn);
                imageView.setLayoutParams(layoutParams);

            }
        }

        @Override
        protected void onSetData(int i, GroupMember groupMember) {
//            imageView.setNum(i);
            imageView.displayImage(groupMember.memberHeadPic);
            tv_name.setText(groupMember.getMemberName());
        }
    }
}
