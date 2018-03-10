package com.example.com.wisdomcommunity.util.photo;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

public final class PathUtils implements PathConst {

    private static final StorageManager sStorageManager = Platform.findPlatform();
    private static String sRtlPath;

    private PathUtils() {
        super();
    }

    public static void setAppRtlPath(String rtlPath) {
        if (!checkFileName(rtlPath)) {
            throw new IllegalArgumentException("rtl-path is illegal.");
        }
        sRtlPath = rtlPath;
    }

    private static String getAppRtlPath(Context context) {
        return !TextUtils.isEmpty(sRtlPath) ? sRtlPath : context.getPackageName();
    }

    private static File makeDir(Context context, boolean isPrivate) {
        File storageDir = sStorageManager.getStorageDir(context, isPrivate);
        File appDir = new File(storageDir, PathUtils.getAppRtlPath(context));
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    private static File makeSubDir(Context context, boolean isPrivate, String subPath) {
        if (!checkFileName(subPath)) {
            throw new IllegalArgumentException("sub-path is illegal.");
        }
        File appDir = PathUtils.makeDir(context, isPrivate);
        File subDir = new File(appDir, subPath);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        return subDir;
    }

    public static File getFontDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_FONT);
    }

    public static File getCrashDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_CRASH);
    }

    public static File getDnsDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_DNS);
    }

    public static File getCacheDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_CACHE);
    }

    public static File getImageDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_IMAGE);
    }

    public static File getGalleryDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_GALLERY);
    }

    public static File getAppDir(Context context) {
        return PathUtils.makeSubDir(context, false, PATH_SUB_DIR_APP);
    }

    public static File makeSubDir(Context context, String subPath) {
        return PathUtils.makeSubDir(context, false, subPath);
    }

    private static boolean checkFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        if (fileName.length() > 255) {
            return false;
        }
        return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }
}
