package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.mine.DebugDetailActivity;
import com.vondear.rxtools.RxRegTool;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SystemSwitchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemSwitchFragment extends Fragment {

//    @BindView(R.id.container)
//    ViewGroup rootViewGroup;

    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_content2)
    TextView tv_content2;
    @BindView(R.id.tv_btn)
    TextView tv_btn;
    @BindView(R.id.et_server_ip)
    EditText etServerIp;
    @BindView(R.id.tv_set)
    TextView tvSet;

    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "NetToolFragment";

    private String mParam1;

    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    public SystemSwitchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static SystemSwitchFragment newInstance() {
        SystemSwitchFragment fragment = new SystemSwitchFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mine_debug_switch, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        etServerIp.setText(UserCache.getPreUrl());
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = etServerIp.getText().toString();
                if (TextUtils.isEmpty(ip)) {
                    Toast.makeText(mContext, "请输入ip", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserCache.setPreUrl(ip);
                call();
            }
        });

        call();
        return view;
    }

    private void call() {

        tv_content.setText("接口状态 : 正在测试...");
        tv_content2.setText("延迟 : ...");
        final long starttime = System.currentTimeMillis();

        NetHelper.getInstance().getBaseTime(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.toast(getResources().getString(R.string.internet_fault));
                setError();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        tv_content.setText("接口状态 : 连通");
                        long delay = System.currentTimeMillis() - starttime;
                        tv_content2.setText("延迟 : " + delay + "ms");
                    } else {
                        setError();
                        mActivity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    setError();
                    Log.log("", e);
                }
            }
        });
    }

    private void setError() {
        tv_content.setText("接口状态 : 不通");
        tv_content2.setText("延迟 : ∞");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mActivity = (DebugDetailActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
