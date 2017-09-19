package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.ChatListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.SearchHeadFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDatailCommentItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailAdvertiseItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailListener;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailSafaItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTagItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTitleItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailWebViewItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleKeywordItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleLikeItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListPicItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListTxtItemFatory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import okhttp3.Call;

import static android.R.attr.id;

/**
 * Created by yzy on 2017/8/11.
 */

public class HomeDetailActivity extends MyBaseActivity implements MediaArticleDetailListener, OnRecyclerLoadMoreListener, SearchHeadFactory.OnItemClickListener, InputFragment.InputFragmentListener, MyBaseActivity.LoginListener, MediaListListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.maskView)
    View maskView;
    Context mContext = HomeDetailActivity.this;

    InputFragment frame_input;

    private AssemblyRecyclerAdapter adapter;
    static HomeDetailActivity instance;
    String Tag = "HomeDetailActivity";

    List<Object> items;

    String title = "";
    String htmlContent = "";
    MediaArticle mediaArticle;
    private boolean isloading = false;
    private int isLike;
    private boolean isFavorite;

    String TAG_COMMENT = "全部评论";
    String TAG_RELATE = "相关文章";
    private LinearLayoutManager layoutManager;
    private boolean loginState;

    private int commentId = 0;

    public static HomeDetailActivity getInstance() {
        if (instance == null) {
            instance = new HomeDetailActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_article_detail);
        ButterKnife.bind(this);
        setStatusBarLightMode();
        instance = this;
        mLoginListener = this;
        initdata();
        findview();
        //放到网络调用结束来加载界面
//        initview();
        loadData();
    }


    private void initdata() {
        loginState = UserCache.isLogin();
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            title = bu.getString("title");
            mediaArticle = (MediaArticle) bu.getSerializable("DATA");
            htmlContent = mediaArticle.articleContent;
        }
        title = "";
    }

    private void findview() {
        rl_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                onWebViewLoadComplete();
            }
        }, 5000);
    }

    private void loadData() {
        NetHelper.getInstance().mediaArticleDetails(mediaArticle.articleId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        mediaArticle = MediaArticle.load(iMsg);
                        isLike = mediaArticle.isArticleEvaluate;
                        isFavorite = mediaArticle.isArticleKeep;
                        if (mediaArticle.articleCommentObj.size() > 0)
                            commentId = mediaArticle.articleCommentObj.get(mediaArticle.articleCommentObj.size() - 1).articleCommentId;
                        iMsg.systemOut();
                        initview();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }


    private void initview() {
        tv_title.setText(title);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        } else {
//            refreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    onRefresh();
//                }
//            });
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();
        frame_input = new InputFragment().setMediaArticle(mediaArticle).setMaskViewProvider(maskView).setListener(this);
        transition.replace(R.id.frame_input, frame_input);
        transition.commit();

        updateData();
    }


    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {

        items = new ArrayList<Object>();
        items.add(mediaArticle.makeTitle());
        items.add(mediaArticle.makeContent());
        if (mediaArticle.getKeywords().size() > 0)
            items.add(mediaArticle.makeKeyword());
        items.add(mediaArticle.makeLiker());
//        images.add(mediaArticle.makeAdvertise());

        if (mediaArticle.relatedArticlesObj.size() > 0) {
            items.add(mediaArticle.makeDivider());
            items.add(mediaArticle.makeTag(TAG_RELATE));
            for (int i = 0; i < mediaArticle.relatedArticlesObj.size(); i++) {
                mediaArticle.relatedArticlesObj.get(i).articleCommentSum = -1;
                items.add(mediaArticle.relatedArticlesObj.get(i));
            }
        }

        if (mediaArticle.articleCommentObj.size() > 0) {
            items.add(mediaArticle.makeDivider());
            items.add(mediaArticle.makeTag(TAG_COMMENT));
            for (int i = 0; i < mediaArticle.articleCommentObj.size(); i++) {
                items.add(mediaArticle.articleCommentObj.get(i));
            }
        } else {
            items.add(mediaArticle.makeSafa());
        }

        adapter = new AssemblyRecyclerAdapter(items);
        adapter.addItemFactory(new MediaArticleDetailTitleItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailWebViewItemFatory(this));
        adapter.addItemFactory(new MediaArticleKeywordItemFatory(this));
        adapter.addItemFactory(new MediaArticleLikeItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailDividerItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailTagItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailAdvertiseItemFatory(this));
        adapter.addItemFactory(new MediaArticleDatailCommentItemFactory(this));
        adapter.addItemFactory(new MediaArticleDetailSafaItemFatory(this));

        adapter.addItemFactory(new MediaListTxtItemFatory(this));
        adapter.addItemFactory(new MediaListPicItemFatory(this));

        if (mediaArticle.articleCommentObj.size() > 0) {
            loadMoreItem = new LoadMoreItemFactory(this);
            adapter.setLoadMoreItem(loadMoreItem);
            if (mediaArticle.articleCommentObj.size() < DataCenter.service_pagesize)
                adapter.setLoadMoreEnd(true);
        }

        recyclerView.setAdapter(adapter);
        count = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginState != UserCache.isLogin()) {
            loginState = UserCache.isLogin();
            loadData();
        }
    }

    private void doWorking() {
    }

    @Override
    public void onSearchClick(int position, Object optionsKey) {
        toast("position = " + position);
    }

    int count = 0;

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        NetHelper.getInstance().mediaCommentList(mediaArticle.articleId, commentId, DataCenter.pagesize, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
//                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        List<MediaComment> list = MediaComment.loadList(iMsg);
                        items = new ArrayList<>();
                        if (list == null) {
                            list = new ArrayList<MediaComment>();
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).isCommentDetail = true;
                                items.add(list.get(i));
                            }
                        }
