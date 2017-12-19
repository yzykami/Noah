package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.MySwipeBackActivity;
import com.tzw.noah.ui.adapter.itemfactory.SearchHeadFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDatailCommentItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailListener;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailSafaItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTagItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTitleItemFatory;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.ShareUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.util.AssemblyRecyclerAdapterTool;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class CommentListActivity extends MySwipeBackActivity implements MediaArticleDetailListener, OnRecyclerLoadMoreListener, SearchHeadFactory.OnItemClickListener, InputFragment.InputFragmentListener, MyBaseActivity.LoginListener {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.maskView)
    View maskView;

    @BindView(R.id.container)
    RelativeLayout container;
    Context mContext = CommentListActivity.this;

    InputFragment frame_input;

    private AssemblyRecyclerAdapter adapter;
    CommentListActivity instance;
    String Tag = "CommentListActivity";

    List<Object> items;

    String title = "评论详情";
    String htmlContent = "";
    MediaArticle mediaArticle;
    MediaComment mMediaComment;
    private boolean isloading = false;
    private int isLike;
    private boolean isFavorite;

    String TAG_COMMENT = "全部回复";
    String TAG_RELATE = "相关文章";
    private LinearLayoutManager layoutManager;
    private boolean loginState;
    private int commentId = 0;
    int index = 0;

//    public static CommentListActivity getInstance() {
//        if (instance == null) {
//            instance = new CommentListActivity();
//        }
//        return instance;
//    }

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
        initview();
//        loadData();
    }


    private void initdata() {
        loginState = UserCache.isLogin();
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
//            title = bu.getString("title");
//            mediaArticle = (MediaArticle) bu.getSerializable("DATA");
//            mMediaComment = (MediaComment) bu.getSerializable("DATA2");
//            mediaArticle = new MediaArticle();
//            mediaArticle.articleId = mMediaComment.webArticleId;
            index = bu.getInt("index");
        }


//        MediaComment mc = DataCenter.getInstance().getMediaArticle();
//        mMediaComment = MediaComment.Clone(mc);
        mediaArticle = DataCenter.getInstance().getMediaArticle();
        mMediaComment = mediaArticle.articleCommentObj.get(index);
