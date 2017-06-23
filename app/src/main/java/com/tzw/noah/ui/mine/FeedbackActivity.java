package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Device;
import com.tzw.noah.models.Dict;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class FeedbackActivity extends MyBaseActivity {

    String TAG = "FeedbackActivity";
    FeedbackActivity mycontext = FeedbackActivity.this;
    private LinearLayout ll;
    private List<Dict> feedbackList;

    List<Boolean> selected;
    List<ImageView> imageViewList;
    private EditText et_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_feedback);
        findview();
        initview();
        doWorking();
    }

    private void findview() {
        ll = (LinearLayout) findViewById(R.id.ll);
        et_text = (EditText) findViewById(R.id.et_text);
    }

    private void initview() {

        DBManager db = new DBManager(mycontext);
        feedbackList = db.selectFeedbacktList();
        selected = new ArrayList<>();
        imageViewList = new ArrayList<>();
        int i = 0;
        for (Dict d : feedbackList) {
            ll.addView(getItemView(d, i++));
            selected.add(false);
        }
    }

    private void doWorking() {

    }

    View getItemView(Dict dict, final int i) {
        View v = (View) LayoutInflater.from(mycontext).inflate(R.layout.mine_settting_personal_nickname_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        final ImageView iv = (ImageView) v.findViewById(R.id.iv);
        tv.setText(dict.dictionaryName);
        tv.setTextColor(getResources().getColor(R.color.textDarkGray));
        iv.setVisibility(View.GONE);
        imageViewList.add(iv);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isselect = selected.get(i);
                isselect = !isselect;

                for (int ii = 0; ii < selected.size(); ii++) {
                    selected.set(ii, false);
                    imageViewList.get(ii).setVisibility(View.GONE);
                }
                selected.set(i, isselect);
                if (isselect)
                    iv.setVisibility(View.VISIBLE);
                else iv.setVisibility(View.GONE);
            }
        });
        return v;
    }

    public void handle_submit(View view) {
        String text = et_text.getText().toString();
        if (text.isEmpty()) {
            toast("请写下您的意见或者建议");
            return;
        }
        Dict selectDict = null;
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                selectDict = feedbackList.get(i);
                break;
            }
        }
        if (selectDict == null) {
            toast("请选择反馈类型");
            return;
        }
        List<Param> body = new ArrayList<>();
        body.add(new Param("feedBackType", Utils.String2Int(selectDict.dictionaryId)));
        body.add(new Param("feedBackValue", text));
        body.add(new Param("contactPerson", UserCache.getUser().memberNickName));
        body.add(new Param("contactNumber", UserCache.getUser().memberMobile));

        NetHelper.getInstance().operationFeedback(body, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.log(TAG, e);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    toast("您的意见已提交,多谢您的反馈");
                    finish();
                } else {
                    Log.log(TAG, iMsg.getMsg());
                    toast(iMsg.getMsg());
                }
            }
        });
    }
}
