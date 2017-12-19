package com.tzw.noah.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.cache.AppCache;
import com.tzw.noah.db.DBManager;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.AppCacheModel;
import com.tzw.noah.net.IMsg;
import com.tzw.noah.net.NetHelper;
import com.tzw.noah.net.StringDialogCallback;
import com.tzw.noah.ui.mine.DebugDetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SystemCacheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemCacheFragment extends Fragment {

    @BindView(R.id.ll_local)
    LinearLayout llLocal;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_config)
    TextView tvConfig;
    @BindView(R.id.tv_dictype)
    TextView tvDictype;
    @BindView(R.id.tv_dic)
    TextView tvDic;
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.tv_wordtype)
    TextView tvWordType;
    @BindView(R.id.tv_update)
    TextView tvUpdate;


    @BindView(R.id.tv_total_local)
    TextView tvTotalLocal;
    @BindView(R.id.tv_area_local)
    TextView tvAreaLocal;
    @BindView(R.id.tv_config_local)
    TextView tvConfigLocal;
    @BindView(R.id.tv_dictype_local)
    TextView tvDictypeLocal;
    @BindView(R.id.tv_dic_local)
    TextView tvDicLocal;
    @BindView(R.id.tv_word_local)
    TextView tvWordLocal;
    @BindView(R.id.tv_wordtype_local)
    TextView tvWordTypeLocal;
    @BindView(R.id.textView2)
    TextView textView2;

    private String mParam1;
    private static final String ARG_PARAM1 = "param1";
    private static final String Tag = "SystemCacheFragment";

    private Context mContext;
    DebugDetailActivity mActivity;
    Object object;

    int localTotalVersion, localArea, localConfig, localDictype, localDic, localWord, localWordType;
    int serverTotalVersion, serverArea, serverConfig, serverDictype, serverDic, serverWord, serverWordType;

    int differentCount = 0;
    boolean isUpdateSuccess = true;

    public SystemCacheFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrashListFragment.
     */
    public static SystemCacheFragment newInstance() {
        SystemCacheFragment fragment = new SystemCacheFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mine_debug_systemcache, container, false);
        ButterKnife.bind(this, view);
        object = mActivity.getObject();
        initview();
        return view;
    }

    private void initview() {
        List<AppCacheModel> list = new DBManager(mContext).getAppCaches();
//        llLocal.removeAllViews();
        for (AppCacheModel acm : list) {
            TextView tv = new TextView(mContext);
//            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(width, height);
//            lp.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.bj), 0, mContext.getResources().getDimensionPixelSize(R.dimen.bj), 0);
//            tv.setText(acm.appCacheRemark + " : " + acm.appCacheVersion);
//            tv.setPadding((int) mContext.getResources().getDimension(R.dimen.bj), (int) mContext.getResources().getDimension(R.dimen.bj), 0, 0);
//            tv.setTextColor(mContext.getResources().getColor(R.color.textDarkGray));
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            tv.setGravity(Gravity.CENTER_VERTICAL);
//            tv.setLines(1);
//            llLocal.addView(tv);


            if (acm.appCacheId.equals("AllCache")) {
                serverTotalVersion = acm.appCacheVersion;
                tvTotal.setText("缓存总版本 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("AreaCache")) {
                serverArea = acm.appCacheVersion;
                tvArea.setText("地区信息缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("ConfigurationCache")) {
                serverConfig = acm.appCacheVersion;
                tvConfig.setText("系统配置缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("DictionaryCache")) {
                serverDictype = acm.appCacheVersion;
                tvDictype.setText("字典类型缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("DictionaryTpyeCache")) {
                serverDic = acm.appCacheVersion;
                tvDic.setText("字典信息缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("SensitiveWordsCache")) {
                serverWord = acm.appCacheVersion;
                tvWord.setText("敏感字缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("SensitiveWordsType")) {
                serverWordType = acm.appCacheVersion;
                tvWordType.setText("敏感词词类型缓存 : " + acm.appCacheVersion);
            }


            if (acm.appCacheId.equals("AllCache")) {
                localTotalVersion = acm.appCacheVersion;
                tvTotalLocal.setText("缓存总版本 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("AreaCache")) {
                localArea = acm.appCacheVersion;
                tvAreaLocal.setText("地区信息缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("ConfigurationCache")) {
                localConfig = acm.appCacheVersion;
                tvConfigLocal.setText("系统配置缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("DictionaryCache")) {
                localDictype = acm.appCacheVersion;
                tvDictypeLocal.setText("字典类型缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("DictionaryTpyeCache")) {
                localDic = acm.appCacheVersion;
                tvDicLocal.setText("字典信息缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("SensitiveWordsCache")) {
                localWord = acm.appCacheVersion;
                tvWordLocal.setText("敏感字缓存 : " + acm.appCacheVersion);
            }
            if (acm.appCacheId.equals("SensitiveWordsType")) {
                localWord = acm.appCacheVersion;
                tvWordTypeLocal.setText("敏感词词类型缓存 : " + acm.appCacheVersion);
            }
        }
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localTotalVersion == 0 || serverTotalVersion == 0) {
                    mActivity.toast("缓存版本正在获取中, 请耐心等待");
                    return;
                }
                if (localTotalVersion == serverTotalVersion) {
                    mActivity.toast("本地缓存已是最新");
                    return;
                } else {
                    List<Integer> local = new ArrayList<Integer>();
                    local.add(localArea);
                    local.add(localConfig);
                    local.add(localDic);
                    local.add(localDictype);
                    local.add(localWord);
                    local.add(localWordType);

                    List<Integer> server = new ArrayList<Integer>();
                    server.add(serverArea);
                    server.add(serverConfig);
                    server.add(serverDic);
                    server.add(serverDictype);
                    server.add(serverWord);
                    server.add(serverWordType);

                    for (int i = 0; i < local.size(); i++) {
                        if (local.get(i) != server.get(i)) {
                            differentCount++;
                        }
                    }
                }
                if (localArea != serverArea) {
                    tvAreaLocal.setText("地区信息缓存 : 正在更新...");
                    AppCache.updateAreaCache(mContext, serverArea, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvAreaLocal.setText("地区信息缓存 : " + version);
                            else
                                tvAreaLocal.setText("地区信息缓存 : " + localArea + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {
                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }
                if (localConfig != serverConfig) {
                    tvConfigLocal.setText("系统配置缓存 : 正在更新...");
                    AppCache.updateConfigCache(mContext, serverConfig, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvConfigLocal.setText("系统配置缓存 : " + version);
                            else
                                tvConfigLocal.setText("系统配置缓存 : " + localConfig + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {
                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }
                if (localDictype != serverDictype) {
                    tvDictypeLocal.setText("字典类型缓存 : 正在更新...");
                    AppCache.updateDictTypeCache(mContext, serverDictype, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvDictypeLocal.setText("字典类型缓存 : " + version);
                            else
                                tvDictypeLocal.setText("字典类型缓存 : " + localDictype + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {
                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }
                if (localDic != serverDic) {
                    tvDicLocal.setText("字典信息缓存 : 正在更新...");
                    AppCache.updateDictCache(mContext, serverDic, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvDicLocal.setText("字典信息缓存 : " + version);
                            else
                                tvDicLocal.setText("字典信息缓存 : " + localDic + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {

                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }

                if (localWord != serverWord) {
                    tvWordLocal.setText("敏感字缓存 : 正在更新...");
                    AppCache.updateWordsCache(mContext, serverWord, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvWordLocal.setText("敏感字缓存 : " + version);
                            else
                                tvWordLocal.setText("敏感字缓存 : " + localWord + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {
                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }

                if (localWordType != serverWordType) {
                    tvWordLocal.setText("敏感词词类型缓存 : 正在更新...");
                    AppCache.updateSensitiveWordsType(mContext, serverWord, new AppCache.UpdateCallback() {
                        @Override
                        public void onComplete(boolean isSuccess, int version) {
                            if (isSuccess)
                                tvWordLocal.setText("敏感词词类型缓存 : " + version);
                            else
                                tvWordLocal.setText("敏感词词类型缓存 : " + localWord + " (更新失败)");

                            if (!isSuccess)
                                isUpdateSuccess = false;
                            differentCount--;
                            if (differentCount == 0) {
                                if (isUpdateSuccess) {
                                    AppCache.updateAllCache(mContext, serverTotalVersion);
                                    tvTotalLocal.setText("缓存总版本 : " + serverTotalVersion);
                                } else
                                    tvTotalLocal.setText("缓存总版本 : " + localTotalVersion + " (更新失败)");
                            }
                        }
                    });
                }

            }
        });
        call();
    }


    private void call() {

        tvTotal.setText("缓存总版本 : 正在获取...");
        tvArea.setText("地区信息缓存 : 正在获取...");
        tvConfig.setText("系统配置缓存 : 正在获取...");
        tvDictype.setText("字典类型缓存 : 正在获取...");
        tvDic.setText("字典信息缓存 : 正在获取...");
        tvWord.setText("敏感字缓存 : 正在获取...");
        tvWordType.setText("敏感词词类型缓存 : 正在获取...");

        NetHelper.getInstance().getBaseAppCache(new StringDialogCallback(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.toast(getResources().getString(R.string.internet_fault));
                setError();
            }

            @Override
            public void onResponse(IMsg iMsg) {
                try {
                    if (iMsg.isSucceed()) {

                        List<AppCacheModel> list = AppCacheModel.loadList(iMsg);
                        for (AppCacheModel acm : list) {
                            if (acm.appCacheId.equals("AllCache")) {
                                serverTotalVersion = acm.appCacheVersion;
                                tvTotal.setText("缓存总版本 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("AreaCache")) {
                                serverArea = acm.appCacheVersion;
                                tvArea.setText("地区信息缓存 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("ConfigurationCache")) {
                                serverConfig = acm.appCacheVersion;
                                tvConfig.setText("系统配置缓存 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("DictionaryCache")) {
                                serverDictype = acm.appCacheVersion;
                                tvDictype.setText("字典类型缓存 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("DictionaryTpyeCache")) {
                                serverDic = acm.appCacheVersion;
                                tvDic.setText("字典信息缓存 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("SensitiveWordsCache")) {
                                serverWord = acm.appCacheVersion;
                                tvWord.setText("敏感字缓存 : " + acm.appCacheVersion);
                            }
                            if (acm.appCacheId.equals("SensitiveWordsType")) {
                                serverWordType = acm.appCacheVersion;
                                tvWordType.setText("敏感词词类型缓存 : " + acm.appCacheVersion);
                            }
                        }
                    } else {
                        setError();
                        mActivity.toast(iMsg.getMsg());
                    }
                } catch (Exception e) {
                    setError();
                    Log.log("", e);
                }
            }
        });
    }

    private void setError() {
        tvTotal.setText("缓存总版本 : 获取失败");
        tvArea.setText("地区信息缓存 : 获取失败");
        tvConfig.setText("系统配置缓存 : 获取失败");
        tvDictype.setText("字典类型缓存 : 获取失败");
        tvDic.setText("字典信息缓存 : 获取失败");
        tvWord.setText("敏感字缓存 : 获取失败");
        tvWordType.setText("敏感词词类型缓存 : 获取失败");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mActivity = (DebugDetailActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
