package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.MainActivity;
import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.SearchHistoryCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Favorite;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.MySwipeBackActivity;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDBigItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicUDItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListTxtItemFatory;
import com.tzw.noah.widgets.DividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lankton.flowlayout.FlowLayout;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class FavoriteActivity extends MySwipeBackActivity implements OnRecyclerLoadMoreListener, MediaListListener {

    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.rl_loading)
    RelativeLayout rl_loading;
    @BindView(R.id.rl_error)
    RelativeLayout rl_error;
    @BindView(R.id.rl_empty)
    RelativeLayout rl_empty;
    @BindView(R.id.rl_nohistory)
    RelativeLayout rl_nohistory;
    @BindView(R.id.ll_history)
    LinearLayout ll_history;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.et_keyword)
    EditText et_keyword;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.rl_delete)
    RelativeLayout rl_delete;
    //    @BindView(R.id.maskView)
//    View maskView;
    Context mContext = FavoriteActivity.this;

    InputFragment frame_input;

    private AssemblyRecyclerAdapter adapter;
    FavoriteActivity instance;
    String Tag = "FavoriteActivity";

    List<MediaArticle> items;
    List<String> list_history;

    //    String title = "评论详情";
    String key = "";
    int keyId = 0;
    //    MediaArticle mediaArticle;
//    MediaComment mMediaComment;
    int typeId = 0;
    int articleId = 0;
    private boolean isloading = false;

    boolean isEditMode = false;

    private LinearLayoutManager layoutManager;

    LoadMoreItemFactory loadMoreItem;

