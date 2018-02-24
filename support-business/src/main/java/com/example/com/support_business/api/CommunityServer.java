package com.example.com.support_business.api;

import android.content.Context;
import android.util.Log;

import com.example.com.support_business.SeverHelper;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.module.ListEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;

/**
 * 网络请求处理
 * Created by rhm on 2018/1/16.
 */

public class CommunityServer extends RestyServer {
    private Context context;
    private CommunityApi communityApi;

    private static volatile CommunityServer sInstance;

    public static synchronized CommunityServer with(Context context) {
        if (sInstance == null) {
            sInstance = new CommunityServer(context);
        }
        return sInstance;
    }

    private CommunityServer(Context context) {
        super(context);
        this.context = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        communityApi = SeverHelper.with().newRetrofit().create(CommunityApi.class);
    }

    //商品推荐
    public void recommend(String compositeTag, boolean refresh, final SSOCallback<ListEntity<Recommend>> callback) {
        Disposable disposable = communityApi.recommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<Recommend>>>() {
                    @Override
                    public void accept(Response<ListEntity<Recommend>> listEntityResponse) throws Exception {
                        if (listEntityResponse != null) {
                            if (listEntityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, listEntityResponse);
                            } else if (listEntityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
                                callOnUnauthorizedMethod(callback);
                            } else {
                                throwNullOrFailureResponse();
                            }
                        } else {
                            throwNullOrFailureResponse();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callOnFailureMethod(callback, throwable);
                    }
                });
        add(compositeTag, disposable);
    }

    //标题栏
}
