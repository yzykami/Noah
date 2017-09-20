package com.tzw.noah.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
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

import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/6/9.
 */

public class MediaComplaintActivity extends MyBaseActivity {

    String TAG = "MediaComplaintActivity";
    MediaComplaintActivity mycontext = MediaComplaintActivity.this;
    private LinearLayout ll;
    private List<Dict> complaintList;

    List<Boolean> selected;
    List<ImageView> imageViewList;
    private int articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_layout_complaint);
        setStatusBarLightMode();
        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            articleId = bu.getInt("articleId");
        } else {

        }
    }

    private void findview() {
        ll = (LinearLayout) findViewById(R.id.ll);
    }

    private void initview() {

        DBManager db = new DBManager(mycontext);
        complaintList = db.selectMediaComplaintList();
        selected = new ArrayList<>();
        imageViewList = new ArrayList<>();
        int i = 0;
        for (Dict d : complaintList) {
            ll.addView(getItemView(d, i++));
            selected.add(false);
        }
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
//        String text = et_text.getText().toString();
//        if (text.isEmpty()) {
//            toast("请写下您的意见或者建议");
//            return;
//        }
        Dict selectDict = null;
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                selectDict = complaintList.get(i);
                break;
            }
        }
        if (selectDict == null) {
            toast("请选择反馈类型");
            return;
        }

        NetHelper.getInstance().mediaComplaint(articleId, selectDict.dictionaryId, "", new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.log(TAG, e);
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    toast("您的投诉已提交,多谢您的反馈");
                    finish();
                } else {
                    Log.log(TAG, iMsg.getMsg());
                    toast(iMsg.getMsg());
                }
            }
        });
    }


    private View getView(String fileUrl) {

        float span = getResources().getDimension(R.dimen.bj);

        float sw = Utils.getSrceenWidth();

        int itemSize = (int) ((sw - 7 * span) / 6);

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.sns_group_member_item, null);

        SampleImageViewHead iv = (SampleImageViewHead) rl.findViewById(R.id.iv_head);
        TextView tv_name = (TextView) rl.findViewById(R.id.tv_name);
        tv_name.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();

        int nn = (int) span;

        layoutParams.width = itemSize;
        layoutParams.height = itemSize;
        layoutParams.setMargins(nn, 0, 0, nn / 2);
        iv.setLayoutParams(layoutParams);
        iv.displayImage(fileUrl);

//        tv_name.setText(gm.getMemberName());

//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (gm.memberNo == -1) {
//                    Bundle bu = new Bundle();
//                    bu.putSerializable("DATA", (ArrayList) items);
//                    bu.putSerializable("DATA2", group);
//                    startActivity(GroupAddMemberActivity.class, bu);
//                } else {
//                    User user = new User();
//                    user.memberNo = gm.memberNo;
//                    user.memberNickName = gm.getMemberName();
//                    user.memberHeadPic = gm.memberHeadPic;
//                    Bundle bu = new Bundle();
//                    bu.putSerializable("DATA", user);
//                    startActivity(PersonalActivity.class, bu);
//                }
//            }
//        });

        return rl;
    }
}
