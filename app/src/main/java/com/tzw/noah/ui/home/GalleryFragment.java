package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListDefaultItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListGalleryItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDBigItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListTxtItemFatory;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.DividerItemDecoration;
import com.tzw.noah.widgets.scrollablelayout.ScrollableHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;
import me.xiaopan.sketchsample.widget.SampleImageView;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/30.
 */
public class GalleryFragment extends Fragment {

    @BindView(R.id.iv_cover)
    FrameLayout ivCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pagesize)
    TextView tvPagesize;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    String Tag = "GalleryFragment";

    Context mContext;
    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;

    GalleryFragment instance;
    MediaArticle mMediaArticle;
    boolean isFirstLoad = true;
    private int articleId = 0;
    int channelId;
    private MediaArticle.GalleryArticle gallery;
    private int size;
    private int index;

    public GalleryFragment setMediaCategory(MediaArticle mediaArticle) {
        this.mMediaArticle = mediaArticle;
        return this;
    }

    public GalleryFragment setGallery(MediaArticle.GalleryArticle gallery) {
        this.gallery = gallery;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        instance = this;

//        ivCover.displayImage(gallery.image);

        boolean showTools = AppConfig.getBoolean(mContext, AppConfig.Key.SHOW_TOOLS_IN_IMAGE_DETAIL);
        Image image = new Image(gallery.image,gallery.image);
        ImageFragment fragmentImage = ImageFragment.build(image, "", showTools);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transition= fm.beginTransaction();
        transition.replace(R.id.iv_cover,fragmentImage);
        transition.commit();

        tvTitle.setText(mMediaArticle.articleTitle);
        if (TextUtils.isEmpty(gallery.text)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(gallery.text);
        }
        tvPagesize.setText(index + "/" + size);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyBaseActivity) context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
            refreshListView();
        } else
            refreshListView2();
    }

    private void refreshListView() {
    }

    private void refreshListView2() {
    }

    private void loadMore() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            refreshListView2();
    }

    public GalleryFragment setPage(int size, int i) {
        this.size = size;
        this.index = i;
        return this;
    }
}
