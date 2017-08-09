package com.tzw.noah.ui.adapter.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tzw.noah.R;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class CircleBoardItemFactory extends AssemblyRecyclerItemFactory<CircleBoardItemFactory.CircleItem> {

    private OnItemClickListener onImageClickListener;
    private int itemSize;

    public CircleBoardItemFactory(OnItemClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof String;
    }

    @Override
    public CircleItem createAssemblyItem(ViewGroup viewGroup) {

        return new CircleItem(R.layout.circle_board_item, viewGroup);
    }

    public interface OnItemClickListener {
        void onClickItem(int position, String optionsKey);
    }

    public class CircleItem extends BindAssemblyRecyclerItem<String> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;

        Context mContext;

        public CircleItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClickItem(getAdapterPosition(), getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int i, String imageUri) {
//            imageView.setNum(i);
            imageView.displayImage(imageUri);

        }
    }
}
