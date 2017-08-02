package com.tzw.noah.ui.sns.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.tzw.noah.R;
import com.tzw.noah.cache.UserCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.mine.MineMainActivity;
import com.tzw.noah.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageView;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/4.
 */

public class GroupEditActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.iv_bg)
    SampleImageView iv_bg;


    Context mContext = GroupEditActivity.this;

    String Tag = "GroupEditActivity";
    Group group;

    private static final int PICK_AVATAR_REQUEST = 0x0E;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_edit);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
    }

    private void findview() {

    }

    private void initview() {
        tv_group_name.setText(group.groupName);
        String introduce = "未设置";
        if (!group.groupIntroduction.isEmpty()) {
            introduce = group.groupIntroduction;
        }
        tv_introduce.setText(introduce);
        iv_bg.getOptions().setErrorImage(R.drawable.sns_group_bg);
        iv_bg.getOptions().setLoadingImage(R.drawable.sns_group_bg);
        iv_bg.displayImage(group.groupHeader);
    }

    public void handle_edit_name(View view) {

        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditNameActivity.class, bu);
    }

    public void handle_edit_introduce(View view) {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        startActivityForResult(100, GroupEditIntroduceActivity.class, bu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == 100) {
                if (data != null) {
                    Bundle bu = data.getExtras();
                    if (bu != null) {
                        group = (Group) bu.getSerializable("DATA");
                        initview();
                    }
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(com.netease.nim.uikit.session.constant.Extras.EXTRA_FILE_PATH);
            updateAvatar(path);
        }
    }

    public void handle_back() {
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", group);
        Intent intent = new Intent();
        intent.putExtras(bu);
        setResult(100, intent);
        finish();
    }

    public void handle_select_picture(View view) {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = com.netease.nim.demo.R.string.set_team_image;;
        option.crop = true;
        option.multiSelect = false;
        option.cropOutputImageWidth = 960;
        option.cropOutputImageHeight = 540;
        PickImageHelper.pickImage(mContext, PICK_AVATAR_REQUEST, option);
    }

    public void updateAvatar(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);

        if (file == null) {
            return;
        }
//        Log.log(Tag, "上传头像大小 = "+file.length() + "");
//        Bitmap bm = Utils.getSmallBitmap(path);
//        Log.log(Tag, "上传头像大小 = "+bm.getByteCount() + "");

        Map<String, File> fileBody = new HashMap<>();
        fileBody.put("groupHeader", file);
        NetHelper.getInstance().snsUploadIconToGroup(group.groupId, fileBody, new StringDialogCallback(this) {
            @Override
            public void onFailure(Call call, IOException e) {

                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
//                        initview();
                        iv_bg.displayImage(path);
                        toast("群头像上传成功");
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
