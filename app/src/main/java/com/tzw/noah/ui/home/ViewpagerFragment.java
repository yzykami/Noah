package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.webview.WebViewActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.s
 * Use the {@link ViewpagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewpagerFragment extends Fragment {

    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.imageView)
    SampleImageView imageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    List<File> fileitems;
    List<String> items;
    private Context mContext;
    MyBaseActivity mActivity;
    Object object;

    MediaArticle mMediaArticle;

    public ViewpagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CrashListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewpagerFragment newInstance(MediaArticle ma) {
        ViewpagerFragment fragment = new ViewpagerFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.mMediaArticle = ma;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_top, container, false);
        ButterKnife.bind(this, view);

        tv_content.setText(mMediaArticle.articleTitle);
        imageView.displayRoundImageBigThumb(mMediaArticle.articleImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaArticle.getContentString().isEmpty())
                    return;
                MediaArticle ma = new MediaArticle();
                Bundle bu = new Bundle();
                bu.putSerializable("DATA", ma);
                bu.putString("title", "广告");
                ma.articleContent = mMediaArticle.getContentString();
                ((MyBaseActivity)getActivity()).startActivity2(WebViewActivity.class, bu);
            }
        });
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mActivity = (MyBaseActivity) context;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (tv_content != null)
//                tv_content.setText(mMediaArticle.articleImage);
//            imageView.displayImage(mMediaArticle.articleImage);
        }
    }
}
