package com.tzw.noah.ui.adapter.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tzw.noah.R;
import com.tzw.noah.models.GroupMember;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class SearchHeadFactory extends AssemblyRecyclerItemFactory<SearchHeadFactory.SearchHeadItem> {

    private OnItemClickListener onItemClickListener;
    private int itemSize;

    public SearchHeadFactory(OnItemClickListener onImageClickListener) {
        this.onItemClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof Object;
    }

    @Override
    public SearchHeadItem createAssemblyItem(ViewGroup viewGroup) {

        return new SearchHeadItem(R.layout.sns_search_head, viewGroup);
    }

    public interface OnItemClickListener {
        void onSearchClick(int position, Object optionsKey);
    }

    public class SearchHeadItem extends BindAssemblyRecyclerItem<Object> {
        //        @BindView(R.id.iv_head)
//        SampleImageView imageView;
        @BindView(R.id.container)
        ViewGroup rootViewGroup;

        public SearchHeadItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            rootViewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onSearchClick(getAdapterPosition(), getData());
                    }
                }
            });

        }

        @Override
        protected void onSetData(int i, Object imageUri) {

        }
    }
}
