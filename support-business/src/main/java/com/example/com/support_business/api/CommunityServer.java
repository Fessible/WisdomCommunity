package com.example.com.support_business.api;

import android.content.Context;
import android.util.Log;

import com.example.com.support_business.SeverHelper;
import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.support_business.module.Entity;
import com.example.com.support_business.module.ListEntity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.support_business.params.PersonParams;

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

    /**
     * 主页
     */
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
    public void banner(String composite, boolean refresh, final SSOCallback<ListEntity<Banner>> callback) {
        Disposable disposable = communityApi.banner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<Banner>>>() {
                    @Override
                    public void accept(Response<ListEntity<Banner>> listEntityResponse) throws Exception {
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
        add(composite, disposable);
    }

    /**
     * 店铺
     */
    //店铺列表
    public void shopList(String composite, boolean refresh, final SSOCallback<ListEntity<ShopList>> callback) {
        Disposable disposable = communityApi.shopList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<ShopList>>>() {
                    @Override
                    public void accept(Response<ListEntity<ShopList>> listEntityResponse) throws Exception {
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
        add(composite, disposable);
    }

    //商品详情
    public void goodsDetail(String composite, String goodsId, final boolean refresh, final SSOCallback<ResultEntity<GoodsDetail>> callback) {
        Disposable disposable = communityApi.goodsDetail(goodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<GoodsDetail>>>() {
                    @Override
                    public void accept(Response<ResultEntity<GoodsDetail>> resultEntityResponse) throws Exception {
                        if (resultEntityResponse != null) {
                            if (resultEntityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, resultEntityResponse);
                            } else if (resultEntityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
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
        add(composite, disposable);
    }

    //店铺详情
    public void shopDetail(String composite, boolean refesh, String shopId, final SSOCallback<ResultEntity<ShopDetail>> callback) {
        Disposable disposable = communityApi.shopDetail(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<ShopDetail>>>() {
                    @Override
                    public void accept(Response<ResultEntity<ShopDetail>> resultEntityResponse) throws Exception {
                        if (resultEntityResponse != null) {
                            if (resultEntityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, resultEntityResponse);
                            } else if (resultEntityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
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
        add(composite, disposable);
    }

    //订单
    public void record(String composite, boolean refresh, final SSOCallback<ListEntity<OrderRecord>> callback) {
        Disposable disposable = communityApi.record()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<OrderRecord>>>() {
                    @Override
                    public void accept(Response<ListEntity<OrderRecord>> listEntityResponse) throws Exception {
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
        add(composite, disposable);
    }

    //订单详情
    public void orderDetail(String composite, boolean refresh, String orderId, final SSOCallback<ResultEntity<OrderDetail>> callback) {
        final Disposable disposable = communityApi.detail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<OrderDetail>>>() {
                    @Override
                    public void accept(Response<ResultEntity<OrderDetail>> resultEntityResponse) throws Exception {
                        if (resultEntityResponse != null) {
                            if (resultEntityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, resultEntityResponse);
                            } else if (resultEntityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
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
        add(composite, disposable);
    }

    //请求地址
    public void address(String composite, boolean refesh, String userId, final SSOCallback<ListEntity<Address>> callback) {
        Disposable disposable = communityApi.address(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<Address>>>() {
                    @Override
                    public void accept(Response<ListEntity<Address>> listEntityResponse) throws Exception {
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
        add(composite, disposable);
    }

    /**
     * 个人中心
     */

    //获取个人信息
    public void info(String composite, boolean refresh, String userId, final SSOCallback<ResultEntity<Info>> callback) {
        Disposable disposable = communityApi.info(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<Info>>>() {
                    @Override
                    public void accept(Response<ResultEntity<Info>> resultEntityResponse) throws Exception {
                        if (resultEntityResponse != null) {
                            if (resultEntityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, resultEntityResponse);
                            } else if (resultEntityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
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
        add(composite, disposable);
    }

    //意见反馈
    public void suggestion(String composite, String suggestion, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.suggestion(suggestion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Entity>>() {
                    @Override
                    public void accept(Response<Entity> entityResponse) throws Exception {
                        if (entityResponse != null) {
                            if (entityResponse.isSuccessful()) {
                                callOnResponseMethod(callback, entityResponse);
                            } else if (entityResponse.code() == HttpStatus.UNAUTHORIZED.code()) {
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
        add(composite, disposable);
    }
}
