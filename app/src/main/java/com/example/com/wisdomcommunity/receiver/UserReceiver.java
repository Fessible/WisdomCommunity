package com.example.com.wisdomcommunity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import static com.example.com.wisdomcommunity.MainActivity.ACTION_USER_CHANGED;

/**
 * Created by rhm on 2018/3/21.
 */

public abstract class UserReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_USER_CHANGED)) {
            onUserChanged();
        }
    }

    protected abstract void onUserChanged();
}
