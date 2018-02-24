package com.example.com.support_business.api;

import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.domain.login.Register;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.support_business.domain.personal.Adress;
import com.example.com.support_business.domain.personal.HeadImage;
import com.example.com.support_business.domain.personal.Version;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.support_business.module.Entity;
import com.example.com.support_business.module.ListEntity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.support_business.params.LoginParams;
import com.example.com.support_business.params.PersonParams;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * 网络请求接口
 * Created by rhm on 2018/1/16.
 */

interface CommunityApi {
    /*****************
     *  个人中心
     *****************/
    //地址管理
    @GET("personal/address/{userId}")
    public Observable<Response<ListEntity<Adress>>> address(@Path("userId") String userId);

    //头像编辑
    @Multipart
    @POST("personal/headImage")
    public Observable<Response<ResultEntity<HeadImage>>> headImage(@Part MultipartBody.Part headImage);

    //版本更新
    @GET("personal/appVersion/{currentVersion}")
    public Observable<Response<ResultEntity<Version>>> version(@Path("currentVersion") String currentVersion);

    //编辑个人信息
    @POST("personal/postUserInfo")
    public Observable<Response<Entity>> edit(@Body PersonParams params);

    //获取个人信息
    @GET("personal/userInfo/{userId}")
    public Observable<Response<PersonParams>> info(@Path("userId") String userId);

    /*************
     * 主页
     *************/
    //商品推荐
    @GET("home/recommend")
    public Observable<Response<ListEntity<Recommend>>> recommend();
//    public rx.Observable<Response<ListEntity<Recommend>>> recommend();


    //标题栏
    @GET("home/banner")
    public Observable<Response<ListEntity<Banner>>> banner();

    /*************
     * 店铺
     ************/
    //商品详情
    @GET("shop/goods/{goodsId}")
    public Observable<Response<ResultEntity<GoodsDetail>>> goods(@Path("goodsId") String goodsId);

    //店铺列表
    @GET("shop/list")
    public Observable<Response<ListEntity<ShopList>>> shopList();

    //店铺详情
    @GET("shop/details/{shopId}")
    public Observable<Response<ResultEntity<ShopDetail>>> shopDetail(@Path("shopId") String shopId);

    /*************
     * 登录
     *************/
    //修改密码
    @POST("login/forgetPassword")
    public Observable<Response<Entity>> forgetPassword(@Body String newPassword, @Body LoginParams params);

    //注册
    @POST("login/register")
    public Observable<Response<ResultEntity<Register>>> register(@Body LoginParams params);

    //登录
    @POST("login/login")
    public Observable<Response<Entity>> login(@Body LoginParams params);

    /*************
     * 订单
     ************/
    //我的订单
    @GET("order/record")
    public Observable<Response<ListEntity<OrderRecord>>> record();

    //订单提交
    @POST("order/add")
    public Observable<Response<Entity>> add(@Body OrderDetail order);

    //订单详情
    @GET("order/details/{orderId}")
    public Observable<Response<ResultEntity<OrderDetail>>> detail(@Path("orderId") String orderId);
}
