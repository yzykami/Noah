package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Advertising;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.MySwipeBackActivity;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaGalleryRelativeFragmentItemFactory;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;
import com.tzw.noah.utils.StatusBarUtil;
import com.tzw.noah.widgets.swipeback.SwipeBackLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyFragmentStatePagerAdapter;
import me.xiaopan.sketchsample.adapter.itemfactory.ImageFragmentItemFactory;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.adapter.itemfactory.PageMenuItemFactory;
import me.xiaopan.sketchsample.bean.Image;
import me.xiaopan.sketchsample.fragment.ImageFragment;
import me.xiaopan.sketchsample.util.AppConfig;
import me.xiaopan.sketchsample.widget.DepthPageTransformer;
import me.xiaopan.sketchsample.widget.ZoomOutPageTransformer;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class HomeDetailGalleryActivity extends MySwipeBackActivity implements InputFragment.InputFragmentListener, MyBaseActivity.LoginListener, ViewPager.OnPageChangeListener, ImageFragmentItemFactory.ImageClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.maskView)
    View maskView;
    @BindView(R.id.tv_airtle_title)
    TextView tvAirtleTitle;
    @BindView(R.id.tv_pagesize)
    TextView tvPagesize;
    @BindView(R.id.tv_airtle_content)
    TextView tvAirtleContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.frame_input)
    FrameLayout frameInput;

    Context mContext = HomeDetailGalleryActivity.this;

    InputFragment frame_input;

    //    private AssemblyRecyclerAdapter adapter;
    static HomeDetailGalleryActivity instance;

    String Tag = "HomeDetailActivity";
    List<Object> items;

    String title = "";

    String htmlContent = "";
    MediaArticle mediaArticle;
    List<MediaArticle.GalleryArticle> list;
    private boolean isloading = false;
    private int isLike;
    private boolean isFavorite;
    //    String TAG_COMMENT = "全部评论";
//    String TAG_RELATE = "相关文章";
//    private LinearLayoutManager layoutManager;
    private boolean loginState;

    private int commentId = 0;

    private FragmentViewPagerAdapter fragmentAdapter;
    private boolean isLightOn = true;
    private Advertising advertising;
    int advPageIndex = -1;
    int relativePageIndex = -1;
    private int currentPageIndex;

