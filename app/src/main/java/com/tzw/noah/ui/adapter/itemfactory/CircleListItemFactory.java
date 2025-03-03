package com.tzw.noah.ui.adapter.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.ui.circle.PostListFragment;
import com.tzw.noah.utils.Utils;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class CircleListItemFactory extends AssemblyRecyclerItemFactory<CircleListItemFactory.CircleItem> {

    private OnImageClickListener onImageClickListener;
    private int itemSize;
    int mode;

//    public  CircleListItemFactory(OnImageClickListener onImageClickListener) {
//        this.onImageClickListener = onImageClickListener;
//    }

    public CircleListItemFactory(OnImageClickListener onImageClickListener, int mode) {
        this.onImageClickListener = onImageClickListener;
        this.mode = mode;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof String;
    }

    @Override
    public CircleItem createAssemblyItem(ViewGroup viewGroup) {

        return new CircleItem(R.layout.circle_item, viewGroup);
    }

    public interface OnImageClickListener {
        void onClickImage(int position, String optionsKey);
    }

    public class CircleItem extends BindAssemblyRecyclerItem<String> {
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;
        @BindView(R.id.ll_picture)
        LinearLayout ll_picture;

        Context mContext;

        public CircleItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClickImage(getAdapterPosition(), getData());
                    }
                }
            });
            if (mode == PostListFragment.HOME)
                ll_user.setVisibility(View.GONE);
            else
                ll_user.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onSetData(int i, String imageUri) {
//            imageView.setNum(i);
            imageView.displayImage(imageUri);
            int num = new Random().nextInt(3);
            for (int ii = 0; ii < num; ii++) {
                ll_picture.addView(getPicture());
            }
        }

        private View getPicture() {
            float span = mContext.getResources().getDimension(R.dimen.bj);

            float sw = Utils.getScreenWidth();

            int picNum = 3;

            int itemSize = (int) ((sw - (picNum + 1) * span) / picNum);

            SampleImageView iv = new SampleImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // fake Data
            List<String> images = Utils.getImageList();

            iv.displayImage(images.get(new Random().nextInt(images.size())));

            int nn = (int) span;

            layoutParams.width = itemSize;
            layoutParams.height = itemSize * 2 / 3;
            layoutParams.setMargins(nn, 0, 0, 0);
            iv.setLayoutParams(layoutParams);

            return iv;
        }
    }


}
