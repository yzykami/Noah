package com.tzw.noah.ui.sns.friendlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.WordNaviView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yzy on 2017/6/30.
 */
public class FollowFragment extends Fragment {
    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.wordnavi)
    WordNaviView wordnavi;

    @BindView(R.id.list_view)
    ListView list_view;

    Context mContext;
    List<SnsPerson> items = new ArrayList<>();

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
        List<String> namelist = new ArrayList<>();
        namelist.add("你111");
        namelist.add("你好在");
        namelist.add("耐111");
        namelist.add("废柴");
        namelist.add("风");
        namelist.add("银");
        namelist.add("辛巴");
        namelist.add("2342辛巴");
        namelist.add("啦啦");
        namelist.add("❤啦啦");
        namelist.add("OMG呵呵");
        namelist.add("ddd呵呵");

        List<SnsPerson> normalItems=new ArrayList<>();
        for (String name :namelist)
        {
            SnsPerson p=new SnsPerson();
            p.name=name;
            p.namePingyin= Utils.getLetter(name);
            p.shortCut=Utils.getLetterShortCut(p.namePingyin);
            normalItems.add(p);
        }
        Collections.sort(normalItems, new MyCompare());

        List<SnsPerson> starItems=new ArrayList<>();
        List<String> namelist2 = new ArrayList<>();
        namelist2.add("偏差工作");
        namelist2.add("完善相关");
        for (String name :namelist2)
        {
            SnsPerson p=new SnsPerson();
            p.name=name;
            p.namePingyin= Utils.getLetter(name);
            p.shortCut=Utils.getLetterShortCut(p.namePingyin);
            starItems.add(p);
        }
        Collections.sort(starItems, new MyCompare());
        for(SnsPerson person:starItems)
        {
            person.shortCut="星标关注者";
        }
        items=new ArrayList<>();
        items.addAll(starItems);
        items.addAll(normalItems);
        FriendAdapter adapter = new FriendAdapter(mContext, items);

        list_view.setAdapter(adapter);

//        View headSearchView = inflater.inflate(R.layout.sns_search_head, container, false);
//        list_view.addHeaderView(headSearchView);
//        View nextView = inflater.inflate(R.layout.sns_next_operation_item, container, false);
//        ImageView iv=(ImageView)nextView.findViewById(R.id.iv_head);
//        TextView tv_name=(TextView)nextView.findViewById(R.id.tv_name);
//        iv.setImageResource(R.drawable.sns_star);
//        tv_name.setText("好友推荐");
//        list_view.addHeaderView(nextView);

        return view;
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
        if(words=="") {
            list_view.setSelection(0);
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String ping = items.get(i).shortCut;
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(ping)) {
                //将列表选中哪一个
                list_view.setSelection(i+list_view.getHeaderViewsCount());
                //找到开头的一个即可
                return;
            }
        }
    }
}