//    private MediaArticleDetailWebViewItemFatory webViewItemFatory;

    public static HomeDetailGalleryActivity getInstance() {
        if (instance == null) {
            instance = new HomeDetailGalleryActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_article_detail_gallery);
        setDragEdge(SwipeBackLayout.DragEdge.TOP);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        setStatusBarHeight();
        instance = this;
        mLoginListener = this;
        initdata();
        findview();
        //放到网络调用结束来加载界面
//      initview();
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
    }

    private void findview() {
        rl_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_bg.setVisibility(View.GONE);
            }
        }, 4000);
        llContent.setVisibility(View.GONE);
    }

    private void loadData() {
        int advId = 23002;
        NetHelper.getInstance().mixArticleDetail(mediaArticle.articleId, advId, 1, new Callback() {
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
                        List<Advertising> advs = Advertising.loadList(iMsg);
                        if (advs.size() > 0) {
                            advertising = advs.get(0);
                        }
                        initview();
                        rl_bg.setVisibility(View.GONE);
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();
        frame_input = new InputFragment().setMediaArticle(mediaArticle).setMaskViewProvider(maskView).setListener(this).setBackgroundBlack(llContent);
        transition.replace(R.id.frame_input, frame_input);
        transition.commit();

        if (fragmentAdapter == null) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            list = mediaArticle.getContentList();
            for (int i = 0; i < list.size(); i++) {
//                fragments.add(new GalleryFragment().setGallery(list.get(i)).setPage(list.size(), i).setMediaCategory(mediaArticle));
//                titles.add("");
//                boolean showTools = AppConfig.getBoolean(mContext, AppConfig.Key.SHOW_TOOLS_IN_IMAGE_DETAIL);
//                Image image = new Image(list.get(i).image, list.get(i).image);
//                ImageFragment fragmentImage = ImageFragment.build(image, "", showTools);
//                fragments.add(fragmentImage);
            }

            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        }

        List<Image> imageList = new ArrayList<>();
        AssemblyFragmentStatePagerAdapter pagerAdapter = new AssemblyFragmentStatePagerAdapter(getSupportFragmentManager(), imageList);
        list = mediaArticle.getContentList();
        for (int i = 0; i < list.size(); i++) {
            Image image = new Image(list.get(i).image, list.get(i).image);
            imageList.add(image);
        }
        if (advertising != null) {
            advPageIndex = imageList.size();
            Image image = new Image(advertising.advertImage, advertising.advertImage);
            imageList.add(image);
        }
        if (mediaArticle.relatedArticlesObj.size() > 0) {
            relativePageIndex = imageList.size();
            pagerAdapter.addItemFactory(new MediaGalleryRelativeFragmentItemFactory(this, ""));
            pagerAdapter.getDataList().add(mediaArticle);
            //pagerAdapter.getDataList().add();
        }
        ImageFragmentItemFactory ifif;
        pagerAdapter.addItemFactory(ifif = new ImageFragmentItemFactory(this, ""));
        ifif.setImageClickListener(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 4.0上使用
            viewPager.setPageTransformer(true, new DepthPageTransformer());
        } else {
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
        llContent.setVisibility(View.VISIBLE);

        onPageSelected(0);
        updateData();
    }


    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {
    }

    public void handle_more(View view) {

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
    public void onLogin(boolean isLogin) {
        if (isLogin) {
            loadData();
        } else {
            toast("请先登录");
        }
    }


    @Override
    public void onCommentClick() {
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
//                            adapter.setNotifyOnChange(false);
//                            adapter.remove(adapter.getDataList().get(finalPosition));
//                            adapter.insert(mediaArticle.makeLiker(), finalPosition);
//                            adapter.notifyItemChanged(finalPosition);
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
//                            adapter.setNotifyOnChange(false);
//                            adapter.remove(adapter.getDataList().get(finalPosition));
//                            adapter.insert(mediaArticle.makeLiker(), finalPosition);
//                            adapter.notifyItemChanged(finalPosition);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageIndex = position;
        if (list != null && list.size() > position) {
            String ss = "恩格斯曾说过：“我们不要过分陶醉于我们人类对自然界的胜利。对于每一次这样的胜利，自然界都对我们进行报复。”\n" +
                    "西方的工业化进程中，中国的快速发展中，都经历了这样的报复。\n" +
                    "面对经济增长和保护环境的矛盾不断加剧，取舍之间，考验的是决心、勇气和担当。";
            ss = "";
            if (TextUtils.isEmpty(list.get(position).text)) {
                tvAirtleContent.setVisibility(View.GONE);
            } else {
                tvAirtleContent.setVisibility(View.VISIBLE);
                tvAirtleContent.setText(list.get(position).text + ss);
            }
            tvAirtleTitle.setText(mediaArticle.articleTitle);
            int i = position + 1;
            tvPagesize.setText(i + "/" + list.size());
        }
        if (position == advPageIndex) {

        }
        if (position == relativePageIndex) {

        }
        controlViewVisiable();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onImageClick() {
        if (isLightOn) {
            rl_top.setVisibility(View.GONE);
            llContent.setVisibility(View.GONE);
            frameInput.setVisibility(View.GONE);
        } else {
            rl_top.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.VISIBLE);
            frameInput.setVisibility(View.VISIBLE);
        }
        isLightOn = !isLightOn;
    }

    //切换广告或者相关文章
    public void controlViewVisiable() {
        if (currentPageIndex == advPageIndex) {
            tv_title.setText("广告");
            llContent.setVisibility(View.GONE);
            frameInput.setVisibility(View.GONE);
            rl_top.setVisibility(View.VISIBLE);
        } else if (currentPageIndex == relativePageIndex) {
            tv_title.setText("图集推荐");
            llContent.setVisibility(View.GONE);
            frameInput.setVisibility(View.VISIBLE);
            rl_top.setVisibility(View.VISIBLE);
        } else {
            tv_title.setText("");
            if (!isLightOn) {
                rl_top.setVisibility(View.GONE);
                llContent.setVisibility(View.GONE);
                frameInput.setVisibility(View.GONE);
            } else {
                rl_top.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.VISIBLE);
                frameInput.setVisibility(View.VISIBLE);
            }
        }
    }
}
