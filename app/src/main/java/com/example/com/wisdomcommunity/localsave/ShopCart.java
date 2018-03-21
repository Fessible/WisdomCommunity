package com.example.com.wisdomcommunity.localsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.com.support_business.domain.order.OrderDetail;

import java.util.HashMap;

/**
 * Created by rhm on 2018/3/21.
 * 存储购物车信息
 *
 */

public class ShopCart {
    //文件名称
    private static final String SHARED_PREF_NAME = "shop_cart_pref";
    private static final String KEY_CART = "shop_cart";

    public static void setShopCart(Context context, HashMap<String,OrderDetail> hashMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
