package com.example.com.wisdomcommunity.localsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.example.com.wisdomcommunity.util.CipherUtils;

/**
 * Created by rhm on 2018/3/21.
 */

public class AccountSetUp {

    //文件名称
    private static final String SHARED_PREF_NAME = "account_fixed_setup";

    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERID = "userId";
    private static final String PRIVATE_KEY_FOR_ACCOUNT = "admin";

    private AccountSetUp() {
    }


    public static CharSequence getPhone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString(KEY_PHONE, "");
        return !TextUtils.isEmpty(phone) ? CipherUtils.xorDecode(phone, PRIVATE_KEY_FOR_ACCOUNT) : null;
    }

    public static void setPhone(Context context, CharSequence phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE, CipherUtils.xorEncode(String.valueOf(phone), PRIVATE_KEY_FOR_ACCOUNT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static CharSequence getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        return !TextUtils.isEmpty(password) ? CipherUtils.xorDecode(password, PRIVATE_KEY_FOR_ACCOUNT) : null;
    }

    public static void setPassword(Context context, CharSequence password) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(KEY_PASSWORD, CipherUtils.xorEncode(String.valueOf(password), PRIVATE_KEY_FOR_ACCOUNT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sharedPrefEditor.apply();
        } else {
            sharedPrefEditor.commit();
        }
    }

    //移除密码和用户ID
    public static void clear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.remove(KEY_PASSWORD);
        sharedPrefEditor.remove(KEY_USERID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sharedPrefEditor.apply();
        } else {
            sharedPrefEditor.commit();
        }
    }

    //存储用户ID
    public static void setUserId(Context context, String userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(KEY_USERID, CipherUtils.xorEncode(String.valueOf(userId), PRIVATE_KEY_FOR_ACCOUNT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sharedPrefEditor.apply();
        } else {
            sharedPrefEditor.commit();
        }
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String password = sharedPreferences.getString(KEY_USERID, "");
        return !TextUtils.isEmpty(password) ? CipherUtils.xorDecode(password, PRIVATE_KEY_FOR_ACCOUNT) : null;
    }
}
