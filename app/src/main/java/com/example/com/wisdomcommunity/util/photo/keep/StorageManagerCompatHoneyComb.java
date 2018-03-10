//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.com.wisdomcommunity.util.photo.keep;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.storage.StorageManager;
import java.lang.reflect.Method;

@TargetApi(11)
class StorageManagerCompatHoneyComb {
    StorageManagerCompatHoneyComb() {
    }

    public static String[] getVolumePaths(Context context) {
        String[] paths = null;

        try {
            StorageManager manager = (StorageManager)context.getSystemService("storage");
            Class<?> clazz = StorageManager.class;
            Method method = clazz.getMethod("getVolumePaths", new Class[0]);
            method.setAccessible(true);
            Object invoke = method.invoke(manager, new Object[0]);
            if(invoke != null && invoke instanceof String[]) {
                paths = (String[])((String[])invoke);
            }
        } catch (NoSuchMethodException var6) {
            var6.printStackTrace();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return paths;
    }
}