//        mediaArticle.articleId = mMediaComment.webArticleId;
    }

    private void findview() {
        container.setBackgroundResource(R.color.bg_light);
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
        rl_bg.setVisibility(View.GONE);
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
        frame_input = new InputFragment().setMediaArticle(mediaArticle).setMediaComment(mMediaComment).setMaskViewProvider(maskView).setListener(this);
        transition.replace(R.id.frame_input, frame_input);
        transition.commit();

        initAdapter();

        updateData();
    }

    private void initAdapter() {
        items = new ArrayList<Object>();
        mMediaComment.isCommentDetail = true;
        mMediaComment.isTopCommentDetail = true;

        items.add(mMediaComment);
//        items.add(mediaArticle.makeDivider());
//        items.add(mediaArticle.makeTitle());
//        items.add(mediaArticle.makeContent());
//        if (mediaArticle.getKeywords().size() > 0)
//            items.add(mediaArticle.makeKeyword());
//        items.add(mediaArticle.makeLiker());
////        images.add(mediaArticle.makeAdvertise());
//
//        if(mediaArticle.relatedArticlesObj.size()>0)
//        {
//            items.add(mediaArticle.makeDivider());
//            items.add(mediaArticle.makeTag(TAG_RELATE));
//            for (int i = 0; i < mediaArticle.relatedArticlesObj.size(); i++) {
//                mediaArticle.relatedArticlesObj.get(i).articleCommentSum=-1;
//                items.add(mediaArticle.relatedArticlesObj.get(i));
//            }
//        }

        items.add(mediaArticle.makeTag(TAG_COMMENT));
        if (mMediaComment.repliesNumber > 0) {
//            items.add(mediaArticle.makeDivider());
//            for (int i = 0; i < mediaArticle.articleCommentObj.size(); i++) {
//                items.add(mediaArticle.articleCommentObj.get(i));
//            }
        } else {
            items.add(mediaArticle.makeSafa());
        }

        adapter = new AssemblyRecyclerAdapter(items);
        adapter.addItemFactory(new MediaArticleDetailTitleItemFatory(this));
//        adapter.addItemFactory(new MediaArticleDetailWebViewItemFatory(this));
//        adapter.addItemFactory(new MediaArticleKeywordItemFatory(this));
//        adapter.addItemFactory(new MediaArticleLikeItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailDividerItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailTagItemFatory(this));
//        adapter.addItemFactory(new MediaArticleDetailAdvertiseItemFatory(this));
        adapter.addItemFactory(new MediaArticleDatailCommentItemFactory(this));
        adapter.addItemFactory(new MediaArticleDetailSafaItemFatory(this));
//        adapter.addItemFactory(new MediaListTxtItemFatory(this));
//        adapter.addItemFactory(new AdvListPicItemFatory(this));

        if (mMediaComment.repliesNumber > 0) {
            loadMoreItem = new LoadMoreItemFactory(this);
            adapter.setLoadMoreItem(loadMoreItem);
//            if (mediaArticle.articleCommentObj.size() < DataCenter.service_pagesize)
//                adapter.setLoadMoreEnd(true);
        }

        recyclerView.setAdapter(adapter);
        count = 0;
    }


    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginState != UserCache.isLogin()) {
            loginState = UserCache.isLogin();
//            loadData();
        }
    }

    private void doWorking() {
    }

    public void handle_more(View view) {
        new ShareUtil(mContext, this).show();
    }

    @Override
    public void onSearchClick(int position, Object optionsKey) {
        toast("position = " + position);
    }

    int count = 0;

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        NetHelper.getInstance().mediaCommentList(mediaArticle.articleId, mMediaComment.articleCommentId, commentId, DataCenter.pagesize, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
//                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        List<MediaComment> list = MediaComment.loadList2(iMsg);
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
//            loadData();
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
                startActivity2(CommentListActivity.class, bu);
                this.finish();
            }
        }
        if (o instanceof MediaComment) {
            User u = new User();
            u.memberNo = ((MediaComment) o).memberNo;
            u.memberNickName = ((MediaComment) o).memberNickName;
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", u);
            startActivity2(PersonalActivity.class, bu);
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
        bu.putString("articleTitle", mediaArticle.articleTitle);
        bu.putString("createTime", mediaArticle.createTime);
        bu.putString("author", mediaArticle.getAuthor());
        startActivity2(LikeListActivity.class, bu);
    }

    @Override
    public void onCommentClick(int adapterPosition, MediaComment data) {
//// TODO: 2017-09-16
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

    @Override
    public void onCommentClick() {
//        int position = getfirstPosition(MediaArticle.TYPE_TAG, TAG_COMMENT);
//        if (position == -1) {
//            position = getfirstPosition(MediaArticle.TYPE_SAFA);
//            if (position != -1) {
//                frame_input.switchEditMode(true);
//                showKeyboardDelayed(frame_input.getEditView(), 100);
//            }
//        }
//        if (position == -1)
//            return;
//        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
//            @Override
//            protected int getVerticalSnapPreference() {
//                return LinearSmoothScroller.SNAP_TO_START;
//            }
//        };
//        smoothScroller.setTargetPosition(position);
//        layoutManager.startSmoothScroll(smoothScroller);

        frame_input.switchEditMode(true);
        showKeyboardDelayed(frame_input.getEditView(), 100);
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
                        int position = getfirstPosition(MediaArticle.TYPE_SAFA);
                        if (position != -1) {
                            isFirstComment = true;
                            adapter.remove(adapter.getDataList().get(position));
//                            adapter.insert(mediaArticle.makeDivider(), position++);
//                            adapter.insert(mediaArticle.makeTag(TAG_COMMENT), position++);
                        } else {
                            position = getfirstPosition(MediaArticle.TYPE_TAG, TAG_COMMENT) + 1;
                        }
                        MediaComment mc = MediaComment.load(iMsg);
//                        mc.memberHeadPic=UserCache.getUser().memberHeadPic;
//                        mc.memberNickName=UserCache.getUser().memberNickName;
//                        mc.commentContent = content;
                        mc.isCommentDetail = true;
//                        mediaArticle.articleCommentObj.get(index).sonList().add(0, mc);
                        mMediaComment.sonList().add(0,mc);
                        mMediaComment.repliesNumber++;
                        adapter.insert(mc, position);
                        adapter.notifyDataSetChanged();
                        mediaArticle.articleCommentSum++;
                        if (isFirstComment) {
                            AssemblyRecyclerAdapterTool.unLock(adapter);
                            loadMoreItem = new LoadMoreItemFactory(instance);
                            adapter.setLoadMoreItem(loadMoreItem);
                            adapter.setLoadMoreEnd(true);
//                            recyclerView.setAdapter(adapter);
                        }
                        frame_input.switchEditMode(false);
                        frame_input.clearInputEdit();
                        showKeyboard(false);
//                        onCommentClick();
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
        NetHelper.getInstance().mediaEvaluate(mediaArticle.articleId, isLike == 0 ? 1 : 0,0, new StringDialogCallback(mContext) {
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
                            frame_input.setLikeAnima();
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
        NetHelper.getInstance().mediaMixFavorite(0, mediaArticle.articleId + "", isFavorite ? 0 : 1, new StringDialogCallback(mContext) {
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
                            frame_input.setFavAnima();
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
