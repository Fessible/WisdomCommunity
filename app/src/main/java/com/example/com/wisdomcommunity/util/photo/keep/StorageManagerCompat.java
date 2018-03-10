//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.com.wisdomcommunity.util.photo.keep;

import android.content.Context;
import android.os.Build.VERSION;

class StorageManagerCompat {
    private final StorageManagerCompat.StorageManagerCompatImpl mImpl;
    private final Context mContext;

    public static StorageManagerCompat get(Context context) {
        return new StorageManagerCompat(context);
    }

    private StorageManagerCompat(Context context) {
        this(VERSION.SDK_INT, context);
    }

    private StorageManagerCompat(int apiVersion, Context context) {
        if(apiVersion >= 11) {
            this.mImpl = new StorageManagerCompat.HoneyCombStorageManagerCompatImpl();
        } else {
            this.mImpl = new StorageManagerCompat.EarlyStorageManagerCompatImpl();
        }

        this.mContext = context;
    }

    public String[] getVolumePaths() {
        return this.mImpl.getVolumePaths(this.mContext);
    }

    static class HoneyCombStorageManagerCompatImpl implements StorageManagerCompat.StorageManagerCompatImpl {
        HoneyCombStorageManagerCompatImpl() {
        }

        public String[] getVolumePaths(Context context) {
            return StorageManagerCompatHoneyComb.getVolumePaths(context);
        }
    }

    static class EarlyStorageManagerCompatImpl implements StorageManagerCompat.StorageManagerCompatImpl {
        EarlyStorageManagerCompatImpl() {
        }

        public String[] getVolumePaths(Context context) {
            return null;
        }
    }

    interface StorageManagerCompatImpl {
        String[] getVolumePaths(Context var1);
    }
}
