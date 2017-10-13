package com.tzw.noah.ui.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;
import com.tzw.noah.ui.MyBaseActivity;
import com.tzw.noah.utils.KeyboardChangeListener;
import com.tzw.noah.utils.SoftHideKeyBoardUtil;
import com.tzw.noah.widgets.KeyBackObservableEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;

/**
 * Created by yzy on 2017/8/30.
 */
public class InputFragment extends Fragment {
    @BindView(R.id.tv_edit)
    KeyBackObservableEditText tv_edit;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;
    @BindView(R.id.iv_comment)
    ImageView iv_comment;
    @BindView(R.id.iv_fav)
    ImageView iv_fav;
    @BindView(R.id.iv_like)
    ImageView iv_like;
    @BindView(R.id.rl_edit)
    RelativeLayout rl_edit;
    @BindView(R.id.rl_comment)
    RelativeLayout rl_comment;

    View maskView;

    String Tag = "InputFragment";
    Context mContext;

    MyBaseActivity activity;
    AssemblyRecyclerAdapter adapter;

    MediaArticle mMediaArticle;
    MediaComment mMediaComment;
    boolean isFirstLoad = true;
    private int articleId = 0;

    View root;
    View rootView;

    boolean isRunning = true;
    private int maxCount = 1000;
    SoftHideKeyBoardUtil keyboradUtil;
    private boolean isBlackBackground = false;
    private View ll_content;

    public void notifyUpdate(MediaArticle mediaArticle) {
        mMediaArticle = mediaArticle;
        initview();
    }

    public void clearInputEdit() {
        tv_edit.setText("");
    }

    public InputFragment setBackgroundBlack(View view) {
        this.ll_content = view;
        this.isBlackBackground = true;
        return this;
    }

    public interface InputFragmentListener {

        public void onCommentClick();

        public void onLikeClick();

        public void onFavoriteClick();

        public void onSendClick(String content, int aId, int cId);
    }

    InputFragmentListener mInputFragmentListener;

    public InputFragment setMaskViewProvider(View maskViewProvider) {
        this.maskView = maskViewProvider;
        return this;
    }

    public InputFragment setMediaArticle(MediaArticle mMediaArticle) {
        this.mMediaArticle = mMediaArticle;
        return this;
    }

    public InputFragment setMediaComment(MediaComment mMediaComment) {
        this.mMediaComment = mMediaComment;
        return this;
    }

    public InputFragment setListener(InputFragmentListener mInputFragmentListener) {
        this.mInputFragmentListener = mInputFragmentListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.input_layout, container, false);
        ButterKnife.bind(this, view);
        if (isBlackBackground) {
            rl_edit.setBackgroundResource(R.drawable.media_input_bg_black);
            tv_edit.setBackgroundResource(R.drawable.bg_lightblack_fill_round);
        }
        keyboradUtil = SoftHideKeyBoardUtil.assistActivity(activity).setOnKeyboardChange(
                (new SoftHideKeyBoardUtil.KeyBoardListener() {
                    @Override
                    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                        if (isShow)
                            switchEditMode(true);
                        else {

//                            Log.log("bbb", "keyboard_close");
                            switchEditMode(false);
                        }
                    }
                }));

//        new KeyboardChangeListener(activity).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
//            @Override
//            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
//                if (isShow)
//                    switchEditMode(true);
//                else
//                    switchEditMode(false);
//            }
//        });

        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboradUtil.setFullScreen();
                activity.showKeyboard(false);
//                switchEditMode(false);
            }
        });
        switchEditMode(false);
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputFragmentListener != null) {
                    mInputFragmentListener.onCommentClick();
                }
            }
        });
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputFragmentListener != null) {
                    mInputFragmentListener.onLikeClick();
                }
            }
        });
        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputFragmentListener != null) {
                    mInputFragmentListener.onFavoriteClick();
                }
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = tv_edit.getText().toString();
                if (TextUtils.isEmpty(content))
                    return;
                int cid = 0;
                if (mMediaComment != null)
                    cid = mMediaComment.articleCommentId;
                if (mInputFragmentListener != null) {
                    mInputFragmentListener.onSendClick(content, mMediaArticle.articleId, cid);
                }
            }
        });
        tv_edit.addTextChangedListener(new TextWatcher() {
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
                selectionStart = tv_edit.getSelectionStart();
                selectionEnd = tv_edit.getSelectionEnd();
                if (temp.length() > maxCount) {


                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    tv_edit.setText(s);
                    tv_edit.setSelection(tempSelection);
                }
                if (s.length() > maxCount)
                    Toast.makeText(mContext, "评论内容过长,请分段发表", Toast.LENGTH_SHORT).show();
//                tv_count.setText((maxCount - temp.length()) + "");
            }
        });

        tv_edit.setOnBackPressedListener(new KeyBackObservableEditText.OnBackPressedListener() {
            @Override
            public boolean onBackPressed() {
                keyboradUtil.setFullScreen();
//                Log.log("bbb", "tv_edit_onbackpress");
                return false;
            }
        });
        initview();

        return view;
    }

    private void initview() {
        tv_comment_count.setText(mMediaArticle.articleCommentSum + "");
        if (mMediaArticle.isArticleEvaluate == 1) {
            iv_like.setImageResource(R.drawable.media_input_like_ed);
        } else {
            iv_like.setImageResource(R.drawable.media_input_like);
        }
        if (mMediaArticle.isArticleKeep) {
            iv_fav.setImageResource(R.drawable.media_input_fav_ed);
        } else {
            iv_fav.setImageResource(R.drawable.media_input_fav);
        }
        if (mMediaComment != null) {
            rl_comment.setVisibility(View.GONE);
            iv_fav.setVisibility(View.GONE);
            iv_like.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyBaseActivity) context;
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isFirstLoad) {
//            isFirstLoad = false;
//            refreshListView();
//        } else
//            refreshListView2();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser)
//            refreshListView2();
    }

    public void switchEditMode(boolean isEditMode) {

        if (isEditMode) {
            rl_comment.setVisibility(View.GONE);
            iv_fav.setVisibility(View.GONE);
            iv_like.setVisibility(View.GONE);
            tv_send.setVisibility(View.VISIBLE);
            maskView.setVisibility(View.VISIBLE);
            if (isBlackBackground) {
                tv_edit.setBackgroundResource(R.drawable.bg_lightblack_fill_round);
                ll_content.setVisibility(View.GONE);
            } else
                tv_edit.setBackgroundResource(R.color.transParent);
            tv_edit.setMaxLines(6);

        } else {
            keyboradUtil.setFullScreen();

            rl_comment.setVisibility(View.VISIBLE);
            iv_fav.setVisibility(View.VISIBLE);
            iv_like.setVisibility(View.VISIBLE);
            tv_send.setVisibility(View.GONE);
            maskView.setVisibility(View.GONE);

            if (isBlackBackground) {
                tv_edit.setBackgroundResource(R.drawable.bg_lightblack_fill_round);
                ll_content.setVisibility(View.VISIBLE);
            } else
                tv_edit.setBackgroundResource(R.drawable.bg_gray_fill_round);
            tv_edit.setMaxLines(1);
        }
        if (mMediaComment != null) {
            rl_comment.setVisibility(View.GONE);
            iv_fav.setVisibility(View.GONE);
            iv_like.setVisibility(View.GONE);
        }
    }

    public View getEditView() {
        return tv_edit;
    }
}