//    public static FavoriteActivity getInstance() {
//        if (instance == null) {
//            instance = new FavoriteActivity();
//        }
//        return instance;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout_favorite);
        ButterKnife.bind(this);
        setStatusBarLightMode();
        instance = this;
        initdata();
        findview();
        initview();
        setLoading();
        doSearch();
    }


    private void initdata() {

        Bundle bu = getIntent().getExtras();
        if (bu != null) {
//            key = bu.getString("key");
//            String keyIdStr = bu.getString("keyId");
//            try {
//                keyId = Integer.parseInt(keyIdStr);
//            } catch (Exception e) {
//
//            }
        }

        rl_nohistory.setVisibility(View.GONE);
        ll_history.setVisibility(View.GONE);

        rl_search.setVisibility(View.GONE);
        rl_title.setVisibility(View.VISIBLE);
    }

    private void findview() {


    }

    private void initview() {
        tv_title.setText("我的收藏");
        rl_bg.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.recycleview_divider_pt5));

        initAdapter();
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });
    }

    private void addHistory(String s) {
        if (list_history.contains(s))
            list_history.remove(s);
        list_history.add(0, s);
        SearchHistoryCache.setSearchHistorys(list_history);
        list_history = SearchHistoryCache.getSearchHistorys(mContext);
    }

    private void initAdapter() {
        items = new ArrayList<MediaArticle>();
        adapter = new AssemblyRecyclerAdapter(items);
        adapter.addItemFactory(new MediaListPicItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDItemFatory(instance));
        adapter.addItemFactory(new MediaListPicUDBigItemFatory(instance));
        adapter.addItemFactory(new MediaListTxtItemFatory(instance));
//        adapter.addItemFactory(new MediaListGalleryItemFatory(instance));
        loadMoreItem = new LoadMoreItemFactory(instance);
        adapter.setLoadMoreItem(loadMoreItem);

        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
//            loadData();
    }

    private void doSearch() {

        NetHelper.getInstance().mediaMixFavoriteList(typeId, articleId, DataCenter.pagesize, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
                setError();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        List<Favorite> list = Favorite.loadList(iMsg);
//                        items =
                        if (list == null)
                            list = new ArrayList();

                        items = new ArrayList<>();
                        for (Favorite fav : list) {
                            items.add(fav.articleDetails);
                        }
                        if (items == null) {
                            items = new ArrayList<MediaArticle>();
                        }
                        if (articleId == 0) {
                            adapter.clear();
                        }
                        //
                        for (int i = 0; i < items.size(); i++) {
                            MediaArticle ma = items.get(i);
                            if (ma.isListPicUDBig()) {
                                ma.listShowType = MediaArticle.LIST_TYPE_PIC_RL;
                                items.set(i, ma);
                            }
                        }
                        adapter.addAll(items);
                        if (list.size() >= 1) {
                            articleId = list.get(list.size() - 1).userKeepArticleId;
                        }
                        adapter.loadMoreFinished(list.size() < DataCenter.pagesize);
                        if (items.size() == 0 && articleId == 0)
                            setEmpty();
                        else
                            setComplete();
                    } else {
                        setError();
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    setError();
                    Log.log(Tag, e);
                }
            }
        });
    }


    public void doDelete() {
//        if (!makesureLogin()) {
//            return;
//        }
//        if (isloading)
//            return;
//        else
//            isloading = true;
        String ids = "";
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected)
                ids += list.get(i).articleId + ",";
        }
        if (TextUtils.isEmpty(ids))
            return;
        NetHelper.getInstance().mediaMixFavorite(0, ids, 0, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                isloading = false;
                ((MyBaseActivity) mContext).toast(mContext.getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                isloading = false;
                try {
                    if (iMsg.isSucceed()) {
                        ((MyBaseActivity) mContext).toast("取消收藏成功");
                        List<MediaArticle> list = adapter.getDataList();
                        Iterator<MediaArticle> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            MediaArticle ma = iterator.next();
                            if (ma.isSelected)
                                iterator.remove();
                        }
                        if (list.size() == 0)
                            setEmpty();
                        adapter.setDataList(list);
                        handle_edit(null);
                    } else {
                        ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        doSearch();
    }


    @Override
    public void onItemClick(int position, Object o) {
        if (o instanceof MediaArticle) {
            if (((MediaArticle) o).isArticleTypeVideo()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                startActivity2(HomeDetailVideoActivity.class, bu);
                return;
            } else if (((MediaArticle) o).isArticleTypPicGallery()) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                startActivity2NoOPT(HomeDetailGalleryActivity.class, bu);

                return;
            } else {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                startActivity2(HomeDetailActivity.class, bu);
            }
        } else if (o instanceof String) {

            refreshDeleteLayout();
        }
    }

    private void refreshDeleteLayout() {
        int count = 0;
        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected)
                count++;
        }
        if (count == 0) {
            tv_delete.setText("删除");
            tv_delete.setTextColor(getResources().getColor(R.color.textLightGray));
        } else {
            tv_delete.setText("删除(" + count + ")");
            tv_delete.setTextColor(getResources().getColor(R.color.textDarkGray));

        }
    }

    @Override
    public void onGalleryPageScrollStateChanged(boolean isViewPagerScrolling) {

    }

    public void setLoading() {
        rl_nohistory.setVisibility(View.GONE);
        ll_history.setVisibility(View.GONE);

        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        rl_empty.setVisibility(View.GONE);
//        rl_bg.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (rl_bg.getVisibility() == View.VISIBLE) {
//                    setError();
//                }
//            }
//        }, DataCenter.INTEL_TIMEOUT);
    }

    public void setComplete() {
        rl_bg.setVisibility(View.GONE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        rl_empty.setVisibility(View.GONE);
    }

    public void setError() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
        rl_empty.setVisibility(View.GONE);
        TextView btn = (TextView) rl_error.findViewById(R.id.btn_rlbg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_error.setVisibility(View.GONE);
                doSearch();
            }
        });
    }

    public void setEmpty() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        rl_empty.setVisibility(View.VISIBLE);
        TextView btn = (TextView) rl_empty.findViewById(R.id.btn_empty);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().selectTag(0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit);
    }

    @Override
    public void handle_back(View v) {
        super.handle_back(v);
        overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit);
    }

    public void handle_clear_history(View view) {
        SearchHistoryCache.setSearchHistorys(new ArrayList<String>());
        initdata();
    }

    public void handle_clear_editor(View view) {
        et_keyword.setText("");
    }


    public void handle_edit(View view) {
        isEditMode = !isEditMode;
        if (isEditMode) {
            tv_right.setText("取消");
            rl_delete.setVisibility(View.VISIBLE);
        } else {
            tv_right.setText("编辑");
            rl_delete.setVisibility(View.GONE);
        }

        List<MediaArticle> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isEditMode = isEditMode;
            if (isEditMode)
                list.get(i).isSelected = false;
        }
        adapter.setDataList(list);
        refreshDeleteLayout();

//        adapter.notifyDataSetChanged();
    }
}
