package com.example.com.wisdomcommunity.util.photo;

import android.content.Context;

import java.io.File;

public abstract class StorageManager {

    public final File getStorageDir(Context context, boolean isPrivate) {
        return getStorageDir(context, 0, isPrivate);
    }

    public abstract File getStorageDir(Context context, int index, boolean isPrivate);

}