//                        if (articleId == 0)
//                            adapter.clear();
                        adapter.addAll(items);
                        if (list.size() >= 1)
                            commentId = list.get(list.size() - 1).articleCommentId;
                        adapter.loadMoreFinished(list.size() < DataCenter.pagesize);
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private int getfirstPosition(int type, String... tags) {
        String tag = "";
        if (tags != null && tags.length > 0)
            tag = tags[0];
        int position = -1;
        for (int i = 0; i < adapter.getDataList().size(); i++) {
            if (adapter.getDataList().get(i) instanceof MediaArticle) {
                if (((MediaArticle) adapter.getDataList().get(i)).TYPE == type) {
                    if (TextUtils.isEmpty(tag)) {
                        return i;
                    } else if (tag.equals(((MediaArticle) adapter.getDataList().get(i)).tag)) {
                        return i;
                    }
                }
            } else if (adapter.getDataList().get(i) instanceof MediaComment) {
                if (type == MediaArticle.TYPE_COMMENT) {
                    return i;
                }
            }
        }
        return position;
    }


    @Override
    public void onLogin(boolean isLogin) {
        if (isLogin) {
            loadData();
        } else {
            toast("请先登录");
        }
    }

    @Override
    public void onItemClick(int position, Object o) {
        if (o instanceof MediaArticle) {
            if (((MediaArticle) o).isSafa()) {
                frame_input.switchEditMode(true);
                showKeyboardDelayed(frame_input.getEditView(), 0);
            }
            if (((MediaArticle) o).TYPE == MediaArticle.TYPE_LIST) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (MediaArticle) o);
                startActivity2(HomeDetailActivity.class, bu);
                this.finish();
            }
        }
    }

    @Override
    public void onLikeClick(int position, Object o) {
        likeClick();
    }

    @Override
    public void onWebViewLoadComplete() {
        rl_bg.setVisibility(View.GONE);
    }

    @Override
    public void onLikeMemberClick(int position, Object o) {
        Bundle bu = new Bundle();
        bu.putInt("articleId", mediaArticle.articleId);
        startActivity2(LikeListActivity.class, bu);
    }

    @Override
    public void onCommentClick(int adapterPosition, MediaComment data) {
//// TODO: 2017-09-16
//        Bundle bu=new Bundle();
//        bu.putSerializable("DATA", mediaArticle);
//        bu.putSerializable("DATA2", data);
        DataCenter.getInstance().setMediaComment(data);
        startActivity2(CommentListActivity.class);
    }

    @Override
    public void onCommentClick() {
        int position = getfirstPosition(MediaArticle.TYPE_TAG, TAG_COMMENT);
        if (position == -1) {
            position = getfirstPosition(MediaArticle.TYPE_SAFA);
            if (position != -1) {
                frame_input.switchEditMode(true);
                showKeyboardDelayed(frame_input.getEditView(), 100);
            }
        }
        if (position == -1)
            return;
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(position);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    @Override
    public void onLikeClick() {

        likeClick();
    }

    @Override
    public void onFavoriteClick() {
        favoriteClick();
    }

    @Override
    public void onSendClick(final String content, int aId, int cId) {
        if (!makesureLogin()) {
            return;
        }
        if (isloading)
            return;
        else
            isloading = true;

        NetHelper.getInstance().mediaComment(aId, content, cId, new StringDialogCallback(mContext) {
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
                        boolean isFirstComment = false;
                        adapter.setNotifyOnChange(false);
                        int position = getfirstPosition(MediaArticle.TYPE_COMMENT);
                        if (position == -1) {
                            position = getfirstPosition(MediaArticle.TYPE_SAFA);
                            isFirstComment = true;
                            adapter.remove(adapter.getDataList().get(position));
                            adapter.insert(mediaArticle.makeDivider(), position++);
                            adapter.insert(mediaArticle.makeTag(TAG_COMMENT), position++);
                        }
                        MediaComment mc = MediaComment.load(iMsg);
//                        mc.memberHeadPic=UserCache.getUser().memberHeadPic;
//                        mc.memberNickName=UserCache.getUser().memberNickName;
//                        mc.commentContent = content;
                        mediaArticle.articleCommentObj.add(0, mc);
                        adapter.insert(mc, position);
                        adapter.notifyDataSetChanged();
                        if (isFirstComment) {
                            loadMoreItem = new LoadMoreItemFactory(instance);
                            adapter.setLoadMoreItem(loadMoreItem);
                            adapter.setLoadMoreEnd(true);
                            recyclerView.setAdapter(adapter);
                        }
                        frame_input.switchEditMode(false);
                        frame_input.clearInputEdit();
                        showKeyboard(false);
                        onCommentClick();
                        ((MyBaseActivity) mContext).toast("发表成功");
                    } else {
                        ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    public void likeClick() {
        if (!makesureLogin()) {
            return;
        }
//        if(isLike==1)
//            return;
        if (isloading)
            return;
        else
            isloading = true;
        int position = 0;
        for (int i = 0; i < adapter.getDataList().size(); i++) {
            if (adapter.getDataList().get(i) instanceof MediaArticle) {
                if (((MediaArticle) adapter.getDataList().get(i)).isLike()) {
                    position = i;
                    break;
                }
            }
        }
        final int finalPosition = position;
        NetHelper.getInstance().mediaEvaluate(mediaArticle.articleId, isLike == 0 ? 1 : 0, new StringDialogCallback(mContext) {
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
                        isLike = isLike == 0 ? 1 : 0;
                        if (isLike == 1) {
                            mediaArticle.isArticleEvaluate = isLike;
                            mediaArticle.praiseNumber++;
                            MediaLike ml = new MediaLike();
                            ml.memberHeadPic = UserCache.getUser().memberHeadPic;
                            ml.memberNickName = UserCache.getUser().memberNickName;
                            ml.memberNo = UserCache.getUser().memberNo;
                            ml.evaluateValue = isLike;
                            mediaArticle.articleEvaluateObj.add(0, ml);
                            adapter.setNotifyOnChange(false);
                            adapter.remove(adapter.getDataList().get(finalPosition));
                            adapter.insert(mediaArticle.makeLiker(), finalPosition);
                            adapter.notifyItemChanged(finalPosition);
                            frame_input.notifyUpdate(mediaArticle);
                            ((MyBaseActivity) mContext).toast("点赞成功");
                        } else {
                            mediaArticle.praiseNumber--;
                            if (mediaArticle.praiseNumber <= 0)
                                mediaArticle.praiseNumber = 0;
                            mediaArticle.isArticleEvaluate = isLike;
                            List<MediaLike> mls = mediaArticle.articleEvaluateObj;
                            for (int i = 0; i < mls.size(); i++) {
                                if (mls.get(i).memberNo == UserCache.getUser().memberNo) {
                                    mls.remove(i);
                                    break;
                                }
                            }
                            adapter.setNotifyOnChange(false);
                            adapter.remove(adapter.getDataList().get(finalPosition));
                            adapter.insert(mediaArticle.makeLiker(), finalPosition);
                            adapter.notifyItemChanged(finalPosition);
                            frame_input.notifyUpdate(mediaArticle);
                            ((MyBaseActivity) mContext).toast("取消点赞成功");
                        }
                    } else {
                        ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    public void favoriteClick() {
        if (!makesureLogin()) {
            return;
        }
        if (isloading)
            return;
        else
            isloading = true;
        int position = 0;
        for (int i = 0; i < adapter.getDataList().size(); i++) {
            if (adapter.getDataList().get(i) instanceof MediaArticle) {
                if (((MediaArticle) adapter.getDataList().get(i)).isLike()) {
                    position = i;
                    break;
                }
            }
        }
        final int finalPosition = position;
        NetHelper.getInstance().mediaFavorite(mediaArticle.articleId, isFavorite ? 0 : 1, new StringDialogCallback(mContext) {
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
                        isFavorite = !isFavorite;
                        mediaArticle.isArticleKeep = isFavorite;
                        frame_input.notifyUpdate(mediaArticle);
                        if (isFavorite) {
                            ((MyBaseActivity) mContext).toast("收藏成功");
                        } else {

                            ((MyBaseActivity) mContext).toast("取消收藏成功");
                        }
                    } else {
                        ((MyBaseActivity) mContext).toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
