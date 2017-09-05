package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tzw.noah.R;
import com.tzw.noah.ui.mine.DebugActivity;
import com.tzw.noah.ui.mine.DebugDetailActivity;
import com.tzw.noah.ui.mine.StringAdapter;
import com.tzw.noah.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatabaseTableListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatabaseTableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatabaseTableListFragment extends Fragment {

    @BindView(R.id.container)
    ViewGroup rootViewGroup;

    @BindView(R.id.list_view)
    ListView list_view;

    List<File> fileitems;
    List<String> items;
    StringAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private DebugDetailActivity mActivity;
    String databasePath;

    public DatabaseTableListFragment() {
        // Required empty public constructor
    }

    public static DatabaseTableListFragment newInstance() {
        DatabaseTableListFragment fragment = new DatabaseTableListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        File inputFile = (File) mActivity.getObject();
        if (inputFile != null)
            databasePath = inputFile.getPath();
        else
            databasePath = FileUtil.getDatabasePath();

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                databasePath, null);

        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            items.add(name);
        }

//        fileitems = getCrashFileList();
//        items = getCrashFileNameList();

        adapter = new StringAdapter(mContext, items);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> bu = new HashMap<String, String>();
                bu.put("file", databasePath);
                bu.put("tablename", items.get(position));
                DebugActivity.startDetailActivity(mContext, items.get(position), DebugActivity.TYPE_DATEBASE_TABLE, bu);
            }
        });
        return view;
    }

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

    public List<File> getCrashFileList() {
        List<File> crashFileList = new ArrayList<>();

        File f = new File(databasePath);
        File[] files = f.listFiles();// 列出所有文件
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            crashFileList.add(ff);
        }
        return crashFileList;
    }

    public List<String> getCrashFileNameList() {
        List<String> crashFileNameList = new ArrayList<>();

        File f = new File(databasePath);
        File[] files = f.listFiles();// 列出所有文件
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            if (ff.isFile()) {
                crashFileNameList.add("数据库文件: " + ff.getName());
            } else
                crashFileNameList.add("目录: " + ff.getName());
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
        void onFragmentInteraction(Uri uri);
    }
}
