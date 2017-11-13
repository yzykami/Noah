package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.content.Intent;
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
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.sns.discuss.DiscussCreateActivity;
import com.tzw.noah.ui.sns.friendlist.MyCompare;
import com.tzw.noah.ui.sns.group.GroupCreateActivity;
import com.tzw.noah.ui.sns.search.GroupSearchActivity;
import com.tzw.noah.ui.sns.search.UserSearchActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.WordNaviView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.tzw.noah.R.id.container;

/**
 * Created by yzy on 2017/6/29.
 */
public class AddGroupFragment extends Fragment {
    @BindView(container)
    ViewGroup rootViewGroup;

    @BindView(R.id.wordnavi)
    WordNaviView wordnavi;

    @BindView(R.id.list_view)
    ListView list_view;

    Context mContext;
    List<Group> items = new ArrayList<>();
    String Tag = "AddGroupFragment";
    AddActivity mActivity;

    AddGroupAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sns_friendlist_friend, container, false);
        mActivity = (AddActivity) getActivity();
        ButterKnife.bind(this, view);
        wordnavi.setOnWordsChangeListener(new WordNaviView.onWordsChangeListener() {
            @Override
            public void wordsChange(String words) {
                updateListView(words);
            }
        });

        items = new ArrayList<>();

        adapter = new AddGroupAdapter(mContext, items);

        list_view.setAdapter(adapter);

        View headSearchView = inflater.inflate(R.layout.sns_search_head, container, false);
        TextView tv = (TextView) headSearchView.findViewById(R.id.tv);
        tv.setText("简介/群名称");
        list_view.addHeaderView(headSearchView);
//        View spanView = inflater.inflate(R.layout.sns_span, container, false);
//        list_view.addHeaderView(spanView);

        list_view.addHeaderView(getHeadView(inflater, container, R.drawable.sns_group, "创建群聊"));
        list_view.addHeaderView(getHeadView(inflater, container, R.drawable.sns_create_multichat, "创建多人会话"));

        View headSpanView = inflater.inflate(R.layout.sns_span, container, false);
        TextView tag = (TextView) headSpanView.findViewById(R.id.tag);
        tag.setText("系统推荐");
        list_view.addHeaderView(headSpanView);

        wordnavi.setVisibility(View.GONE);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= list_view.getHeaderViewsCount()) {

                } else {
                    if(position==0)
                    {
                        mActivity.startActivity(GroupSearchActivity.class);
                        mActivity.overridePendingTransition(R.anim.window_push_enter, R.anim.window_push_exit);
                    }
                    if (position == 1) {
                        mActivity.startActivityForResult(100, GroupCreateActivity.class);
                    }
                    if (position == 2) {
                        mActivity.startActivityForResult(100, DiscussCreateActivity.class);
                    }
                }
            }
        });
        return view;
    }

    private View getHeadView(LayoutInflater inflater, ViewGroup container, int drawableId, String title) {
        View headView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
        ImageView iv = (ImageView) headView.findViewById(R.id.iv_head);
        TextView tv = (TextView) headView.findViewById(R.id.tv_name);
        iv.setImageResource(drawableId);
        tv.setText(title);
        return headView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void updateListView(String words) {
        if (words == "") {
            list_view.setSelection(0);
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String ping = "";//items.get(i).nameFirstChar;
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(ping)) {
                //将列表选中哪一个
                list_view.setSelection(i + list_view.getHeaderViewsCount());
                //找到开头的一个即可
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        new SnsManager(mContext).snsRecommendGroup(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            items = (List<Group>) iMsg.Data;
                        else
                            items = Group.loadRecommendList(iMsg);
//                        items = Utils.processUser(items);
//                        Collections.sort(items, new MyCompare());
                        if (items == null)
                            items = new ArrayList<Group>();
                        adapter = new AddGroupAdapter(mContext, items);
                        list_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        mActivity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
