package com.tzw.noah.ui.sns.group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Group;
import com.tzw.noah.models.GroupMember;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/5.
 */

public class GroupSetAdminActivity extends MyBaseActivity {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.ll_member)
    LinearLayout ll_member;
    @BindView(R.id.tv_num)
    TextView tv_num;

    Context mContext = GroupSetAdminActivity.this;

    private List<GroupMember> memberlist;

    String Tag = "GroupSetAdminActivity";
    Group group;

    Bundle bu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_group_setadmin);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        bu = getIntent().getExtras();
        if (bu != null) {
            group = (Group) bu.getSerializable("DATA");
        } else
            group = new Group();
        memberlist = new ArrayList<>();


    }

    private void findview() {
        initManagerListView();
    }

    private void initview() {
    }

    private View getMemberView(final GroupMember groupMember) {

        final SwipeLayout sl = (SwipeLayout) getLayoutInflater().inflate(R.layout.sns_select_item_swipe, null);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sl.getLayoutParams();
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        SampleImageViewHead iv_head = (SampleImageViewHead) sl.findViewById(R.id.iv_head);
        TextView tv_name = (TextView) sl.findViewById(R.id.tv_name);
        tv_name.setText(groupMember.getMemberName());
        iv_head.displayImage(groupMember.memberHeadUrl);

        sl.addDrag(SwipeLayout.DragEdge.Right, sl.findViewById(R.id.bottom_wrapper));

//        sl.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
//            @Override
//            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {
//
//                toast("Click on delete");
//            }
//        });

        sl.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl.close(true);
                onDelete(groupMember);
            }
        });

        sl.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return sl;
    }

    private void onDelete(GroupMember groupMember) {
        List<String> ids = new ArrayList<>();
        ids.add(groupMember.memberNo + "");

        new SnsManager(mContext).snsRemoveManagersFromGroup(group.groupId, ids, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        toast("移除管理员成功");
                        refreshView();
//                        setResult(100);
//                        finish();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    Log.log(Tag, e);
                }
            }
        });
    }

    private void initManagerListView() {
        ll_member.removeAllViews();
        GroupMember gm = new GroupMember();
        gm.groupMemberName = "hehe";
//        memberlist.add(gm);
//        memberlist.add(gm);
        for (int i = 0; i < memberlist.size(); i++) {
            ll_member.addView(getMemberView(memberlist.get(i)));
            View span = new View(mContext);
            span.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDimension(R.dimen.pt1) + 0.5f)));
            span.setBackgroundColor(getResources().getColor(R.color.bg_light));
            ll_member.addView(span);
        }
        tv_num.setText("管理员(" + memberlist.size() + "/10)");
        if (memberlist.size() == 0) {
            tv_num.setText("管理员");
        }
    }

    public void handle_edit(View view) {
        startActivity(GroupSelectAdminActivity.class, bu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new SnsManager(mContext).snsGetMembers(group.groupId, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {
                        if (iMsg.Data != null)
                            memberlist = (List<GroupMember>) iMsg.Data;
                        else
                            memberlist = GroupMember.loadManager(iMsg);
                        if (memberlist == null)
                            memberlist = new ArrayList<GroupMember>();
                        initManagerListView();
                    } else {
                        toast(iMsg.getMsg());
                    }
                } catch (Exception e)
                {
                    Log.log(Tag, e);
                }
            }
        });
    }
}
