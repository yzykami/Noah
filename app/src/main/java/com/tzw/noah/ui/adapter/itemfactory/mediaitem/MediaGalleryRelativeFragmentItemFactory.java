package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.home.GalleryRelativeFragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;

public class MediaGalleryRelativeFragmentItemFactory extends AssemblyFragmentItemFactory<MediaArticle> {
    private Context context;
    private String loadingImageOptionsId;

    public void setImageClickListener(ImageClickListener mImageClickListener) {
        this.mImageClickListener = mImageClickListener;
    }

    private ImageClickListener mImageClickListener;

    public interface ImageClickListener {
        void onImageClick();
    }

    public MediaGalleryRelativeFragmentItemFactory(Context context, String loadingImageOptionsId) {
        this.context = context;
        this.loadingImageOptionsId = loadingImageOptionsId;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof MediaArticle;
    }

    @Override
    public Fragment createFragment(int i, MediaArticle mediaArticle) {
        boolean showTools = AppConfig.getBoolean(context, AppConfig.Key.SHOW_TOOLS_IN_IMAGE_DETAIL);
        GalleryRelativeFragment fragment = new GalleryRelativeFragment();
        fragment.setMediaArticle(mediaArticle);
        return fragment;
    }
}
