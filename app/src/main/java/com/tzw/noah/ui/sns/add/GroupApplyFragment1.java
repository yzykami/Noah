package com.tzw.noah.ui.sns.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.tzw.noah.R.id.container;

/**
 * Created by yzy on 2017/7/5.
 */
public class GroupApplyFragment1 extends Fragment {
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.et_msg)
    EditText et_msg;

    String Tag = "GroupApplyFragment1";
    Context mContext;

    GroupApplyActivity groupApplyActivity;
    int maxCount = 150;

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
                doApply();
            }
        });
        tv_count.setText(maxCount + "");
        et_msg.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = et_msg.getSelectionStart();
                selectionEnd = et_msg.getSelectionEnd();
                if (temp.length() > maxCount) {


                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_msg.setText(s);
                    et_msg.setSelection(tempSelection);
                }
                tv_count.setText((maxCount - temp.length()) + "");
            }
        });
        return view;
    }

    private void doApply() {
        String msg = et_msg.getText().toString();
        if (et_msg.getText().toString().isEmpty()) {
            groupApplyActivity.toast("请输入你的加群理由");
            return;
        }
        new SnsManager(mContext).snsApplyToGroup(groupApplyActivity.group.groupId, msg, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                groupApplyActivity.toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        groupApplyActivity.showFragment(1);
                    } else {
                        groupApplyActivity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
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
