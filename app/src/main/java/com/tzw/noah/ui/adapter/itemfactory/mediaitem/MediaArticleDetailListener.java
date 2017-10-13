package com.tzw.noah.ui.adapter.itemfactory.mediaitem;

import com.tzw.noah.models.MediaArticle;
import com.tzw.noah.models.MediaComment;

/**
 * Created by yzy on 2017/8/30.
 */

public interface MediaArticleDetailListener {
    void onItemClick(int position, Object o);
    void onLikeClick(int position, Object o);
    void onWebViewLoadComplete();
    void onLikeMemberClick(int position, Object o);
    void onCommentClick(int adapterPosition, MediaComment data);
    void onComplaintClick();

    void toggledFullscreen(boolean fullscreen);

    void onKeywordClick(String key, String keyId);
}
