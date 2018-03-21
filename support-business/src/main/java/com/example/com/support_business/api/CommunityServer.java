package com.example.com.support_business.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.example.com.support_business.SeverHelper;
import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Home;
import com.example.com.support_business.domain.login.User;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.support_business.domain.personal.HeadImage;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.support_business.domain.search.Search;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.support_business.module.Entity;
import com.example.com.support_business.module.ListEntity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.support_business.params.ForgetParams;
import com.example.com.support_business.params.LoginParams;
import com.example.com.support_business.params.PersonParams;
import com.example.com.support_business.params.RegisterParams;
import com.example.com.support_business.util.FilenameUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

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
    public void recommend(String compositeTag, boolean refresh, final SSOCallback<ResultEntity<Home>> callback) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("http://39.108.158.246:8080/community/home/recommend").build();
////        Request request = new Request.Builder().url("http://rapapi.org/mockjs/30782/community/home/recommend?").build();
//        Call call = client.newCall(request);
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("http", "onFailure: "+call);
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                Log.d("http", "onResponse: "+response.body().string());
//            }
//        });

        Disposable disposable = communityApi.recommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<Home>>>() {
                    @Override
                    public void accept(Response<ResultEntity<Home>> resultEntityResponse) throws Exception {
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
                //todo
//        Disposable disposable = communityApi.goodsDetail()
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
                //todo 测试
//        Disposable disposable = communityApi.shopDetail()
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
     * 修改地址
     */
    public void editAddress(String composite, Address address, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.editAddress(address)
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


    /**
     * 删除地址
     */
    public void deleteAddress(String composite, String addressId, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.deleteAddress(addressId)
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

    //头像编辑
    public void editHeadImage(String compositeTag, @NonNull File headImageFile, final SSOCallback<ResultEntity<HeadImage>> callback) {
        RequestBody headImage = RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton().getMimeTypeFromExtension(FilenameUtils.getExtension(headImageFile.getAbsolutePath()))), headImageFile);
        Disposable disposable = communityApi.headImage(MultipartBody.Part.createFormData("headImage", headImageFile.getName(), headImage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<HeadImage>>>() {
                    @Override
                    public void accept(Response<ResultEntity<HeadImage>> resultEntityResponse) throws Exception {
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
        add(compositeTag, disposable);
    }

    //个人信息编辑
    public void editInfo(String composite, PersonParams params, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.edit(params)
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

    /**
     * 搜索
     */
    public void search(String composite, String search, final SSOCallback<ListEntity<Search>> callback) {
        Disposable disposable = communityApi.search(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ListEntity<Search>>>() {
                    @Override
                    public void accept(Response<ListEntity<Search>> listEntityResponse) throws Exception {
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
     * 登录
     */
    public void login(String compositeTag, String phone, String password, final SSOCallback<ResultEntity<User>> callback) {
        Disposable disposable = communityApi.login(new LoginParams(phone,password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResultEntity<User>>>() {
                    @Override
                    public void accept(Response<ResultEntity<User>> resultEntityResponse) throws Exception {
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
        add(compositeTag, disposable);
    }

    /**
     * 注册
     */
    public void register(String composite, String userName, String phone, String password, String sms, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.register(new RegisterParams(userName, phone, sms, password))
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

    /**
     * 忘记密码
     */
    public void forgetPassword(String composite, String smsCode, String phone, String password, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.forgetPassword(new ForgetParams(smsCode, phone, password))
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

    /**
     * 获取验证码
     */
    public void sms(String composite, String phone, final SSOCallback<Entity> callback) {
        Disposable disposable = communityApi.sms(phone)
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
