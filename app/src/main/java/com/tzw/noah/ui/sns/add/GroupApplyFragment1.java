package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tzw.noah.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tzw.noah.R.id.container;

/**
 * Created by yzy on 2017/7/5.
 */
public class GroupApplyFragment1 extends Fragment {
    @BindView(R.id.tv_submit)
    TextView tv_submit;


    Context mContext;

    GroupApplyActivity groupApplyActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sns_fragment_group_apply1, container, false);
        ButterKnife.bind(this, view);
        groupApplyActivity = (GroupApplyActivity) getActivity();
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupApplyActivity.showFragment(1);
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
        mContext = context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
