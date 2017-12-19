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
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.ui.mine.DebugActivity;
import com.tzw.noah.ui.mine.DebugDetailActivity;
import com.tzw.noah.ui.mine.StringAdapter;
import com.tzw.noah.utils.FileUtil;
import com.tzw.noah.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatabaseTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatabaseTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatabaseTableFragment extends Fragment {

    @BindView(R.id.container)
    ViewGroup rootViewGroup;

//    @BindView(R.id.tableView)
    TableView<String[]> tableView;

    @BindView(R.id.hsv)
    HorizontalScrollView hsv;
    @BindView(R.id.ll)
    LinearLayout ll;

    List<File> fileitems;
    List<String> items;
    StringAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private DebugDetailActivity mActivity;
    String databasePath;

    public DatabaseTableFragment() {
        // Required empty public constructor
    }

    public static DatabaseTableFragment newInstance() {
        DatabaseTableFragment fragment = new DatabaseTableFragment();
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
        View view = inflater.inflate(R.layout.fragment_tableview, container, false);
        ButterKnife.bind(this, view);
        HashMap<String, String> bu = (HashMap<String, String>) mActivity.getObject();
        if (bu != null) {

            databasePath = bu.get("file");
            String tablename = bu.get("tablename");

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                    databasePath, null);


            String[][] DATA_TO_SHOW = {{"This", "is", "a", "test"},
                    {"and", "a", "second", "test"}};

            String sql = "select * from " + tablename + " limit 200";
            Cursor c = db.rawQuery(sql, null);
            String[] headers = new String[]{};
            try {
                headers = c.getColumnNames();
                DATA_TO_SHOW = new String[c.getCount()][c.getColumnCount()];
                int i = 0;

                while (c.moveToNext()) {
                    String[] columns = new String[c.getColumnCount()];
                    for (int j = 0; j < columns.length; j++) {
                        columns[j] = c.getString(j);
                    }
                    DATA_TO_SHOW[i] = columns;
                    i++;
                }
            } catch (Exception e) {
                Log.log("DatabaseTableFragment", e);
            } finally {
                if (c != null)
                    c.close();
            }
            tableView =new TableView<String[]>(mContext);
            tableView.setDataAdapter(new SimpleTableDataAdapter(mContext, DATA_TO_SHOW));
            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(mContext, headers));

//            FrameLayout.LayoutParams layoutparams =new FrameLayout.LayoutParams(headers.length* Utils.dp2px(mContext,150), ViewGroup.LayoutParams.MATCH_PARENT);
////            layoutparams.width = headers.length* Utils.dp2px(mContext,150);
//            hsv.setLayoutParams(layoutparams);

            HorizontalScrollView.LayoutParams layoutparams2 =new HorizontalScrollView.LayoutParams(headers.length* Utils.dp2px(mContext,150), ViewGroup.LayoutParams.MATCH_PARENT);
            layoutparams2.width = headers.length* Utils.dp2px(mContext,150);
            tableView.setLayoutParams(layoutparams2);

            TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(mContext, headers.length, 150);
            tableView.setColumnModel(columnModel);
            tableView.setColumnCount(headers.length);
            ll.addView(tableView);
        }

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
