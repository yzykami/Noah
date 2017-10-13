package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzw.noah.R;
import com.tzw.noah.logger.Log;
import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.utils.Utils;

import java.util.List;

import butterknife.BindView;
import cn.lankton.flowlayout.FlowLayout;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;

public class MediaArticleKeywordItemFatory extends AssemblyRecyclerItemFactory<MediaArticleKeywordItemFatory.GalleryItem> {

    private MediaArticleDetailListener mMediaListListener;
    private int itemSize;

    public MediaArticleKeywordItemFatory(MediaArticleDetailListener mMediaListListener) {
        this.mMediaListListener = mMediaListListener;
    }

    @Override
    public boolean isTarget(Object o) {
        if (o instanceof MediaArticle)
            return ((MediaArticle) o).isKeyword();
        return false;
    }

    @Override
    public GalleryItem createAssemblyItem(ViewGroup viewGroup) {

        return new GalleryItem(R.layout.media_article_flowlayout, viewGroup);
    }

    public class GalleryItem extends BindAssemblyRecyclerItem<MediaArticle> {
        @BindView(R.id.container)
        FlowLayout container;
//        @BindView(R.id.tv_title)
//        TextView tv_title;
//        @BindView(R.id.tv_author)
//        TextView tv_author;

        Context mContext;

        public GalleryItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        String[] keywords = getData().getKeywords();
//                        System.out.println(keywords);
//                    }catch (Exception e)
//                    {
//                        Log.log("a",e);
//                    }
                }
            });
        }

        @Override
        protected void onSetData(int position, MediaArticle mediaArticle) {
            container.removeAllViews();
            int height = Utils.dp2px(mContext, 24);
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            List<String> keywords = mediaArticle.getKeywords();//new String[]{"哈哈","哈哈补补","哈哈呵呵"};
            List<String> keywordIds = mediaArticle.getKeywordIds();//new String[]{"哈哈","哈哈补补","哈哈呵呵"};
            for (int i =0;i<keywords.size()&&i<keywordIds.size();i++) {
                final String key = keywords.get(i);
                final String keyId = keywordIds.get(i);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(width, height);
                lp.setMargins(0, 0, Utils.dp2px(mContext, 10), 0);
                TextView tv = new TextView(mContext);
                tv.setPadding(Utils.dp2px(mContext, 15), 0, Utils.dp2px(mContext, 15), 0);
                tv.setTextColor(mContext.getResources().getColor(R.color.textDarkGray));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tv.setText(key);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setLines(1);
                tv.setBackgroundResource(R.drawable.bg_gray_fill_round);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mMediaListListener!=null)
                            mMediaListListener.onKeywordClick(key,keyId);
                    }
                });
                container.addView(tv, lp);
            }
        }
    }
}
