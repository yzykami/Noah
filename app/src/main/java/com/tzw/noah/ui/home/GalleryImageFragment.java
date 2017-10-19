package com.tzw.noah.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.tzw.noah.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GalleryImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryImageFragment extends Fragment {

//    @BindView(R.id.container)
//    ViewGroup rootViewGroup;

    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "NetToolFragment";

    @BindView(R.id.image_imageFragment_image)
    SampleImageView imageImageFragmentImage;
    @BindView(R.id.container)
    ScrollView container;
//    @BindView(R.id.framelayout)
//    FrameLayout framelayout;
//    @BindView(R.id.hint_imageFragment_hint)
//    HintView hintImageFragmentHint;
    private String mParam1;

    private Context mContext;
    Object object;
    private ImageClickListener imageClickListener;

    public void setImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public interface ImageClickListener {
        void onImageClick();
    }

    public GalleryImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static GalleryImageFragment newInstance(String param1) {
        GalleryImageFragment fragment = new GalleryImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image2, container, false);
        ButterKnife.bind(this, view);
//        int width = Utils.getSrceenWidth();
//        int height = Utils.getSrceenHeight();


//        ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(width, height);
//        this.container.setLayoutParams(lp1);
//
//        ScrollView.LayoutParams lp = (ScrollView.LayoutParams) framelayout.getLayoutParams();
//        lp.height = 1920;
//        lp.width = 1080;
//        framelayout.setLayoutParams(lp);
//        container.measure(0,0);
//        imageImageFragmentImage.setLayoutParams(lp);
        imageImageFragmentImage.getOptions().setLoadingImage(R.drawable.logo_gray_fatter);
        imageImageFragmentImage.setScaleType(ImageView.ScaleType.CENTER);
        imageImageFragmentImage.displayImage(mParam1);
        imageImageFragmentImage.setZoomEnabled(true);
        imageImageFragmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageClickListener != null) {
                    imageClickListener.onImageClick();
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
