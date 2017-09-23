package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.net.Callback;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.circle.FragmentViewPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import okhttp3.Call;

/**
 * Created by yzy on 2017/8/11.
 */

public class HomeDetailGalleryActivity2 extends MyBaseActivity implements InputFragment.InputFragmentListener, MyBaseActivity.LoginListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    //    @BindView(R.id.list_view)
//    RecyclerView recyclerView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.maskView)
    View maskView;

    Context mContext = HomeDetailGalleryActivity2.this;

    InputFragment frame_input;

    //    private AssemblyRecyclerAdapter adapter;
    static HomeDetailGalleryActivity2 instance;
    String Tag = "HomeDetailActivity";

    List<Object> items;

    String title = "";
    String htmlContent = "";
    MediaArticle mediaArticle;
    private boolean isloading = false;
    private int isLike;
    private boolean isFavorite;

    //    String TAG_COMMENT = "全部评论";
//    String TAG_RELATE = "相关文章";
//    private LinearLayoutManager layoutManager;
    private boolean loginState;

    private int commentId = 0;
    private FragmentViewPagerAdapter fragmentAdapter;

//    private MediaArticleDetailWebViewItemFatory webViewItemFatory;

    public static HomeDetailGalleryActivity2 getInstance() {
        if (instance == null) {
            instance = new HomeDetailGalleryActivity2();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_article_detail_gallery);
        ButterKnife.bind(this);
        setStatusBarDarkMode();
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
    }

    private void findview() {
        rl_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_bg.setVisibility(View.GONE);
            }
        }, 4000);
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
        frame_input = new InputFragment().setMediaArticle(mediaArticle).setMaskViewProvider(maskView).setListener(this).setBackgroundBlack(null);
        transition.replace(R.id.frame_input, frame_input);
        transition.commit();

        if (fragmentAdapter == null) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            List<MediaArticle.GalleryArticle> list = mediaArticle.getContentList();
            for (int i = 0; i < list.size(); i++) {
                fragments.add(new GalleryFragment().setGallery(list.get(i)).setPage(list.size(), i).setMediaCategory(mediaArticle));
                titles.add("");
            }
            fragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        }
        viewPager.setAdapter(fragmentAdapter);

        updateData();
    }


    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {
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
        bu.putSerializable("DATA", mediaArticle.articleId);
//        bu.putSerializable("DATA2", data);
//        DataCenter.getInstance().setMediaComment(mMediaComment);
        startActivity2(GalleryCommentListActivity.class);
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
//                        adapter.setNotifyOnChange(false);
//                        int position = getfirstPosition(MediaArticle.TYPE_COMMENT);
//                        if (position == -1) {
//                            position = getfirstPosition(MediaArticle.TYPE_SAFA);
//                            isFirstComment = true;
//                            adapter.remove(adapter.getDataList().get(position));
//                            adapter.insert(mediaArticle.makeDivider(), position++);
//                            adapter.insert(mediaArticle.makeTag(TAG_COMMENT), position++);
//                        }
//                        MediaComment mc = MediaComment.load(iMsg);
////                        mc.memberHeadPic=UserCache.getUser().memberHeadPic;
////                        mc.memberNickName=UserCache.getUser().memberNickName;
////                        mc.commentContent = content;
//                        mediaArticle.articleCommentObj.add(0, mc);
//                        adapter.insert(mc, position);
//                        adapter.notifyDataSetChanged();
//                        if (isFirstComment) {
//                            loadMoreItem = new LoadMoreItemFactory(instance);
//                            adapter.setLoadMoreItem(loadMoreItem);
//                            adapter.setLoadMoreEnd(true);
//                            recyclerView.setAdapter(adapter);
//                        }
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
}
