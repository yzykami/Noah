package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.mine.DebugDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SampleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SampleFragment extends Fragment {

    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.tv_content)
    TextView tv_content;

    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "SampleFragment";
    private String mParam1;

    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    public SampleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static SampleFragment newInstance() {
        SampleFragment fragment = new SampleFragment();
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
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();

        return view;
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
}
