package com.example.com.wisdomcommunity.localsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import com.example.com.support_business.domain.order.OrderDetail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by rhm on 2018/3/21.
 * 存储购物车信息
 */

public class ShopCart {
    //文件名称
    private static final String SHARED_PREF_NAME = "shop_cart_pref";
    private static final String KEY_CART = "shop_cart";
    private static final String KEY_NUM = "shop_num";
    private static final String KEY_TOTAL_PRICE = "total_price";

    public static void setShopCart(Context context, HashMap<String, OrderDetail.Order> hashMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CART, saveHashMap(hashMap));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    //将对象变成字符串存储起来
    private static String saveHashMap(HashMap<String, OrderDetail.Order> orderHashMap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(orderHashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(Base64.encode(byteArrayOutputStream.toByteArray(), 1));

    }

    public static HashMap<String, OrderDetail.Order> getShopCart(Context context) {
        HashMap<String, OrderDetail.Order> orderMap = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(KEY_CART, "");
        orderMap = readObjectFromShared(result);
        return orderMap;
    }

    private static HashMap<String, OrderDetail.Order> readObjectFromShared(String result) {
        byte[] base64Bytes = Base64.decode(result.getBytes(), 1);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        HashMap<String, OrderDetail.Order> hashMap = null;
        try {
            ois = new ObjectInputStream(bais);
            hashMap = (HashMap<String, OrderDetail.Order>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hashMap;
    }


    public static void setCount(Context context, int count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_NUM, count);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }


    public static int getCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int count = sharedPreferences.getInt(KEY_NUM, 0);
        return count;
    }


    public static void setTotalPrice(Context context, String price) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOTAL_PRICE, price);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static String getTotalPrice(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOTAL_PRICE, "");
    }

    public static void clear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.clear().commit();
    }
}
