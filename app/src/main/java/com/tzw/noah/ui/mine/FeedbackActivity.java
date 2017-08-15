package com.tzw.noah.ui.mine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.Param;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.sns.group.GroupAddMemberActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.xiaopan.sketchsample.widget.SampleImageViewHead;
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
    private TextView tv_ver;

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
        tv_ver = (TextView) findViewById(R.id.tv_ver);
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
        //设备:Android 系统:5.0.1 版本:2.3.1
        String osver = android.os.Build.VERSION.RELEASE;
        String sdkver = android.os.Build.VERSION.SDK;
        String brand = Build.BRAND;
        String model = Build.MODEL;

        String appver = "";
        try {
            appver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_ver.setText("设备:" + brand + " " + model + " 系统:" + osver  + " 版本:" + appver);//+ " sdk: " + sdkver
        //tv_ver.setText(getDeviceInfo());
    }

    private String getDeviceInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("主板：" + Build.BOARD);
        sb.append("\n系统启动程序版本号：" + Build.BOOTLOADER);
        sb.append("\n系统定制商：" + Build.BRAND);
        sb.append("\ncpu指令集：" + Build.CPU_ABI);
        sb.append("\ncpu指令集2：" + Build.CPU_ABI2);
        sb.append("\n设置参数：" + Build.DEVICE);
        sb.append("\n显示屏参数：" + Build.DISPLAY);
        sb.append("\n无线电固件版本：" + Build.getRadioVersion());
        sb.append("\n硬件识别码：" + Build.FINGERPRINT);
        sb.append("\n硬件名称：" + Build.HARDWARE);
        sb.append("\nHOST:" + Build.HOST);
        sb.append("\n修订版本列表：" + Build.ID);
        sb.append("\n硬件制造商：" + Build.MANUFACTURER);
        sb.append("\n版本：" + Build.MODEL);
        sb.append("\n硬件序列号：" + Build.SERIAL);
        sb.append("\n手机制造商：" + Build.PRODUCT);
        sb.append("\n描述Build的标签：" + Build.TAGS);
        sb.append("\nTIME:" + Build.TIME);
        sb.append("\nbuilder类型：" + Build.TYPE);
        sb.append("\nUSER:" + Build.USER);
        return sb.toString();
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
