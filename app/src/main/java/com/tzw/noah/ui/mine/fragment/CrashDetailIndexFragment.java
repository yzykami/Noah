package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.ui.mine.DebugActivity;
import com.tzw.noah.ui.mine.DebugDetailActivity;
import com.tzw.noah.ui.mine.StringAdapter;
import com.tzw.noah.utils.CrashHandler;
import com.tzw.noah.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrashDetailIndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrashDetailIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrashDetailIndexFragment extends Fragment {

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

    List<String> contentItems;
    List<String> items;
    StringAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    public CrashDetailIndexFragment() {
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
    public static CrashDetailIndexFragment newInstance(String param1, String param2) {
        CrashDetailIndexFragment fragment = new CrashDetailIndexFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();
        final File file = (File) object;
        String content = FileUtil.read(file);

        initDatas(content);
        adapter = new StringAdapter(mContext, items);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DebugActivity.startDetailActivity(mContext, items.get(position), DebugActivity.TYPE_CRASH_DETAIL_CONTENT, contentItems.get(position));
            }
        });
        return view;
    }

    private void initDatas(String content) {
        items = new ArrayList<>();
        contentItems = new ArrayList<>();
        content = content.replace("\r\n", "\n");
        String[] lines = content.split("\n");

        String ss = "";
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if (s.equals(CrashHandler.getInstance().startPrefix)) {
                if (i != lines.length - 2) {
                    items.add(lines[i + 1]);
                }
                if (!ss.equals("")) {
                    contentItems.add(ss);
                }
                ss = "";
            }
            ss += s + "\r\n";
        }
        if (!ss.equals("")) {
            contentItems.add(ss);
        }
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
        mActivity = (DebugDetailActivity) context;
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
