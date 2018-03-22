package com.example.com.wisdomcommunity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import static com.example.com.wisdomcommunity.MainActivity.ACTION_SHOP_CART_CHANGED;
import static com.example.com.wisdomcommunity.MainActivity.ACTION_SHOP_CART_CLEAR;

/**
 * Created by rhm on 2018/3/21.
 */

public abstract class CartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_SHOP_CART_CHANGED)) {
            onShopCartChanged();
        } else if (TextUtils.equals(action, ACTION_SHOP_CART_CLEAR)) {
            onShopCartClear();
        } 
    }

    protected abstract void onShopCartClear();

    protected abstract void onShopCartChanged();
}
