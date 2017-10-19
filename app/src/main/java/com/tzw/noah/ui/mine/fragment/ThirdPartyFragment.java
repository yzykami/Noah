package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link ThirdPartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdPartyFragment extends Fragment {


    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_token)
    TextView tvToken;

    private String mParam1;
    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "ThirdPartyFragment";

    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    public ThirdPartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static ThirdPartyFragment newInstance() {
        ThirdPartyFragment fragment = new ThirdPartyFragment();
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
        View view = inflater.inflate(R.layout.mine_debug_thirdparty, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();
        initview();
        return view;
    }

    private void initview() {

        String id = "未登录";
        String token = "未登录";
        if (UserCache.isLogin()) {
            id = UserCache.getUser().netEaseId + "";
            token = UserCache.getUser().netEaseToken;
        }
        tvId.setText("云信ID : " + id);
        tvToken.setText("云信Token : " + token);

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
