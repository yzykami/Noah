package com.tzw.noah.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.models.MediaLike;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MySwipeBackActivity;
import com.tzw.noah.ui.adapter.itemfactory.media.LikeListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailDividerItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTagItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.mediaitem.MediaArticleDetailTitleItemFatory;
import com.tzw.noah.ui.sns.add.AddActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

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

/**
 * Created by yzy on 2017/7/4.
 */

public class LikeListActivity extends MySwipeBackActivity implements LikeListItemFactory.OnImageClickListener, OnRecyclerLoadMoreListener {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;

    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.divider)
    View divider;

    Context mContext = LikeListActivity.this;
    Activity mActivity;
    private AssemblyRecyclerAdapter adapter;

    String Tag = "GroupMemberListActivity";
    private Group group;

    int pagesize = 50;
    List<Object> items;
    private int articleId;
    private String articleTitle;
    private String createTime;
    private String author;
    private int likeId;
    private MediaArticle mediaArticle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_layout_likelist);
        ButterKnife.bind(this);
        mActivity = this;
        setStatusBarLightMode();
        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            articleId = bu.getInt("articleId");
            articleTitle = bu.getString("articleTitle");
            createTime = bu.getString("createTime");
            author = bu.getString("author");
            mediaArticle = (MediaArticle) bu.getSerializable("DATA");
        } else {

        }
    }
    int totaly = 0;
    private void findview() {
        divider.setVisibility(View.GONE);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totaly += dy;
                if (totaly > 0)
                    divider.setVisibility(View.VISIBLE);
                else
                    divider.setVisibility(View.GONE);
            }
        });

        TextView tv_article_title = (TextView) findViewById(R.id.tv_article_title);
        TextView tv_article_author = (TextView) findViewById(R.id.tv_article_author);

        tv_article_title.setText(articleTitle);
        tv_article_author.setText(Utils.getStandardDate(createTime) + "  " + author);

        tv_article_title.setVisibility(View.GONE);
        tv_article_author.setVisibility(View.GONE);
    }

    private void initview() {
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateData();
//            }
//        }, 500);
        // updateData();

        final int spancount = 5;
        final GridLayoutManager gm = new GridLayoutManager(mContext, spancount);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == adapter.getDataCount() || position <=2)
                    return spancount;
                else
                    return 1;
            }
        });
        recyclerView.setLayoutManager(gm);
//        int padding = Utils.dp2px(mContext, 20);
//        recyclerView.setPadding(padding, padding, padding, padding);
//        recyclerView.setClipToPadding(false);
//
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new AssemblyRecyclerAdapter(new ArrayList<MediaLike>());
            adapter.addItemFactory(new LikeListItemFactory(this));
            adapter.addItemFactory(new MediaArticleDetailTagItemFatory(null));
            adapter.addItemFactory(new MediaArticleDetailTitleItemFatory(null));
            adapter.addItemFactory(new MediaArticleDetailDividerItemFatory(null));
//            adapter.addHeaderItem(new SearchHeadFactory(this), "");
            recyclerView.setAdapter(adapter);
            count = 0;
            loadMoreItem = new LoadMoreItemFactory(this);
            adapter.setLoadMoreItem(loadMoreItem);
//            if (adapter.getDataList().size() < pagesize) {
//                adapter.setLoadMoreEnd(true);
//            }
        }

        items = new ArrayList<Object>();
//        mMediaComment.isCommentDetail = true;
//        items.add(mMediaComment);
        items.add(mediaArticle.makeTitle());
        items.add(mediaArticle.makeDivider());
        items.add(new MediaArticle().makeTag("全部点赞"));
        updateData();
    }

    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {
        if (articleId == 0)
            return;

        NetHelper.getInstance().mediaLikeList(articleId, likeId, pagesize, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        List<MediaLike> list = MediaLike.loadList(iMsg);
//                        items = new ArrayList<>();
                        if (list == null) {
                            list = new ArrayList<MediaLike>();
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                items.add(list.get(i));
                            }
                        }
//                        if (likeId == 0) {
//                            adapter.clear();
//                            adapter.addAll(new MediaArticle().makeTag("全部点赞"));
//                        }
                        adapter.addAll(items);
                        if (items.size() >= 1)
                            likeId = list.get(list.size() - 1).articleEvaluateId;
                        adapter.loadMoreFinished(list.size() < pagesize);
                        adapter.notifyDataSetChanged();

                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });

//        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                toast(getResources().getString(R.string.internet_fault));
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                try {
//                    if (iMsg.isSucceed()) {
//
//                        List<GroupMember> items;
//                        items = DataCenter.getInstance().getGroupMemberList();
//                        group.memberCount = items.size();
//                        GroupMember gm = new GroupMember();
//                        gm.memberNo = -1;
//                        gm.memberHeadPic = "drawable://" + R.drawable.sns_add_person;
//                        items.add(gm);
//                        if (group.myMemberType == Group.MemberType.MANAGER || group.myMemberType == Group.MemberType.OWNER) {
//                            GroupMember gm2 = new GroupMember();
//                            gm2.memberNo = -2;
//                            gm2.memberHeadPic = "drawable://" + R.drawable.sns_delete_person;
//                            items.add(gm2);
//                        }
//
//                        adapter.setDataList(items);
//
//                        mPtrFrame.refreshComplete();
//
//                    } else {
//                        toast(iMsg.getMsg());
//                    }
//                } catch (Exception e) {
//                    Log.log(Tag, e);
//                }
//            }
//        });


    }

    private void doWorking() {
    }

    @Override
    public void onClickImage(int position, MediaLike gm) {
        Bundle bu = new Bundle();
        User user = new User();
        user.memberNo = gm.memberNo;
        user.memberNickName = gm.memberNickName;
        user.memberHeadPic = gm.memberHeadPic;
        bu.putSerializable("DATA", user);
        startActivity2(PersonalActivity.class, bu);
    }

    int count = 0;

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<String> images = new ArrayList<String>();
//                adapter.addAll(items);
//                adapter.loadMoreFinished(false);
//                if (count++ >= 6)
//                    adapter.setLoadMoreEnd(true);
//                mPtrFrame.refreshComplete();
//            }
//        }, 500);
        updateData();
    }


    public void handle_add(View view) {
        startActivity(AddActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateData();
    }
}
