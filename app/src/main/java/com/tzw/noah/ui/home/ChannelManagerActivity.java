package com.tzw.noah.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzw.noah.MainActivity;
import com.tzw.noah.R;
import com.tzw.noah.cache.ChannelCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaCategory;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.MySwipeBackActivity;
import com.tzw.noah.widgets.DragGridView;
import com.tzw.noah.widgets.MyAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class ChannelManagerActivity extends MySwipeBackActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.drag_selected)
    DragGridView dragSelected;
    @BindView(R.id.tv11)
    TextView tv11;
    @BindView(R.id.tv21)
    TextView tv21;
    @BindView(R.id.drag_rest)
    DragGridView dragRest;


    String TAG = "ChannelManagerActivity";
    ChannelManagerActivity mContext = ChannelManagerActivity.this;
    ChannelManagerActivity instance;

    private int articleId;
    List<MediaCategory> list1 = new ArrayList<>();
    List<MediaCategory> list2 = new ArrayList<>();
    private MyAdapter dragAdapter;
    private MyAdapter dragAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_layout_channel_manager);
        ButterKnife.bind(this);
        instance = this;
        setStatusBarLightMode();
        initdata();
        findview();
//        initview();
    }


    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            articleId = bu.getInt("articleId");
        } else {

        }

        ChannelCache.getChannels(mContext, new ChannelCache.ChannelListGetter() {
            @Override
            public void onRead(List<MediaCategory> list) {
                list1 = new ArrayList<MediaCategory>();
                for (MediaCategory mc : list)
                    list1.add(mc);
                initview();
                loadData();
            }
        });
    }

    private void findview() {
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle_submit(null);
            }
        });
    }

    private void initview() {
        dragSelected.setDragModel(DragGridView.DRAG_BY_LONG_CLICK);
        dragSelected.setNumColumns(4);
        dragSelected.TAG_KEY = R.id.first;
        dragAdapter = new MyAdapter(list1, this);
        dragAdapter.setOnClickListener(new MyAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onButtonClick(int position) {
                if (list1.size() == 1) {
                    toast("至少保留一个频道");
                    return;
                }
                MediaCategory item = list1.remove(position);
                dragAdapter.notifyDataSetChanged();
                list2.add(item);
                dragAdapter2.notifyDataSetChanged();
            }
        });
        dragSelected.setAdapter(dragAdapter);
    }

    private void loadData() {
        list2 = new ArrayList<>();
        NetHelper.getInstance().mediaCategory(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        MediaCategory mediaCategory = MediaCategory.load(iMsg);
                        for (int i = 0; i < mediaCategory.children.size(); i++) {
                            boolean isSame = false;
                            MediaCategory curItem = mediaCategory.children.get(i);
                            for (MediaCategory mc : list1) {
                                if (mc.channelId == curItem.channelId)
                                    isSame = true;
                            }
                            if (!isSame)
                                list2.add(curItem);
                        }

                        dragRest = (DragGridView) findViewById(R.id.drag_rest);
                        dragRest.setNumColumns(4);
                        dragRest.setDragModel(DragGridView.DRAG_BY_LONG_CLICK);
                        dragRest.TAG_KEY = R.id.second;
                        dragAdapter2 = new MyAdapter(list2, instance);
                        dragAdapter2.setMode(2);
                        dragAdapter2.setOnClickListener(new MyAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                MediaCategory item = list2.get(position);
                                list1.add(item);
                                list2.remove(position);
                                dragAdapter2.notifyDataSetChanged();
                                dragAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onButtonClick(int position) {
                                list1.add(list2.get(position));
                                list2.remove(position);
                                dragAdapter2.notifyDataSetChanged();
                                dragAdapter.notifyDataSetChanged();
                            }
                        });
                        dragRest.setAdapter(dragAdapter2);
                    } else {
                    }
                } catch (Exception e) {
                    Log.log("ChannelCache", e);
                } finally {
                }
            }
        });
    }

    public void handle_submit(View view) {
        ChannelCache.setChannels(list1);
        setResult(100);
//        MainActivity.getInstance().ss();
        finish();
//        NetHelper.getInstance().mediaComplaint(articleId, selectDict.dictionaryId, "", new StringDialogCallback(this) {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.log(TAG, e);
//            }
//
//            @Override
//            public void onResponse(IMsg iMsg) {
//                if (iMsg.isSucceed()) {
//                    toast("您的投诉已提交,多谢您的反馈");
//                    finish();
//                } else {
//                    Log.log(TAG, iMsg.getMsg());
//                    toast(iMsg.getMsg());
//                }
//            }
//        });
    }
}
