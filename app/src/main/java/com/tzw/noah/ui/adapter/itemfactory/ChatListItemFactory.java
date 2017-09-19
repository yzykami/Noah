package com.tzw.noah.ui.adapter.itemfactory;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.process.CircleImageProcessor;
import me.xiaopan.sketch.shaper.ImageShaper;
import me.xiaopan.sketch.shaper.RoundRectImageShaper;
import me.xiaopan.sketch.util.SketchUtils;
import me.xiaopan.sketchsample.ImageOptions;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class ChatListItemFactory extends AssemblyRecyclerItemFactory<ChatListItemFactory.ChatListItem> {

    private OnImageClickListener onImageClickListener;
    private int itemSize;

    public ChatListItemFactory(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if(o instanceof String) {
            if(((String) o).startsWith("http"))
            return o instanceof String;
        }
        return false;
    }

    @Override
    public ChatListItem createAssemblyItem(ViewGroup viewGroup) {

        return new ChatListItem(R.layout.sns_chat_item, viewGroup);
    }

    public interface OnImageClickListener {
        void onClickImage(int position, String optionsKey);
    }

    public class ChatListItem extends BindAssemblyRecyclerItem<String> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;

        public ChatListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClickImage(getAdapterPosition(), getData());
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
