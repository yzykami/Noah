package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.CircleListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.MemberListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListDefaultItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListGalleryItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDBigItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListTxtItemFatory;
import com.tzw.noah.ui.fragment.ViewPagerBaseFragment;
import com.tzw.noah.ui.sns.SnsMainActivity;
import com.tzw.noah.ui.webview.WebViewActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.DividerItemDecoration;
import com.tzw.noah.widgets.scrollablelayout.ScrollableHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/30.
 */
public class ArticleListFragment extends ViewPagerBaseFragment implements MediaListListener, OnRecyclerLoadMoreListener, ScrollableHelper.ScrollableContainer {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    String Tag = "ArticleListFragment";
    Context mContext;

    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;
    ArticleListFragment instance;

    public MediaCategory getMediaCategory() {
        return mMediaCategory;
    }

    MediaCategory mMediaCategory;
    boolean isFirstLoad = true;
    private int articleId = 0;
    int channelId ;
    List<MediaArticle> items;

    public ArticleListFragment setMediaCategory(MediaCategory mMediaCategory) {
        this.mMediaCategory = mMediaCategory;
        channelId=mMediaCategory.channelId;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        android.util.Log.d("aaa",getTitle()+"___onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.circle_list_fragment, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                loadMore();
                articleId = 0;
                refreshListView();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                if (mode == CIRCLEDETAIL)
//                    return false;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        updateData();
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

    @Override
    public String getTitle() {
        if(mMediaCategory!=null)
            return mMediaCategory.channelName;
        else
            return "";
    }

    private void refreshListView() {


        NetHelper.getInstance().mediaArticleList(channelId, articleId, DataCenter.pagesize, new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        items = MediaArticle.loadList(iMsg);
                        iMsg.systemOut();
                        if (items == null)
                            items = new ArrayList<MediaArticle>();
                        if (articleId == 0) {
                            adapter.clear();
//                            MediaArticle ma = new MediaArticle();
//                            ma.LIST_TYPE = MediaArticle.LIST_TYPE_VIEWPAGER;
//                            items.add(0,ma);
                        }
                        adapter.addAll(items);
                        if (items.size() >= 1)
                            articleId = items.get(items.size() - 1).articleId;
                        adapter.loadMoreFinished(items.size() < DataCenter.pagesize);
                    } else {
                        activity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void refreshListView2() {
//        items = DataCenter.getInstance().getFriendList();
//        items = Utils.processUser(items);
//        Collections.sort(items, new MyCompare());
//        adapter = new FriendAdapter(mContext, items);
////        list_view.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    protected void updateData() {
        List<MediaArticle> images = new ArrayList<MediaArticle>();

        items = new ArrayList<>();
        adapter = new AssemblyRecyclerAdapter(items);
        adapter.addItemFactory(new MediaListGalleryItemFatory(instance));
        adapter.addItemFactory(new MediaListTxtItemFatory(instance));
        adapter.addItemFactory(new MediaListPicItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDBigItemFatory(instance));
        adapter.addItemFactory(new MediaListDefaultItemFatory(instance));
        loadMoreItem = new LoadMoreItemFactory(instance);
        adapter.setLoadMoreItem(loadMoreItem);

        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        if (adapter.getItemCount() > 0)
            list_view.smoothScrollToPosition(0);
        list_view.addItemDecoration(new DividerItemDecoration(mContext,R.drawable.recycleview_divider_bjs));
    }

    private void loadMore() {
//        frameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.refreshComplete();
//            }
//        }, 500);
    }

//    @Override
//    public void onClickImage(int position, String optionsKey) {
//        activity.startActivity2(WebViewActivity.class);
//    }

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        refreshListView();
    }

    @Override
    public View getScrollableView() {
        return list_view;
    }

    @Override
    public void onItemClick(int position, Object o) {

        if(HomeMainActivity.getShowMode()) {
            Utils.showObjectString(o, activity);
            return;
        }
        if(((MediaArticle)o).isArticleTypVideo())
        {
//            activity.toast("视频模式未实现");
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", (MediaArticle) o);
            activity.startActivity2(HomeDetailActivity.class, bu);
            return;
        }
        else if(((MediaArticle)o).isArticleTypPicGallery())
        {
//            activity.toast("纯图模式未实现");
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", (MediaArticle)o);
            activity.startActivity2(HomeDetailGalleryActivity.class, bu);
            return;
        }
        else {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", (MediaArticle) o);
            activity.startActivity2(HomeDetailActivity.class, bu);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        android.util.Log.d("aaa",getTitle()+"___setUserVisibleHint");
        if (isVisibleToUser&&activity!=null)
            refreshListView();
    }
}
