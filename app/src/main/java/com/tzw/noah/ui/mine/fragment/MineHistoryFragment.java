package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MyComment;
import com.tzw.noah.models.MyFavorite;
import com.tzw.noah.models.MyHistory;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListDefaultItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDBigItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListTxtItemFatory;
import com.tzw.noah.ui.fragment.ViewPagerBaseFragment;
import com.tzw.noah.ui.home.HomeDetailActivity;
import com.tzw.noah.ui.home.HomeDetailGalleryActivity;
import com.tzw.noah.ui.home.HomeDetailVideoActivity;
import com.tzw.noah.ui.home.HomeMainActivity;
import com.tzw.noah.ui.mine.MineHistoryActivity;
import com.tzw.noah.ui.mine.fragment.itemfactory.CommentItemFactory;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.DividerItemDecoration;
import com.tzw.noah.widgets.MyRecyclerView;

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
public class MineHistoryFragment extends ViewPagerBaseFragment implements CommentItemFactory.MediaArticleDetailListener, OnRecyclerLoadMoreListener, MediaListListener {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.list_view)
    MyRecyclerView list_view;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.rl_loading)
    RelativeLayout rl_loading;
    @BindView(R.id.rl_error)
    RelativeLayout rl_error;

    String Tag = MineHistoryFragment.class.getName();
    Context mContext;

    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;
    MineHistoryFragment instance;
    private int type;

    boolean isFirstLoad = true;
    private int articleId = 0;
    int channelId;
    List<MediaArticle> items;

    Callback mycallback;

    public MineHistoryFragment setType(int type) {
        this.type = type;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

//        android.util.Log.d("aaa",getTitle()+"___onCreateView");
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
                if (items != null && items.size() == 0) {
                    setLoading();
                }
                refreshListView();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
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
        }
// else
//            refreshListView2();
    }

    @Override
    public String getTitle() {
//        if (mMediaCategory != null)
//            return mMediaCategory.channelName;
//        else
        return "";
    }

    public void setLoading() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        rl_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rl_bg.getVisibility() == View.VISIBLE) {
                    setError();
                }
            }
        }, DataCenter.INTEL_TIMEOUT);
    }

    public void setComplete() {
        rl_bg.setVisibility(View.GONE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
    }

    public void setError() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
        TextView btn = (TextView) rl_error.findViewById(R.id.btn_rlbg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_error.setVisibility(View.GONE);
                refreshListView();
            }
        });
    }

    private void refreshListView() {
//        if(1==1)
//            return;

        if (rl_error.getVisibility() == View.VISIBLE)
            return;

        mycallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                activity.toast(getResources().getString(R.string.internet_fault));
                mPtrFrame.refreshComplete();
                adapter.loadMoreFailed();
                setError();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        List<MyHistory> list = MyHistory.loadList(iMsg);
//                        items =
                        if (list == null)
                            list = new ArrayList();

                        items = new ArrayList<>();
                        for (MyHistory mh : list) {
                            if(mh.articleDetails!=null)
                            items.add(mh.articleDetails);
                        }
                        if (items == null) {
                            items = new ArrayList<MediaArticle>();
                        }

                        adapter.addAll(items);
                        if (list.size() > 0) {
//                            if (type == 0)
                            articleId = list.get(list.size() - 1).memberReadId;
//                            else articleId = items.get(items.size() - 1).articleCommentId;
                        }
                        adapter.loadMoreFinished(list.size() < DataCenter.pagesize);

                        setComplete();
                    } else {
                        setError();
                        activity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    setError();
                    Log.log(Tag, e);
                }
            }
        };
//        if (type == 0)
        NetHelper.getInstance().mediaMixMemberReadList(type, articleId, DataCenter.pagesize, mycallback);
//        else
//            NetHelper.getInstance().mediaMixRevertList(0, articleId, DataCenter.pagesize, mycallback);
    }

    protected void updateData() {
        List<MediaArticle> images = new ArrayList<MediaArticle>();

        items = new ArrayList<>();
        adapter = new AssemblyRecyclerAdapter(items);
//        adapter.addItemFactory(new CommentItemFactory(instance, (MyBaseActivity) getActivity()));
        adapter.addItemFactory(new MediaListPicItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDBigItemFatory(instance));
        adapter.addItemFactory(new MediaListTxtItemFatory(instance));
        adapter.addItemFactory(new MediaListDefaultItemFatory(instance));
        loadMoreItem = new LoadMoreItemFactory(instance);
        adapter.setLoadMoreItem(loadMoreItem);

        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        if (adapter.getItemCount() > 0)
            list_view.smoothScrollToPosition(0);
//        list_view.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.r ecycleview_divider_pt5));
        list_view.addOnItemTouchListener(new com.tzw.noah.widgets.listener.SimpleClickListener<AssemblyRecyclerAdapter>() {
            @Override
            public void onItemClick(AssemblyRecyclerAdapter adapter, View view, int position) {

            }
        });
        list_view.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.recycleview_divider_pt5));
    }

    private void loadMore() {
    }

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        refreshListView();
    }

    @Override
    public void onItemClick(int position, Object o) {

        if (HomeMainActivity.getShowMode()) {
            Utils.showObjectString(o, activity);
            return;
        }
        if (o instanceof MediaArticle) {
            if (((MediaArticle) o).isArticleTypeVideo()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailVideoActivity.class, bu);
                activity.overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
                return;
            } else if (((MediaArticle) o).isArticleTypPicGallery()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailGalleryActivity.class, bu);

                return;
            } else {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                activity.startActivity2(HomeDetailActivity.class, bu);
                activity.overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
            }
        } else if (o instanceof String) {
            ((MineHistoryActivity) getActivity()).refreshDeleteLayout();
        }
    }

    @Override
    public void onGalleryPageScrollStateChanged(boolean isViewPagerScrolling) {

    }

    @Override
    public void onCommentClick(int position, Object data) {

    }

//    @Override
//    public void onGalleryPageScrollStateChanged(boolean isViewPagerScrolling) {
//
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        android.util.Log.d("aaa",getTitle()+"___setUserVisibleHint");
        if (isVisibleToUser && activity != null)
            refreshListView();
    }

    public AssemblyRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void doDelete()
    {

    }
}
