package com.tzw.noah.ui.sns.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.tzw.noah.R;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.Area;
import com.tzw.noah.models.Dict;
import com.tzw.noah.models.User;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.sdk.SnsManager;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.Utils;
import com.tzw.noah.widgets.ListenedScrollView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;
import okhttp3.Call;

/**
 * Created by yzy on 2017/7/3.
 */

public class PersonalActivity extends MyBaseActivity {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.tv_registday)
    TextView tv_registday;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.tv_interest)
    TextView tv_interest;
    @BindView(R.id.tv_character)
    TextView tv_character;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_sign)
    TextView tv_sign;
    @BindView(R.id.tv_relate)
    TextView tv_relate;
    @BindView(R.id.tv_btn1)
    TextView tv_btn1;
    @BindView(R.id.tv_btn2)
    TextView tv_btn2;

    @BindView(R.id.scrollView)
    ListenedScrollView scrollView;
    @BindView(R.id.iv_head)
    SampleImageViewHead iv_head;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;

    Context mContext = PersonalActivity.this;

    int touchHeight;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_personal);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
    }

    private void initdata() {
        float bgHeight = getResources().getDimension(R.dimen.sns_personal_bg_height);
        float titleHeight = getResources().getDimension(R.dimen.title_height);
        int headHeight = Utils.dp2px(mContext, 80 / 2);
        touchHeight = (int) (bgHeight - titleHeight - headHeight);
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            user = (User) bu.getSerializable("DATA");
        }
    }

    private void findview() {

    }

    private void initview() {
        tv_name.setText(user.getName());
        tv_id.setText("会员号: " + user.memberNo);
        String day = "";
        if (user.createTime.length() > 10) {
            day = user.createTime.substring(0, 10);
        }
        tv_registday.setText(day);
        tv_area.setText(getArea(user.areaId));
        tv_interest.setText(getInterestbyID(user.memberInterest));
        tv_character.setText(getCharacterByID(user.memberCharacter));
        tv_job.setText(getWorkbyID(user.memberWork));
        tv_sign.setText(user.memberIntroduce);

        tv_relate.setText(user.getRelative());

        if (user.getRelative().equals(User.RelativeType.Blacklist)) {
            tv_btn1.setVisibility(View.GONE);
            tv_btn2.setVisibility(View.GONE);
        } else if (user.getRelative().equals(User.RelativeType.Fowllow) || user.getRelative().equals(User.RelativeType.Friend)) {
            tv_btn1.setVisibility(View.GONE);
            tv_btn2.setTextColor(getResources().getColor(R.color.white));
            tv_btn2.setBackgroundResource(R.drawable.bg_red_fill_round);
        } else {
            tv_btn1.setVisibility(View.VISIBLE);
            tv_btn2.setTextColor(getResources().getColor(R.color.myRed));
            tv_btn2.setBackgroundResource(R.drawable.bg_red_border_round);
        }


        scrollView.setOnScrollListener(new ListenedScrollView.OnScrollListener() {
                                           @Override
                                           public void onBottomArrived() {

                                           }

                                           @Override
                                           public void onScrollStateChanged(ListenedScrollView view, int scrollState) {

                                           }

                                           @Override
                                           public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                                               Log.log("bbb", "x = " + l + ",y = " + t + ",th = " + touchHeight);
//                                               if (t <= touchHeight && t > 10) {
//                                                   int c = getResources().getColor(R.color.myRed);
//                                                   float x = (float) t;
//                                                   float y = (float) touchHeight;
//                                                   int a = (int) ((x / y) * 255);
//                                                   a <<= 6;
//                                                   a ^= c;
//                                                   final int aa =a;
//                                                   scrollView.post(new Runnable() {
//                                                       @Override
//                                                       public void run() {
//                                                           rl_top.setBackgroundColor(aa);
//                                                       }
//                                                   });
//                                               } else if (t > touchHeight)
//                                                   rl_top.setBackgroundColor(getResources().getColor(R.color.myRed));
//                                               else
//                                                   rl_top.setBackgroundColor(getResources().getColor(R.color.transParent));
                                           }
                                       }
        );


    }

    public void handle_more(View view) {
        if (user.getRelative().equals(User.RelativeType.Stranger)) {
            toast("你们还是陌生人,不能查看更多");
            return;
        }
        Bundle bu = new Bundle();
        bu.putSerializable("DATA", user);
        startActivity(PersonalMoreActivity.class, bu);
    }

    private String getArea(int areaId) {
        String area = "";
        DBManager db = new DBManager(mContext);

        Area province = db.queryProvinceByTownId(areaId);
        Area city = db.queryCityByTownId(areaId);
        Area town = db.queryTownByTownId(areaId);
        area = province.areaName + " " + city.areaName + " " + town.areaName;
        return area;
    }

    private String getInterestbyID(String memberInterest) {

        List<Dict> interestList = new DBManager(mContext).selectInterestList();
        return getDictNameById(memberInterest, interestList);
    }

    private String getCharacterByID(String memberCharacter) {
        List<Dict> interestList = new DBManager(mContext).selectCharacterList();
        return getDictNameById(memberCharacter, interestList);
    }

    private String getWorkbyID(String memberWork) {

        List<Dict> interestList = new DBManager(mContext).selectJobtList();
        return getDictNameById(memberWork, interestList);
    }

    private String getDictNameById(String ids, List<Dict> list) {
        String s = "";
        String[] ss = ids.split(",");
        for (String sss : ss) {
            if (!sss.isEmpty()) {
                for (Dict dict : list) {
                    if (dict.dictionaryId.equals(sss)) {
                        if (s.isEmpty()) {
                            s += dict.dictionaryName;
                        } else
                            s += " " + dict.dictionaryName;
                    }
                }
            }
        }
        return s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new SnsManager(mContext).snsDetail(user, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    user = (User) iMsg.Data;
                    initview();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    public void handle_btn1(View view) {
        new SnsManager(mContext).snsAttention(user.memberNo, new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                toast(getResources().getString(R.string.internet_fault));
            }

            @Override
            public void onResponse(IMsg iMsg) {
                if (iMsg.isSucceed()) {
                    toast("关注成功");
                    refreshView();
                } else {
                    toast(iMsg.getMsg());
                }
            }
        });
    }

    public void handle_btn2(View view) {
    }
}
