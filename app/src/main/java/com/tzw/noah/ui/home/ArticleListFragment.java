package com.tzw.noah.ui.home;

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
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.CircleListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.MemberListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListGalleryItemFatory;
import com.tzw.noah.ui.adapter.itemfactory.medialist.MediaListListener;
import com.tzw.noah.ui.webview.WebViewActivity;
import com.tzw.noah.widgets.scrollablelayout.ScrollableHelper;

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
 * Created by yzy on 2017/8/30.
 */
public class ArticleListFragment extends Fragment implements MediaListListener, OnRecyclerLoadMoreListener, ScrollableHelper.ScrollableContainer {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    String Tag = "ArticleListFragment";
    Context mContext;

    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;
    LoadMoreItemFactory loadMoreItem;
    ArticleListFragment instance;

    MediaCategory mMediaCategory;
    boolean isFirstLoad = true;
    private int articleId = 0;

    List<MediaArticle> items;

    public ArticleListFragment setMediaCategory(MediaCategory mMediaCategory) {
        this.mMediaCategory = mMediaCategory;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.circle_list_fragment, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                loadMore();
                articleId = 0;
                refreshListView();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                if (mode == CIRCLEDETAIL)
//                    return false;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
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
        activity = (MyBaseActivity) context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
            refreshListView();
        } else
            refreshListView2();
    }

    private void refreshListView() {
        int channelId = mMediaCategory.channelId;

        NetHelper.getInstance().mediaArticleList(channelId, articleId, DataCenter.pagesize, new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        items = MediaArticle.load(iMsg);
                        if (items == null)
                            items = new ArrayList<MediaArticle>();
                        if (articleId == 0)
                            adapter.clear();
                        adapter.addAll(items);
                        articleId = items.get(items.size() - 1).articleId;
                        adapter.loadMoreFinished(items.size() < DataCenter.pagesize);
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
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646800199&di=e588dafd6e16678d08e8404c7f6a5651&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_2_1979295486D2113125476_23.jpg");
        images.add("http://v1.qzone.cc/avatar/201405/10/17/00/536deaa6c35a9512.jpg!200x200.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646872650&di=8c968f968b9423051048d1eec7c5d598&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_3520253239D3803949043_21.jpg");
        images.add("http://img17.3lian.com/d/file/201702/22/1005a2e0825ffe290b3f697404ee8038.jpg");
        images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=c89f6064a21ea8d38e227306a70b30cf/0824ab18972bd407ce9f04227f899e510eb30991.jpg");
        images.add("http://www.adquan.com/upload/20151223/1450838259813154.jpg");
        images.add("http://www.feizl.com/upload2007/2015_07/150720124522248.jpg");
//        images.add("http://pic.iqshw.com/d/file/gexingqqziyuan/touxiang/2016/03/17/8581e320e98e541ed03a8fcab51068fd.jpg");
//        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222436.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646757823&di=ceb2ef896125f0f5ead9140c5e68cef7&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-3%2F2016030111061053440.jpg");
//        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222419.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358314210&di=c13978e7c2007292e70c63e441dfb3d4&imgtype=0&src=http%3A%2F%2Fpic67.nipic.com%2Ffile%2F20150514%2F21036787_181947848862_2.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358314208&di=35d0bbde5e6102ce3dbda268ec17c7e4&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F09%2F64%2F39i58PICmgE.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358357689&di=1bc5e30cd999cee0b0ca4fba4e7dc966&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2Ff1d7bdf9d72a6059e78e5fcf2834349b013bbaa6.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358428996&di=cdda43084cf31ca96448dd504fb58624&imgtype=0&src=http%3A%2F%2Fbizhi.zhuoku.com%2F2013%2F05%2F23%2Fxiaoqingxin%2Fxiaoqingxin021.jpg");
//        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1226504803,606513985&fm=27&gp=0.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=4b3dbebc9b004df6361129ff7b66ca85&src=http://cdn.duitang.com/uploads/item/201404/07/20140407205935_RfBQQ.jpeg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=28c19605a014fa6f0b0541da82156bd4&src=http://dl.bizhi.sogou.com/images/2012/03/16/43517.jpg");
//        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3308758682,671689542&fm=27&gp=0.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1502348364&di=0d71b4ac734645e7ad8d2d0ab9948b6c&src=http://pic1.win4000.com/wallpaper/b/5488fa61b7d6a.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358595922&di=190cf56ca643a7507664afa4c317627f&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201201%2F25%2F20120125215809_wWLBV.thumb.600_0.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358595921&di=dd7d1b87850c0601e8a3d502a805dfcd&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201412%2F04%2F20141204205535_GWBCH.thumb.700_0.jpeg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358646705&di=ab83c91ebd58d2e171ab59ad1446d7ec&imgtype=jpg&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F6159252dd42a28348dcd92c952b5c9ea14cebf8d.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358655703&di=e1962f210d72420015387a68a64be6ea&imgtype=jpg&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9825bc315c6034a83986a140c21349540823768e.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358646527&di=7085916b2b8f8c6c9cafda79d69a9c91&imgtype=0&src=http%3A%2F%2Fimg.article.pchome.net%2F00%2F28%2F93%2F86%2Fpic_lib%2Fwm%2Feurope038.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358712323&di=d23256855a12f14a848222abc98e2137&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Ff%2F52665cb5a0b5d.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358792608&di=a7ca66ae5bd2ef326c4bb539635f0c14&imgtype=0&src=http%3A%2F%2Fimg22.mtime.cn%2Fup%2F2010%2F07%2F14%2F093346.94291758_o.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358829280&di=50e016857977dc6b7024948bc3218ec5&imgtype=0&src=http%3A%2F%2Fpic23.nipic.com%2F20120809%2F10436117_161557283110_2.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502358843244&di=a1bce78535b6683f3670b286c0aff9a1&imgtype=0&src=http%3A%2F%2Fpic12.nipic.com%2F20110103%2F5089137_130401034157_2.jpg");

//        images.add("");

        items = new ArrayList<>();
        adapter = new AssemblyRecyclerAdapter(items);
        adapter.addItemFactory(new MediaListGalleryItemFatory(instance));
        loadMoreItem = new LoadMoreItemFactory(instance);
        adapter.setLoadMoreItem(loadMoreItem);

        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        if (adapter.getItemCount() > 0)
            list_view.smoothScrollToPosition(0);
    }

    private void loadMore() {
//        frameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.refreshComplete();
//            }
//        }, 500);
    }

//    @Override
//    public void onClickImage(int position, String optionsKey) {
//        activity.startActivity2(WebViewActivity.class);
//    }

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter assemblyRecyclerAdapter) {
        refreshListView();
    }

    @Override
    public View getScrollableView() {
        return list_view;
    }

    @Override
    public void onItemClick(int position, Object o) {
//        activity.toast(mMediaCategory.channelName + " " + mMediaCategory.channelId);
//        activity.startActivity2(WebViewActivity.class);
        MediaArticle mediaArticle = (MediaArticle) o;
        NetHelper.getInstance().mediaArticleDetails(mediaArticle.articleId, new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    mPtrFrame.refreshComplete();
                    if (iMsg.isSucceed()) {
                        String s = iMsg.toString();
                    } else {
                        activity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            refreshListView2();
    }
}
