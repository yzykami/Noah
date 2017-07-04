package com.tzw.noah.ui.sns.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.models.SnsMessage;
import com.tzw.noah.models.SnsPerson;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.ui.adapter.itemfactory.ChatListItemFactory;
import com.tzw.noah.ui.adapter.itemfactory.SearchHeadFactory;
import com.tzw.noah.ui.sns.group.GroupDetailActivity;
import com.tzw.noah.ui.sns.personal.PersonalActivity;
import com.tzw.noah.ui.sns.personal.PersonalChatDetailActivity;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.sketchsample.adapter.itemfactory.LoadMoreItemFactory;
import me.xiaopan.sketchsample.adapter.itemfactory.UnsplashPhotosItemFactory;

/**
 * Created by yzy on 2017/6/26.
 */

public class ChatActivity extends MyBaseActivity {
    @BindView(R.id.header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;

    @BindView(R.id.list_view)
    RecyclerView listView;
    @BindView(R.id.framelayout)
    FrameLayout frameLayout;
    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.tv_input)
    EditText tv_input;

    Context mContext = ChatActivity.this;
    private ChatRecyclerAdapter adapter;
    private List<SnsMessage> items;

    int type = 0;
    boolean isPersonal = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_layout_chat);
        ButterKnife.bind(this);

        initdata();
        findview();
        initview();
        doWorking();
    }

    private void initdata() {
        Bundle bu = getIntent().getExtras();
        if (bu != null) {
            type = bu.getInt("DATA");
        }

        isPersonal = type == 0;
    }

    private void findview() {

    }

    private void initview() {
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadMore();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        setupViews(mPtrFrame);

        updateData();

        frameLayout.setVisibility(View.GONE);

        if (isPersonal) {
            iv_detail.setImageResource(R.drawable.sns_person_info);
        } else
            iv_detail.setImageResource(R.drawable.sns_group_info);

        tv_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {//(actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_SEND) {
                    if (tv_input.getText().toString().isEmpty())
                        return true;

                    adapter = new ChatRecyclerAdapter(mContext, makeData2(tv_input.getText().toString()));
                    listView.setAdapter(adapter);
                    listView.smoothScrollToPosition(0);//adapter.getItemCount());
//                    toast(tv_input.getText().toString());
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
//                                0);
//                    }
                    tv_input.setText("");
                    return true;
                }
                return false;
            }
        });
        tv_input.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                selectionStart = tv_input.getSelectionStart();
//                selectionEnd = tv_input.getSelectionEnd();
//                if (temp.length() > maxCount) {
//
//
//                    s.delete(selectionStart - 1, selectionEnd);
//                    int tempSelection = selectionStart;
//                    et_sign.setText(s);
//                    et_sign.setSelection(tempSelection);
//                }
//                tv_input.setText((maxCount - temp.length()) + "");
            }
        });


    }


    private void loadMore() {
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
            }
        }, 500);
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }

    protected void updateData() {

        items = new ArrayList<>();
        makeData();
        adapter = new ChatRecyclerAdapter(mContext, items);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true));
        if (adapter.getItemCount() > 0)
            listView.smoothScrollToPosition(0);//adapter.getItemCount());
    }

    private void doWorking() {
    }


    private void makeData() {
        List<String> contentlist = new ArrayList<>();
        contentlist.add("你111");
        contentlist.add("你好在");
        contentlist.add("耐111");
        contentlist.add("废柴");
        contentlist.add("风");
        contentlist.add("银");
        contentlist.add("辛巴");
        contentlist.add("2342辛巴");
        contentlist.add("啦啦");
        contentlist.add("❤啦啦");
        contentlist.add("OMG呵呵");
        contentlist.add("ddd呵呵");

        items = new ArrayList<>();
        int i = 0;
        String[] names = new String[]{"奇犽", "小杰"};
        String[] headurls = new String[]{"http://img3.duitang.com/uploads/item/201406/27/20140627214706_ESetE.jpeg", "http://img5.imgtn.bdimg.com/it/u=1884722731,3085178072&fm=26&gp=0.jpg"};
        for (String content : contentlist) {
            SnsMessage p = new SnsMessage();
            p.name = names[i % 2];
            p.imageUrl = headurls[i % 2];
            p.isMyself = i % 2 == 0;
            p.sendTime = "12:42";
            p.content = content;
            i++;
            items.add(p);
        }
    }

    private List<SnsMessage> makeData2(String s) {
        List<String> contentlist = new ArrayList<>();
        contentlist.add(s);
        List<SnsMessage> items2 = new ArrayList<>();

//        items = new ArrayList<>();
        int i = 0;
        String[] names = new String[]{"奇犽", "小杰"};
        String[] headurls = new String[]{"http://img3.duitang.com/uploads/item/201406/27/20140627214706_ESetE.jpeg", "http://img5.imgtn.bdimg.com/it/u=1884722731,3085178072&fm=26&gp=0.jpg"};
        for (String content : contentlist) {
            SnsMessage p = new SnsMessage();
            p.name = names[i % 2];
            p.imageUrl = headurls[i % 2];
            p.isMyself = i % 2 == 0;
            p.sendTime = "12:42";
            p.content = content;
            i++;
            items2.add(p);
        }
        items2.addAll(items);
        items = items2;
        return items2;
    }

    public void handle_info(View view) {

        if (isPersonal)
            startActivity(PersonalChatDetailActivity.class);
        else
            startActivity(GroupDetailActivity.class);

    }
}
