package com.tzw.noah.ui.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.adapter.itemfactory.CircleBoardItemFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/29.
 */
public class BoardFragment extends Fragment implements CircleBoardItemFactory.OnItemClickListener {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    String Tag = "PostListFragment";
    Context mContext;
    List<User> items = new ArrayList<>();

    CirileMainActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;
    BoardFragment instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.circle_list_fragment, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        mPtrFrame.setPullToRefresh(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                loadMore();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
                //return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
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
        activity = (CirileMainActivity) context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
//        if(activity.firstLoad())
//        refreshListView();
//        else
//            refreshListView2();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsMyList(new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
//                        items = DataCenter.getInstance().getFriendList();
//                        items = Utils.processUser(items);
//                        Collections.sort(items, new MyCompare());
//                        adapter = new FriendAdapter(mContext, items);

                    } else {
                        activity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void refreshListView2() {
//        items = DataCenter.getInstance().getFriendList();
//        items = Utils.processUser(items);
//        Collections.sort(items, new MyCompare());
//        adapter = new FriendAdapter(mContext, items);
////        list_view.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    protected void updateData() {
        List<String> images = new ArrayList<String>();
        List<String> images2 = new ArrayList<String>();
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
//        images.add("");
        for (int i = 0; i < images.size(); i++) {
            int count = new Random().nextInt(images.size());
            if (count % 2 == 0)
                images2.add(images.get(i));
        }
        adapter = new AssemblyRecyclerAdapter(images2);
        adapter.addItemFactory(new CircleBoardItemFactory(instance));

        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        if (adapter.getItemCount() > 0)
            list_view.smoothScrollToPosition(0);
    }

    @Override
    public void onClickItem(int position, String optionsKey) {

    }
}
