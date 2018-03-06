package com.example.com.support_business;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhm on 2018/1/16.
 */

public class SeverHelper {

    private String baseUrl = "http://rapapi.org/mockjs/30782/community/";
//    private String baseUrl = "http://39.108.158.246:8080/community/";

    private SeverHelper() {
    }

    private static volatile SeverHelper sInstance;

    public static SeverHelper with() {
        if (sInstance == null) {
            synchronized (SeverHelper.class) {
                if (sInstance == null) {
                    sInstance = new SeverHelper();
                }

            }
        }
        return sInstance;
    }

    //获取到retrofit
    public Retrofit newRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
