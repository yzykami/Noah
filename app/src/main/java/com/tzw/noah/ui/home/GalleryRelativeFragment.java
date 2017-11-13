package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailListener;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaGalleryRelativeItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;

/**
 * Created by yzy on 2017/8/30.
 */
public class GalleryRelativeFragment extends Fragment implements MediaArticleDetailListener {

    /// /    @BindView(R.id.iv_cover)
//    FrameLayout ivCover;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.tv_pagesize)
//    TextView tvPagesize;
//    @BindView(R.id.tv_content)
//    TextView tvContent;
//    @BindView(R.id.ll_content)
//    LinearLayout llContent;
    @BindView(R.id.list_view)
    RecyclerView recyclerView;

    String Tag = "GalleryFragment";

    Context mContext;
    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;

    GalleryRelativeFragment instance;
    MediaArticle mMediaArticle;
    boolean isFirstLoad = true;
    private int articleId = 0;
    int channelId;
    private MediaArticle.GalleryArticle gallery;
    private int size;
    private int index;


    public GalleryRelativeFragment setMediaArticle(MediaArticle mediaArticle) {
        this.mMediaArticle = mediaArticle;
        return this;
    }

    public GalleryRelativeFragment setGallery(MediaArticle.GalleryArticle gallery) {
        this.gallery = gallery;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gallery_relative, container, false);
        ButterKnife.bind(this, view);
        instance = this;


        if (mMediaArticle.relatedArticlesObj.size() == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        }
        adapter = new AssemblyRecyclerAdapter(mMediaArticle.relatedArticlesObj);
        adapter.addItemFactory(new MediaGalleryRelativeItemFactory(this));
//        adapter.addAll(mMediaArticle.relatedArticlesObj);
//        adapter.addAll(mMediaArticle.relatedArticlesObj);
//        adapter.addAll(mMediaArticle.relatedArticlesObj);
        recyclerView.setAdapter(adapter);

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

    public GalleryRelativeFragment setPage(int size, int i) {
        this.size = size;
        this.index = i;
        return this;
    }

    @Override
    public void onItemClick(int position, Object o) {
//        MediaArticle ma = new MediaArticle();
//        ma.articleImage = "http://img.zhisheji.com/bbs/forum/201409/29/211716mtecegbn9299u0bp.jpg";
//        ma.articleTitle = "position = " + position + 2;
//        adapter.addAll(ma);
//        adapter.notifyDataSetChanged();
//        activity.toast("gallery relative click posi = " + position);
        if (o instanceof MediaArticle) {
            if (((MediaArticle) o).isArticleTypeVideo()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailActivity.class, bu);
                activity.finish();
                return;
            } else if (((MediaArticle) o).isArticleTypPicGallery()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailGalleryActivity.class, bu);
                activity.finish();
                return;
            } else {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailActivity.class, bu);
                activity.finish();
                return;
            }
        }
    }

    @Override
    public void onLikeClick(int position, Object o) {

    }

    @Override
    public void onWebViewLoadComplete() {

    }

    @Override
    public void onLikeMemberClick(int position, Object o) {

    }

    @Override
    public void onCommentClick(int adapterPosition, MediaComment data) {

    }

    @Override
    public void onCommentLikeClick(int position, MediaComment data) {

    }

    @Override
    public void onComplaintClick() {

    }

    @Override
    public void toggledFullscreen(boolean fullscreen) {

    }

    @Override
    public void onKeywordClick(String key, String keyId) {

    }
}
