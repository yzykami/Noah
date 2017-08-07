package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tzw.noah.R;
import com.tzw.noah.cache.DataCenter;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.WordNaviView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/30.
 */
public class FollowFragment extends MyFragment {
    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.wordnavi)
    WordNaviView wordnavi;

    @BindView(R.id.list_view)
    ListView list_view;

    String Tag = "FollowFragment";
    Context mContext;
    List<User> items = new ArrayList<>();

    FriendListActivity activity;
    FriendAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sns_friendlist_friend, container, false);
        ButterKnife.bind(this, view);
        wordnavi.setOnWordsChangeListener(new WordNaviView.onWordsChangeListener() {
            @Override
            public void wordsChange(String words) {
                updateListView(words);
            }
        });

        items = DataCenter.getInstance().getFollowList();
        items = Utils.processUser(items);
        Collections.sort(items, new MyCompare());
        adapter = new FriendAdapter(mContext, items);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        View headSearchView = inflater.inflate(R.layout.sns_search_head, container, false);
//        list_view.addHeaderView(headSearchView);
//        View nextView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
//        ImageView iv=(ImageView)nextView.findViewById(R.id.iv_head);
//        TextView tv_name=(TextView)nextView.findViewById(R.id.tv_name);
//        iv.setImageResource(R.drawable.sns_star);
//        tv_name.setText("好友推荐");
//        list_view.addHeaderView(nextView);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= list_view.getHeaderViewsCount()) {
                    Bundle bu = new Bundle();
                    bu.putSerializable("DATA", items.get(position - list_view.getHeaderViewsCount()));
                    activity.startActivity(PersonalActivity.class, bu);
                } else {
                    if (position == 0) {

                    } else if (position == 1) {

                    } else if (position == 2) {
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
        activity = (FriendListActivity)context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void updateListView(String words) {
        if(words=="") {
            list_view.setSelection(0);
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String ping = items.get(i).nameFirstChar;
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(ping)) {
                //将列表选中哪一个
                list_view.setSelection(i+list_view.getHeaderViewsCount());
                //找到开头的一个即可
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(activity.firstLoad())
        refreshListView();
        else
            refreshListView2();
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
                        items = DataCenter.getInstance().getFollowList();
                        items = Utils.processUser(items);
                        Collections.sort(items, new MyCompare());
                        adapter = new FriendAdapter(mContext, items);
                        list_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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
        items = DataCenter.getInstance().getFollowList();
        items = Utils.processUser(items);
        Collections.sort(items, new MyCompare());
        adapter = new FriendAdapter(mContext, items);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
