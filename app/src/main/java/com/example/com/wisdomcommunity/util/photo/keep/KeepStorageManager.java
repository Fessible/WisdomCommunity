//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.com.wisdomcommunity.util.photo.keep;

import android.content.Context;
import android.os.Environment;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.example.com.wisdomcommunity.util.photo.StorageManager;

import java.io.File;

public class KeepStorageManager extends StorageManager {
    public KeepStorageManager() {
    }

    public File getStorageDir(Context context, int index, boolean isPrivate) {
        File storageDir = null;
        if(!isPrivate && this.checkExternalStoragePermissions(context)) {
            StorageManagerCompat manager = StorageManagerCompat.get(context);
            String[] paths = manager.getVolumePaths();
            if(paths != null) {
                String storageDirPath = null;
                int length = paths.length;
                if(length > 0 && index >= 0 && length > index) {
                    storageDirPath = paths[index];
                }

                if(TextUtils.isEmpty(storageDirPath) && length > 0) {
                    storageDirPath = paths[0];
                }

                if(!TextUtils.isEmpty(storageDirPath)) {
                    storageDir = new File(storageDirPath);
                }
            }

            if(storageDir == null) {
                storageDir = this.hasExternalStorage(context)?this.getSDCardStorageDir(context):this.getMemoryStorageDir(context);
            }

            if(storageDir == null) {
                storageDir = this.getMemoryStorageDir(context);
            }
        } else {
            storageDir = this.getMemoryStorageDir(context);
        }

        return storageDir;
    }

    private boolean checkExternalStoragePermissions(Context context) {
        return VERSION.SDK_INT < 23?true:context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0 && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0;
    }

    private boolean hasExternalStorage(Context context) {
        return Environment.getExternalStorageState().equals("mounted");
    }

    private File getSDCardStorageDir(Context context) {
        return Environment.getExternalStorageDirectory();
    }

    private File getMemoryStorageDir(Context context) {
        return context.getFilesDir();
    }
}
