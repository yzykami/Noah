package com.tzw.noah.ui.home;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Advertising;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.SearchHeadFactory;
import com.tzw.noah.ui.adapter.itemfactory.advertising.AdvDetailItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDatailCommentItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailAdvertiseItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerLineItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerWhiteItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailListener;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailSafaItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTagItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTitleItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailWebViewItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleKeywordItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleLikeItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleRelativeItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.widgets.MyWebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.widget.SampleImageView;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class HomeDetailActivity extends MyBaseActivity implements MediaArticleDetailListener, OnRecyclerLoadMoreListener, SearchHeadFactory.OnItemClickListener, InputFragment.InputFragmentListener, MyBaseActivity.LoginListener, MediaListListener, StandardVideoAllCallBack {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.rl_loading)
    RelativeLayout rl_loading;
    @BindView(R.id.rl_error)
    RelativeLayout rl_error;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.rl_divider)
    View divider;
    @BindView(R.id.videoLayout)
    RelativeLayout videoLayout;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.maskView)
    View maskView;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.video_item_player)
    com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer videoPlayer;
    @BindView(R.id.rl_video_cover)
    RelativeLayout rl_video_cover;
    @BindView(R.id.iv_adv)
    SampleImageView iv_adv;
    @BindView(R.id.tv_adv_close)
    TextView tv_adv_close;
    @BindView(R.id.tv_adv_reply)
    TextView tv_adv_reply;

    private boolean isPlay;
    private boolean isPause;

    Context mContext = HomeDetailActivity.this;

    InputFragment frame_input;

    private AssemblyRecyclerAdapter adapter;
    static HomeDetailActivity instance;
    String Tag = "HomeDetailActivity";

    List<Object> items;

    String title = "";
    String htmlContent = "";
    MediaArticle mediaArticle;
    Advertising advertising;
    private boolean isloading = false;
    private int isLike;
    private boolean isFavorite;

    String TAG_COMMENT = "全部评论";
    String TAG_RELATE = "相关文章";
    private LinearLayoutManager layoutManager;
    private boolean loginState;

    private int commentId = 0;
    private MediaArticleDetailWebViewItemFatory webViewItemFatory;
    private OrientationUtils orientationUtils;

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
            htmlContent = mediaArticle.getContentString();
        }
        title = "";
        if (mediaArticle.isArticleTypeVideo()) {
            videoPlayer.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            videoPlayer.setVisibility(View.GONE);

        }
    }

    private void findview() {

    }

    private void loadData() {

        setLoading();
        int advId = 23001;
        if (mediaArticle.isArticleTypeVideo())
            advId = 23003;
        NetHelper.getInstance().mixArticleDetail(mediaArticle.articleId, advId, 1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                toast(getResources().getString(R.string.internet_fault));
                setError();
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
                        List<Advertising> advs = Advertising.loadList(iMsg);
                        if (advs.size() > 0) {
                            advertising = advs.get(0);
                        }
                        initview();
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

        if (mediaArticle.isArticleTypeVideo()) {
            setComplete();
            initVideo();
        }

        updateData();
    }

    private void initVideo() {

        statusBar.setVisibility(View.GONE);
        statusBar.setBackgroundResource(R.color.transParent);
        rl_top.setBackgroundResource(R.color.transParent);
        iv_back.setImageResource(R.drawable.back_white);
        iv_detail.setImageResource(R.drawable.more_white);
//        rl_top.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        lp.addRule(RelativeLayout.BELOW, R.id.video_item_player);
        recyclerView.setLayoutParams(lp);

        if (mediaArticle.videoObj.size() > 0) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, videoPlayer);
            orientationUtils.setEnable(false);
            String url = mediaArticle.videoObj.get(0).articleRessourceUrl;
            videoPlayer.setUp(url, false, "");
            videoPlayer.getBackButton().setVisibility(View.GONE);
            videoPlayer.startPlayLogic();
            videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接横屏
                    orientationUtils.resolveByClick();

                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    videoPlayer.startWindowFullscreen(HomeDetailActivity.this, true, true);
                }
            });
            videoPlayer.setStandardVideoAllCallBack(this);
        }
    }

    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {

        items = new ArrayList<Object>();
        if (mediaArticle.isArticleTypeVideo()) {
//            items.add(mediaArticle.makeVideo());
        }
        items.add(mediaArticle.makeTitle());

        if (mediaArticle.isArticleTypeArticle())
            items.add(mediaArticle.makeContent());

        if (mediaArticle.getKeywords().size() > 0)
            items.add(mediaArticle.makeKeyword());

        items.add(mediaArticle.makeLiker());
        //广告
        if (advertising != null && mediaArticle.isArticleTypeArticle()) {

            items.add(mediaArticle.makeDividerWhite());
            items.add(advertising);
        }
//        images.add(mediaArticle.makeAdvertise());


        // 相关文章
        if (mediaArticle.relatedArticlesObj.size() > 0) {
            //增加一个白色分割区
//            if (advertising == null)//如果增加该判断,则在有广告的时候不加
            items.add(mediaArticle.makeDividerWhite());
//            items.add(mediaArticle.makeTag(TAG_RELATE));
            //图文显示4条, 视频显示8条
            int maxcount = 4;
            if (mediaArticle.isArticleTypeVideo())
                maxcount = 8;
            for (int i = 0; i < mediaArticle.relatedArticlesObj.size() && i < maxcount; i++) {
                mediaArticle.relatedArticlesObj.get(i).articleCommentSum = -1;
                items.add(mediaArticle.relatedArticlesObj.get(i));
                if (i < mediaArticle.relatedArticlesObj.size() - 1 && i < maxcount-1 )
                    items.add(mediaArticle.makeDividerLine());
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
        adapter.addItemFactory(webViewItemFatory = new MediaArticleDetailWebViewItemFatory(this));
        adapter.addItemFactory(new MediaArticleKeywordItemFatory(this));
        adapter.addItemFactory(new MediaArticleLikeItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailDividerItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailDividerLineItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailDividerWhiteItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailTagItemFatory(this));
        adapter.addItemFactory(new MediaArticleDetailAdvertiseItemFatory(this));
        adapter.addItemFactory(new MediaArticleDatailCommentItemFactory(this));
        adapter.addItemFactory(new MediaArticleDetailSafaItemFatory(this));

        adapter.addItemFactory(new MediaArticleRelativeItemFatory(this));

        adapter.addItemFactory(new AdvDetailItemFatory(this));

        if (mediaArticle.articleCommentObj.size() > 0) {
            loadMoreItem = new LoadMoreItemFactory(this);
            adapter.setLoadMoreItem(loadMoreItem);
            if (mediaArticle.articleCommentObj.size() < DataCenter.service_pagesize)
                adapter.setLoadMoreEnd(true);
        }

        recyclerView.setAdapter(adapter);
        count = 0;
    }


    private void doWorking() {
    }

    public void handle_more(View view) {

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
    }

    public void setError() {
        rl_bg.setVisibility(View.VISIBLE);
        rl_loading.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
        TextView btn = (TextView) rl_error.findViewById(R.id.btn_rlbg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
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
                if (o instanceof MediaArticle) {
                    if (((MediaArticle) o).isArticleTypeVideo()) {
                        Bundle bu = new Bundle();
                        bu.putSerializable("DATA", (MediaArticle) o);
                        startActivity2(HomeDetailActivity.class, bu);
                        this.finish();
                        return;
                    } else if (((MediaArticle) o).isArticleTypPicGallery()) {
                        Bundle bu = new Bundle();
                        bu.putSerializable("DATA", (MediaArticle) o);
                        startActivity2(HomeDetailGalleryActivity.class, bu);
                        this.finish();
                        return;
                    } else {
                        Bundle bu = new Bundle();
                        bu.putSerializable("DATA", (MediaArticle) o);
                        startActivity2(HomeDetailActivity.class, bu);
                        this.finish();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onGalleryPageScrollStateChanged(boolean isViewPagerScrolling) {

    }

    @Override
    public void onLikeClick(int position, Object o) {
        likeClick();
    }

    @Override
    public void onWebViewLoadComplete() {
        setComplete();
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
    public void onComplaintClick() {
        if (!makesureLogin()) {
            return;
        }
        Bundle bu = new Bundle();
        bu.putSerializable("articleId", mediaArticle.articleId);
        startActivity2(MediaComplaintActivity.class, bu);
    }

    @Override
    public void toggledFullscreen(boolean fullscreen) {
// Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
        if (fullscreen) {
//            setTheme(R.style.AppTheme_NoActionBarBlack);
            //noinspection all
//            WindowManager.LayoutParams attrs = getWindow().getAttributes();
//            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//            getWindow().setAttributes(attrs);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
//            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//            getWindow().setAttributes(attrs);
//            setTheme(R.style.AppTheme_NoActionBar);
//            setStatusBarLightMode();
            //noinspection all
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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

        Bundle bu = new Bundle();
        bu.putSerializable("DATA", mediaArticle);
//        bu.putSerializable("DATA2", data);
//        DataCenter.getInstance().setMediaComment(mMediaComment);
        startActivity2(GalleryCommentListActivity.class, bu);
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

    private void showVideoAdv() {
        if (advertising != null) {
            iv_detail.setVisibility(View.GONE);
            rl_video_cover.setVisibility(View.VISIBLE);
            iv_adv.displayImage(advertising.advertImage);
            final CountDownTimer timer = new CountDownTimer(15 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //每隔countDownInterval秒会回调一次onTick()方法
//                    Log.d(TAG, "onTick  " + millisUntilFinished / 1000);
                    tv_adv_close.setText(millisUntilFinished / 1000 + " | 关闭广告 ");
                }

                @Override
                public void onFinish() {
//                    Log.d(TAG, "onFinish -- 倒计时结束");
                    iv_detail.setVisibility(View.VISIBLE);
                    rl_video_cover.setVisibility(View.GONE);
                }
            };
            timer.start();// 开始计时
            tv_adv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                    iv_detail.setVisibility(View.VISIBLE);
                    rl_video_cover.setVisibility(View.GONE);
                }
            });
            tv_adv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_detail.setVisibility(View.VISIBLE);
                    rl_video_cover.setVisibility(View.GONE);
                    videoPlayer.startPlayLogic();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        if (loginState != UserCache.isLogin()) {
            loginState = UserCache.isLogin();
            loadData();
        }
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
//        GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        if (mediaArticle.isArticleTypeArticle())
            if (webViewItemFatory != null) {
                MyWebView webView = webViewItemFatory.item.getWebView();
                if (webView != null)
                    webView.destroy();
            }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (mediaArticle.isArticleTypeVideo()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (isPlay && !isPause) {
                videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
            }
        }
        //网页视频
        else if (mediaArticle.isArticleTypeArticle()) {
            int o = newConfig.orientation;
            View view = videoLayout.getChildAt(0);
            if (view != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                layoutParams.width = videoLayout.getHeight();
                layoutParams.height = videoLayout.getWidth();
                view.setLayoutParams(layoutParams);
//            View focusedChild = view.getFocusedChild();
                //
//            Log.log("orientation",view.toString());
//            Log.log("orientation",  o + "; " + view.getWidth() + "," + view.getHeight() + " | " + videoLayout.getWidth() + "," + videoLayout.getHeight() + " | " + container.getWidth() + "," + container.getHeight());
            }
        }
    }


    private void resolveNormalVideoUI() {
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (videoPlayer.getFullWindowPlayer() != null) {
            return videoPlayer.getFullWindowPlayer();
        }
        return videoPlayer;
    }


    //video Interface
    @Override
    public void onPrepared(String url, Object... objects) {
        if (orientationUtils == null)
            return;
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        isPlay = true;
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {

    }

    @Override
    public void onClickStartError(String url, Object... objects) {

    }

    @Override
    public void onClickStop(String url, Object... objects) {

    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickResume(String url, Object... objects) {

    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {

    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        showVideoAdv();
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {

    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {

    }

    @Override
    public void onPlayError(String url, Object... objects) {

    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {

    }

    @Override
    public void onClickBlank(String url, Object... objects) {

    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {

    }
}
