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
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

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

    private List<SnsPerson> memberlist;


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
        c();
    }

    private void findview() {
        ll_member.removeAllViews();
        for (int i = 0; i < 6 && i < memberlist.size(); i++) {
            ll_member.addView(getMemberView(memberlist.get(i)));
        }
        tv_num.setText("管理员("+memberlist.size()+"/10)");
    }

    private void initview() {
    }

    private View getMemberView(SnsPerson snsPerson) {

//        float span = getResources().getDimension(R.dimen.bj);
//
//        float sw = Utils.getSrceenWidth();
//
//        int itemSize = (int) ((sw - 7 * span) / 6);

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.sns_select_item, null);

        SampleImageViewHead iv_head = (SampleImageViewHead) rl.findViewById(R.id.iv_head);
        ImageView iv = (ImageView) rl.findViewById(R.id.iv);
        TextView tv_name = (TextView) rl.findViewById(R.id.tv_name);
        iv.setVisibility(View.GONE);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//
//        int nn = (int) span;
//
//        layoutParams.width = itemSize;
//        layoutParams.height = itemSize;
//        layoutParams.setMargins(nn, 0, 0, nn / 2);
//        iv.setLayoutParams(layoutParams);
        iv_head.displayImage(snsPerson.headUrl);
        return rl;
    }

    public void c() {
        List<String> images = new ArrayList<String>();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646800199&di=e588dafd6e16678d08e8404c7f6a5651&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_2_1979295486D2113125476_23.jpg");
        images.add("http://v1.qzone.cc/avatar/201405/10/17/00/536deaa6c35a9512.jpg!200x200.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646872650&di=8c968f968b9423051048d1eec7c5d598&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_3520253239D3803949043_21.jpg");
        images.add("http://img17.3lian.com/d/file/201702/22/1005a2e0825ffe290b3f697404ee8038.jpg");
//        images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=c89f6064a21ea8d38e227306a70b30cf/0824ab18972bd407ce9f04227f899e510eb30991.jpg");
//        images.add("drawable://" + R.drawable.sns_add_person);
//        images.add("http://www.adquan.com/upload/20151223/1450838259813154.jpg");
//        images.add("http://www.feizl.com/upload2007/2015_07/150720124522248.jpg");
//        images.add("http://pic.iqshw.com/d/file/gexingqqziyuan/touxiang/2016/03/17/8581e320e98e541ed03a8fcab51068fd.jpg");
//        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222436.jpg");
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646757823&di=ceb2ef896125f0f5ead9140c5e68cef7&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-3%2F2016030111061053440.jpg");
//        images.add("http://www.feizl.com/upload2007/2015_07/1507201245222419.jpg");
        memberlist = new ArrayList<>();

        for (String name : images) {
            SnsPerson p = new SnsPerson();
            p.headUrl = name;
            p.name = "aaa";
            p.namePingyin = Utils.getLetter(name);
            p.shortCut = "好友推荐";
            memberlist.add(p);
        }
    }

    public void handle_edit(View view) {
        startActivity(GroupSelectAdminActivity.class);
    }
}
