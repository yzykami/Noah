package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tzw.noah.R;
import com.tzw.noah.ui.mine.DebugActivity;
import com.tzw.noah.ui.mine.StringAdapter;
import com.tzw.noah.utils.CrashHandler;
import com.tzw.noah.widgets.WordNaviView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrashListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrashListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrashListFragment extends Fragment {

    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.list_view)
    ListView list_view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<File> fileitems;
    List<String> items;
    StringAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public CrashListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrashListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrashListFragment newInstance(String param1, String param2) {
        CrashListFragment fragment = new CrashListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        fileitems = getCrashFileList();
        items = getCrashFileNameList();
        adapter = new StringAdapter(mContext, items);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DebugActivity.startDetailActivity(mContext, items.get(position), DebugActivity.TYPE_CRASH_DETAIL_INDEX, fileitems.get(position));
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public List<File> getCrashFileList() {
        List<File> crashFileList = new ArrayList<>();

        File f = new File(CrashHandler.getInstance().getGlobalpath());
        File[] files = f.listFiles();// 列出所有文件
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            if (ff.isFile()) {
                crashFileList.add(ff);
            }
        }
        return crashFileList;
    }

    public List<String> getCrashFileNameList() {
        List<String> crashFileNameList = new ArrayList<>();

        File f = new File(CrashHandler.getInstance().getGlobalpath());
        File[] files = f.listFiles();// 列出所有文件
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            if (ff.isFile()) {
                crashFileNameList.add(ff.getName());
            }
        }
        return crashFileNameList;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
