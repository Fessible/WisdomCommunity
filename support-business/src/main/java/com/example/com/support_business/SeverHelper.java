package com.example.com.support_business;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhm on 2018/1/16.
 */

public class SeverHelper {

    //    private String baseUrl = "http://192.168.98.147:8080/";
    private String baseUrl = "http://192.168.191.1:8080/";

    //    private String baseUrl = "http://rapapi.org/mockjsdata/30782/community/";
//    private String baseUrl="http://rap2api.taobao.org/app/mock/3614/GET/";
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

        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//自定义OkHttpClient
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
//添加拦截器
        okHttpClient.addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
