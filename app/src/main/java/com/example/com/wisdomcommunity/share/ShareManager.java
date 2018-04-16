package com.example.com.wisdomcommunity.share;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import static com.example.com.wisdomcommunity.share.ShareManager.PlatofrmType.QQ;
import static com.example.com.wisdomcommunity.share.ShareManager.PlatofrmType.Qzone;

/**
 * Created by rhm on 2018/1/8.
 */

public class ShareManager {
    private static ShareManager manager = null;

    /**
     * 要分享的平台
     */
    private Platform currentPlatform;

    private ShareManager() {
    }

    public static ShareManager getManager() {
        if (manager == null) {
            synchronized (ShareManager.class) {
                if (manager == null) {
                    manager = new ShareManager();
                }
            }
        }
        return manager;
    }

    /**
     * 分享数据到不同平台
     */
    public void shareData(ShareData shareData, PlatformActionListener listener) {
        switch (shareData.mPlatformType) {
            case QQ:
                currentPlatform = ShareSDK.getPlatform(QQ.name());
                break;
            case Qzone:
                currentPlatform = ShareSDK.getPlatform(Qzone.name());
                break;
        }
        currentPlatform.setPlatformActionListener(listener);
        currentPlatform.share(shareData.mShareParams);

    }

    public enum PlatofrmType {
        WeChat, Weibo, QQ, Qzone, Comment
    }


}
