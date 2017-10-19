package com.tzw.noah.ui.adapter.itemfactory.advertising;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tzw.noah.models.Advertising;
import com.tzw.noah.ui.home.GalleryAdvFragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;

public class AdvDetailGalleryItemFactory extends AssemblyFragmentItemFactory<Advertising> {
    private Context context;
    private String loadingImageOptionsId;

    public void setImageClickListener(ImageClickListener mImageClickListener) {
        this.mImageClickListener = mImageClickListener;
    }

    private ImageClickListener mImageClickListener;

    public interface ImageClickListener {
        void onImageClick();
    }

    public AdvDetailGalleryItemFactory(Context context, String loadingImageOptionsId) {
        this.context = context;
        this.loadingImageOptionsId = loadingImageOptionsId;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof Advertising;
    }

    @Override
    public Fragment createFragment(int i, Advertising advertising) {
        boolean showTools = AppConfig.getBoolean(context, AppConfig.Key.SHOW_TOOLS_IN_IMAGE_DETAIL);
        GalleryAdvFragment fragment =new GalleryAdvFragment().setAdvertising(advertising);
//        fragment.setImageClickListener(new ImageFragment.ImageClickListener() {
//            @Override
//            public void onImageClick() {
//                if (mImageClickListener != null)
//                    mImageClickListener.onImageClick();
//            }
//        });
        return fragment;
    }
}
