package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.ui.mine.DebugDetailActivity;
import com.tzw.noah.utils.DeviceUuidFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SystemParamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemParamsFragment extends Fragment {

//    @BindView(R.id.container)
//    ViewGroup rootViewGroup;


    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_set_timeout)
    TextView tvSetTimeout;
    @BindView(R.id.tv_offset)
    TextView tvOffset;
    @BindView(R.id.tv_deviceid)
    TextView tvDeviceid;
    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.tv_token)
    TextView tvToken;
    @BindView(R.id.tv_db_version)
    TextView tvDbVersion;
    @BindView(R.id.et_timeout)
    EditText etTimeout;


    private String mParam1;
    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "SystemParamsFragment";

    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    public SystemParamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static SystemParamsFragment newInstance() {
        SystemParamsFragment fragment = new SystemParamsFragment();
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
        View view = inflater.inflate(R.layout.mine_debug_systemparams, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();
        initview();
        return view;
    }

    private void initview() {
        etTimeout.setText("" + UserCache.getTimeOut());
        tvOffset.setText("时间偏移量 : " + UserCache.getTimeOffset());
        tvDeviceid.setText("设备编码 : " + new DeviceUuidFactory(mContext).getDeviceUuidString());
        tvKey.setText("登录Key : " + UserCache.getLoginKey());
        tvToken.setText("登录Token : " + UserCache.getToken());
        tvDbVersion.setText("数据库版本信息 : 待完善");

        tvSetTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = etTimeout.getText().toString();
                int timeout = -1;
                try {
                    timeout = Integer.parseInt(time);
                } catch (Exception e) {
                    return;
                }
                if (timeout <= 0) {
                    mActivity.toast("超时时间必须大于0");
                    return;
                }
                UserCache.setTimeOut(timeout);
                mActivity.toast("设置成功");
                mActivity.showKeyboard(false);
            }
        });
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
