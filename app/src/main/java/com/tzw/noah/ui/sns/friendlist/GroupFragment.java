package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
import com.tzw.noah.ui.sns.notification.NotificationListActivity;
import com.tzw.noah.utils.ViewUtils;
import com.tzw.noah.widgets.WordNaviView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/30.
 */
public class GroupFragment extends MyFragment {
    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.wordnavi)
    WordNaviView wordnavi;

    @BindView(R.id.list_view)
    ListView list_view;

    String Tag = "GroupFragment";

    Context mContext;
    List<Group> items = new ArrayList<>();

    GroupAdapter adapter;
    MyBaseActivity activity;

    boolean isFirstLoad = true;
    boolean isUpdated = true;

    static GroupFragment instance;

    public static void setUpdate()
    {
        if(instance==null)
            return;
        instance.isUpdated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sns_friendlist_friend, container, false);
        ButterKnife.bind(this, view);
        instance = this;
        wordnavi.setOnWordsChangeListener(new WordNaviView.onWordsChangeListener() {
            @Override
            public void wordsChange(String words) {
                updateListView(words);
            }
        });


        items = DataCenter.getInstance().getGroupList();
        adapter = new GroupAdapter(mContext, items);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        View spanView = inflater.inflate(R.layout.sns_span, container, false);
        list_view.addHeaderView(spanView);

        View nextView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv = (ImageView) nextView.findViewById(R.id.iv_head);
        TextView tv = (TextView) nextView.findViewById(R.id.tv_name);
        iv.setImageResource(R.drawable.sns_star);
        tv.setText("群聊推荐");
        list_view.addHeaderView(nextView);

        View nextView2 = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv2 = (ImageView) nextView2.findViewById(R.id.iv_head);
        TextView tv2 = (TextView) nextView2.findViewById(R.id.tv_name);
        iv2.setImageResource(R.drawable.sns_create_multichat);
        tv2.setText("讨论组");
        list_view.addHeaderView(nextView2);

        list_view.addHeaderView(ViewUtils.getHeadView(inflater, container, R.drawable.sns_create_group, "群消息"));

        wordnavi.setVisibility(View.GONE);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= list_view.getHeaderViewsCount()) {
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
                    activity.startActivity(GroupDetailActivity.class, bu);
                } else {
                    if (position == 0) {

                    } else if (position == 1) {
                        activity.startActivity(RecommendGroupListActivity.class);
                    } else if (position == 2) {
                        activity.startActivity(DiscussListActivity.class);
                    } else if (position == 3) {
                        activity.startActivity(NotificationListActivity.class);
                    }
                }
            }
        });

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

    private void updateListView(String words) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(activity==null)
            return;
        if(isUpdated == true)
        {
            refreshListView();
            isUpdated = false;
            return;
        }
        if (isFirstLoad == true) {
            refreshListView();
            isFirstLoad = false;
        }
    }

    private void refreshListView() {
        new SnsManager(mContext).snsGroups(new StringDialogCallback(activity) {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        items = DataCenter.getInstance().getGroupList();
                        adapter = new GroupAdapter(mContext, items);
                        list_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        activity.toast(iMsg.getMsg());
                    }
                } catch (
                        Exception e)

                {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
