package com.tzw.noah.ui.sns.group;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.ChatListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.MemberListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.SearchHeadFactory;
import com.tzw.noah.ui.sns.add.AddActivity;
import com.tzw.noah.ui.sns.chat.ChatActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

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
 * Created by yzy on 2017/7/4.
 */

public class GroupMemberListActivity extends MyBaseActivity implements MemberListItemFactory.OnImageClickListener, OnRecyclerLoadMoreListener, SearchHeadFactory.OnItemClickListener {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;

    @BindView(R.id.list_view)
    RecyclerView recyclerView;

    Context mContext = GroupMemberListActivity.this;
    Activity mActivity;
    private AssemblyRecyclerAdapter adapter;

    String Tag = "GroupMemberListActivity";
    private Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_memberlist);
        ButterKnife.bind(this);
        mActivity = this;
        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
    }

    private void findview() {

    }

    private void initview() {
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
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

        GridLayoutManager gm = new GridLayoutManager(mContext, 5);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return 5;
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
            adapter = new AssemblyRecyclerAdapter(new ArrayList<GroupMember>());
            adapter.addItemFactory(new MemberListItemFactory(this));
            adapter.addHeaderItem(new SearchHeadFactory(this), "");
            recyclerView.setAdapter(adapter);
            count = 0;
//        loadMoreItem = new LoadMoreItemFactory(this);
//        adapter.setLoadMoreItem(loadMoreItem);
//            refreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    onRefresh();
//                }
//            });
        }

        updateData();
    }

    LoadMoreItemFactory loadMoreItem;

    protected void updateData() {

        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        List<GroupMember> items;
                        if (iMsg.Data != null)
                            items = (List<GroupMember>) iMsg.Data;
                        else
                            items = GroupMember.loadList(iMsg);
                        if (items == null)
                            items = new ArrayList<GroupMember>();

                        GroupMember gm = new GroupMember();
                        gm.memberNo = -1;
                        gm.memberHeadUrl = "drawable://" + R.drawable.sns_add_person;
                        items.add(gm);
                        if (group.myMemberType == Group.MemberType.MANAGER || group.myMemberType == Group.MemberType.OWNER) {
                            GroupMember gm2 = new GroupMember();
                            gm2.memberNo = -2;
                            gm2.memberHeadUrl = "drawable://" + R.drawable.sns_delete_person;
                            items.add(gm2);
                        }

                        adapter.addAll(items);

                        mPtrFrame.refreshComplete();

                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });


    }

    private void doWorking() {
    }

    @Override
    public void onClickImage(int position, GroupMember gm) {
        if (gm.memberNo == -1) {
            Bundle bu = new Bundle();
            bu.putSerializable("DATA", (ArrayList) adapter.getDataList());
            bu.putSerializable("DATA2", group);
            startActivity(GroupAddMemberActivity.class, bu);
        } else if (gm.memberNo == -2) {
            if (group.myMemberType == Group.MemberType.MANAGER || group.myMemberType == Group.MemberType.OWNER) {
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", (ArrayList) adapter.getDataList());
                bu.putSerializable("DATA2", group);
                startActivity(GroupRemoveMemberActivity.class, bu);
            }
        } else {
            Bundle bu = new Bundle();
            User user = new User();
            user.memberNo = gm.memberNo;
            user.memberNickName = gm.getMemberName();
            user.memberHeadPic = gm.memberHeadUrl;
            bu.putSerializable("DATA", user);
            startActivity(PersonalActivity.class, bu);
        }
    }

    @Override
    public void onSearchClick(int position, Object optionsKey) {
        toast("position = " + position);
    }

    int count = 0;

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> images = new ArrayList<String>();

                images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646800199&di=e588dafd6e16678d08e8404c7f6a5651&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_2_1979295486D2113125476_23.jpg");
                images.add("http://v1.qzone.cc/avatar/201405/10/17/00/536deaa6c35a9512.jpg!200x200.jpg");
                images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646872650&di=8c968f968b9423051048d1eec7c5d598&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_3520253239D3803949043_21.jpg");
                images.add("http://img17.3lian.com/d/file/201702/22/1005a2e0825ffe290b3f697404ee8038.jpg");
                images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=c89f6064a21ea8d38e227306a70b30cf/0824ab18972bd407ce9f04227f899e510eb30991.jpg");
                images.add("http://www.adquan.com/upload/20151223/1450838259813154.jpg");
                images.add("http://www.feizl.com/upload2007/2015_07/150720124522248.jpg");
                images.add("http://pic.iqshw.com/d/file/gexingqqziyuan/touxiang/2016/03/17/8581e320e98e541ed03a8fcab51068fd.jpg");
                images.add("http://www.feizl.com/upload2007/2015_07/1507201245222436.jpg");
                images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646757823&di=ceb2ef896125f0f5ead9140c5e68cef7&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-3%2F2016030111061053440.jpg");
                images.add("http://www.feizl.com/upload2007/2015_07/1507201245222419.jpg");
                adapter.addAll(images);
                adapter.loadMoreFinished(false);
                if (count++ >= 5)
                    adapter.setLoadMoreEnd(true);
                mPtrFrame.refreshComplete();
            }
        }, 500);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    public void handle_add(View view) {
        startActivity(AddActivity.class);
    }
}
