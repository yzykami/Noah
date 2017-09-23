package me.xiaopan.sketchsample.adapter.itemfactory;

import android.content.Context;
import android.support.v4.app.Fragment;

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;

public class ImageFragmentItemFactory extends AssemblyFragmentItemFactory<Image> {
    private Context context;
    private String loadingImageOptionsId;

    public void setImageClickListener(ImageClickListener mImageClickListener) {
        this.mImageClickListener = mImageClickListener;
    }

    private ImageClickListener mImageClickListener;

    public interface ImageClickListener {
        void onImageClick();
    }

    public ImageFragmentItemFactory(Context context, String loadingImageOptionsId) {
        this.context = context;
        this.loadingImageOptionsId = loadingImageOptionsId;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof Image;
    }

    @Override
    public Fragment createFragment(int i, Image image) {
        boolean showTools = AppConfig.getBoolean(context, AppConfig.Key.SHOW_TOOLS_IN_IMAGE_DETAIL);
        ImageFragment fragment = ImageFragment.build(image, loadingImageOptionsId, showTools);
        fragment.setImageClickListener(new ImageFragment.ImageClickListener() {
            @Override
            public void onImageClick() {
                if (mImageClickListener != null)
                    mImageClickListener.onImageClick();
            }
        });
        return fragment;
    }
}
