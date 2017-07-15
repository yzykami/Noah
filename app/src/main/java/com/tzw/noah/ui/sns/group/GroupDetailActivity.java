package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.GroupType;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.BottomPopupWindow;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.utils.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import me.xiaopan.sketch.util.SketchUtils;
import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

import static com.tzw.noah.R.id.list_view;

/**
 * Created by yzy on 2017/7/3.
 */

public class GroupDetailActivity extends MyBaseActivity implements BottomPopupWindow.OnItemClickListener {
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.ll_member)
    LinearLayout ll_member;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_id)
    TextView tv_group_id;
    @BindView(R.id.tv_group_name1)
    TextView tv_group_name1;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_bulletin)
    TextView tv_bulletin;
    @BindView(R.id.iv_msg)
    ImageView iv_msg;
    @BindView(R.id.iv_slient)
    ImageView iv_slient;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_count)
    TextView tv_count;


    Context mContext = GroupDetailActivity.this;
    private List<SnsPerson> memberlist;
    String Tag = "GroupDetailActivity";

    Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_detail);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        getMemberList();
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
        ll_member.removeAllViews();
        tv_group_name.setText(group.groupName);
        tv_group_name1.setText(group.groupName);
        tv_introduce.setText(group.groupIntroduction);
        tv_introduce.setText(group.memberCount + "人");
    }

    private View getMemberView(GroupMember gm) {

        float span = getResources().getDimension(R.dimen.bj);

        float sw = Utils.getSrceenWidth();

        int itemSize = (int) ((sw - 7 * span) / 6);

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.sns_group_member_item, null);

        SampleImageViewHead iv = (SampleImageViewHead) rl.findViewById(R.id.iv_head);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();

        int nn = (int) span;

        layoutParams.width = itemSize;
        layoutParams.height = itemSize;
        layoutParams.setMargins(nn, 0, 0, nn / 2);
        iv.setLayoutParams(layoutParams);
        iv.displayImage(gm.memberHeadUrl);
        return rl;
    }


    public void handle_memberlist(View view) {
        startActivity(GroupMemberListActivity.class);
    }

    public void handle_more(View view) {
        BottomPopupWindow.create(this, this).addItem("分享群").addItem("举报").addItem("退出该群", R.color.myRed).show();

    }

    @Override
    public void OnItemClick(int position, String title) {
        toast(title);//"index = " + position + " : "+
    }

    public void handle_manager(View view) {
        startActivity(GroupManagerActivity.class);
    }

    public void getMemberList() {

        new SnsManager(mContext).snsGroupType(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        List<GroupMember> items;
                        if (iMsg.Data != null)
                            items = (List<GroupMember>) iMsg.Data;
                        else
                            items = GroupMember.loadList(iMsg);
                        if (items != null && items.size() > 0) {
                            ll_member.removeAllViews();
                            for (int i = 0; i < 4 && i < items.size(); i++) {
                                items.get(i).memberHeadUrl = "drawable://" + R.drawable.sns_user_default;
                                ll_member.addView(getMemberView(items.get(i)));
                            }
                            GroupMember gm = new GroupMember();
                            gm.memberHeadUrl = "drawable://" + R.drawable.sns_add_person;
                            ll_member.addView(getMemberView(gm));
                            gm.memberHeadUrl = "drawable://" + R.drawable.sns_delete_person;
                            ll_member.addView(getMemberView(gm));
                        }
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
