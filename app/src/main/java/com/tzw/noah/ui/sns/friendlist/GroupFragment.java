package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
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


        items = new ArrayList<>();
//

//        Collections.sort(items, new MyCompare());

        adapter = new GroupAdapter(mContext, items);

        list_view.setAdapter(adapter);

        View headSearchView = inflater.inflate(R.layout.sns_span, container, false);
        list_view.addHeaderView(headSearchView);
        View nextView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv = (ImageView) nextView.findViewById(R.id.iv_head);
        TextView tv = (TextView) nextView.findViewById(R.id.tv_name);
        iv.setImageResource(R.drawable.sns_star);
        tv.setText("群组推荐");
        list_view.addHeaderView(nextView);

        View nextView2 = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv2 = (ImageView) nextView2.findViewById(R.id.iv_head);
        TextView tv2 = (TextView) nextView2.findViewById(R.id.tv_name);
        iv2.setImageResource(R.drawable.sns_create_multichat);
        tv2.setText("讨论组");
        list_view.addHeaderView(nextView2);

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
                        //activity.startActivity(BlackListActivity.class);
                    } else if (position == 2) {
                        activity.startActivity(DiscussListActivity.class);
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
        refreshListView();
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
//                        if (iMsg.Data != null)
//                            items = (List<Group>) iMsg.Data;
//                        else {
//                            List<Group> list1 = Group.loadDiscussList(iMsg);
//                            List<Group> list2 = Group.loadGroupList(iMsg);
//                            items = new ArrayList<Group>();
//                            if (list1 != null && list1.size() > 0)
//                                items.addAll(list1);
//                            if (list2 != null && list2.size() > 0)
//                                items.addAll(list2);
                        items = Group.loadGroupList(iMsg);
//                    }
//                        items = Utils.processUser(items);
//                        Collections.sort(items, new MyCompare());
                        if (items == null || items.size() == 0)
                            items = new ArrayList<Group>();
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
