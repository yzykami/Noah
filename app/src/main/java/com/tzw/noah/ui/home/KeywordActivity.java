package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.SearchHistoryCache;
import com.tzw.noah.logger.Log;
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
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.DividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
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

public class KeywordActivity extends MySwipeBackActivity implements OnRecyclerLoadMoreListener, MediaListListener {

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
    //    @BindView(R.id.maskView)
//    View maskView;
    Context mContext = KeywordActivity.this;

    InputFragment frame_input;

    private AssemblyRecyclerAdapter adapter;
    KeywordActivity instance;
    String Tag = "KeywordActivity";

    List<MediaArticle> items;
    List<String> list_history;

    //    String title = "评论详情";
    String key = "";
    int keyId = 0;
    //    MediaArticle mediaArticle;
//    MediaComment mMediaComment;
    int articleId = 0;
    private boolean isloading = false;

    private LinearLayoutManager layoutManager;

    LoadMoreItemFactory loadMoreItem;

//    public static KeywordActivity getInstance() {
//        if (instance == null) {
//            instance = new KeywordActivity();
//        }
//        return instance;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout_search);
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
            key = bu.getString("key");
            String keyIdStr = bu.getString("keyId");
            try {
                keyId = Integer.parseInt(keyIdStr);
            } catch (Exception e) {

            }
        }

        rl_nohistory.setVisibility(View.GONE);
        ll_history.setVisibility(View.GONE);

        rl_search.setVisibility(View.GONE);
        rl_title.setVisibility(View.VISIBLE);
//        list_history = SearchHistoryCache.getSearchHistorys(mContext);
//        flowlayout.removeAllViews();
//        if (list_history.size() > 0) {
//            rl_nohistory.setVisibility(View.GONE);
//            ll_history.setVisibility(View.VISIBLE);
//            int height = Utils.dp2px(mContext, 24);
//            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            for (String key : list_history) {
//                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(width, height);
//                lp.setMargins(0, 0, Utils.dp2px(mContext, 10), 0);
//                final TextView tv = new TextView(mContext);
//                tv.setPadding(Utils.dp2px(mContext, 10), 0, Utils.dp2px(mContext, 10), 0);
//                tv.setTextColor(mContext.getResources().getColor(R.color.textDarkGray));
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                tv.setText(key);
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//                tv.setLines(1);
//                tv.setBackgroundResource(R.drawable.bg_gray_fill_round);
//                tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        et_keyword.setText(tv.getText());
//                        articleId = 0;
//                        addHistory(et_keyword.getText().toString());
//                        setLoading();
//                        doSearch();
//                        showKeyboard(false);
//                    }
//                });
//                flowlayout.addView(tv, lp);
//            }
//        }
    }

    private void findview() {


    }

//    private void loadData() {
//        NetHelper.getInstance().mediaArticleDetails(mediaArticle.articleId, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                toast(getResources().getString(R.string.internet_fault));
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                try {
//                    if (iMsg.isSucceed()) {
//                        mediaArticle = MediaArticle.load(iMsg);
//                        isLike = mediaArticle.isArticleEvaluate;
//                        isFavorite = mediaArticle.isArticleKeep;
//                        iMsg.systemOut();
//                        initview();
//                    } else {
//                        toast(iMsg.getMsg());
//                    }
//                } catch (Exception e) {
//                    Log.log(Tag, e);
//                }
//            }
//        });
//    }


    private void initview() {
        tv_title.setText(key);
        rl_bg.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.recycleview_divider_pt5));

//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit);
//            }
//        });
//        et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH
//                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    if (et_keyword.getText().toString().equals("")) {
////                        toast("请输入关键字");
//                        adapter.clear();
//                        initdata();
//                        return true;
//                    }
//
//                    articleId = 0;
//                    addHistory(et_keyword.getText().toString());
//                    setLoading();
//                    doSearch();
//                    showKeyboard(false);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        showKeyboardDelayed(et_keyword);

        initAdapter();

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

        NetHelper.getInstance().mediaKeywordList(keyId, articleId, DataCenter.pagesize, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
                setError();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        items = MediaArticle.loadSearchList(iMsg);
//                        items = new ArrayList<>();
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
                        if (items.size() >= 1) {
                            articleId = items.get(items.size() - 1).articleId;
                        }
                        adapter.loadMoreFinished(items.size() < DataCenter.pagesize);
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
}
