package com.example.com.wisdomcommunity.localsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.ShopDetail;

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
    private static final String KEY_SHOP = "shop_detail";
    private static final String KEY_SHOP_ID = "shop_ID";

    //存储购物清单
    public static void setShopCart(Context context, HashMap<String, OrderDetail.Order> hashMap) {
        saveObject(context, KEY_CART, hashMap);
    }

    //获取购物清单
    public static HashMap<String, OrderDetail.Order> getShopCart(Context context) {
        return getObject(context, KEY_CART);
    }

    //存储店铺信息
    public static void setShop(Context context, ShopDetail shopDetail) {
        saveObject(context, KEY_SHOP, shopDetail);
    }

    //获取店铺信息
    public static ShopDetail getShop(Context context) {
        return getObject(context, KEY_SHOP);
    }

    public static void setCount(Context context, int count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_NUM, count);
        editor.apply();
    }

    public static int getCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_NUM, 0);
    }

    public static void setShopId(Context context, String shopId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOP_ID, shopId);
        editor.apply();
    }

    public static String getShopId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SHOP_ID, "");
    }


    public static void setTotalPrice(Context context, String price) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOTAL_PRICE, price);
        editor.apply();
    }

    public static String getTotalPrice(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOTAL_PRICE, "");
    }

    //清除所有内容
    public static void clear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.clear().apply();
    }

    //将对象转化成字符串
    private static <T extends Object> String ObjectToString(T object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        String strObject = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(object);

            //通过Base64将字节码转换成Base64编码保存在String中
            strObject = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strObject;
    }

    //存储对象
    private static <T extends Object> void saveObject(Context context, String key, T object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(key, ObjectToString(object));
        sharedPrefEditor.apply();
    }

    //将字符串转换为对象
    private static <T extends Object> T stringToObject(String result) {
        T object = null;
        byte[] bytes = Base64.decode(result, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = (T) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    //读取对象
    private static <T extends Object> T getObject(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return stringToObject(sharedPreferences.getString(key, ""));
    }


}
