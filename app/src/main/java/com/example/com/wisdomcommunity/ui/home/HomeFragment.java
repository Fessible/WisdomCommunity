package com.example.com.wisdomcommunity.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.Constants;
import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Home;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.domain.home.RecommendGoods;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.localsave.AccountSetUp;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.HomeContract;
import com.example.com.wisdomcommunity.ui.category.CategoryFragment;
import com.example.com.wisdomcommunity.ui.login.LoginFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.MultipleRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshWizard;
import com.example.com.wisdomcommunity.view.itemdecoration.EmptyDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

import static android.app.Activity.RESULT_OK;
import static com.example.com.support_business.Constants.CATEGORY_DRINK;
import static com.example.com.support_business.Constants.CATEGORY_FLOWER;
import static com.example.com.support_business.Constants.CATEGORY_FOOD;
import static com.example.com.support_business.Constants.CATEGORY_FRUIT;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;

/**
 * 主页
 * Created by rhm on 2018/1/16.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    public static final String TAG_HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final int REQUEST_LOGIN = 1;
    public static final String CATEGORY = "category";
    public static final int HOME_TAG = 1;
    public static final String KEY_HOME = "home";
    public static final String KEY_RECOMMEND = "recommend";
    public static final String KEY_RECOMMEND_GOODS = "recommend_goods";

    @BindView(R.id.multiple_refresh_layout)
    MultipleRefreshLayout multipleRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.banner)
    BGABanner banner;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.second_kill)
    ImageView secondKill;

    @BindView(R.id.recommend)
    ImageView recommend;

    private HomeContract.Presenter presenter;

    private List<String> imgUrlList = new ArrayList<>();
    private List<String> imgTipList = new ArrayList<>();
    private List<String> imgIDList = new ArrayList<>();
    private HomeAdapter adapter;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private RecommendGoods goodskill;
    private RecommendGoods goodsRecommend;


    @Override
    public int getResLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CharSequence phone = AccountSetUp.getPhone(getContext());
        CharSequence password = AccountSetUp.getPassword(getContext());
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            IntentUtil.startTemplateActivityForResult(HomeFragment.this, LoginFragment.class, LoginFragment.TAG_Login_FRAGMENT, REQUEST_LOGIN);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        //声明LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        multipleRefreshLayout.setRefreshWizard(new SwipeRefreshWizard(getContext(), multipleRefreshLayout));
        multipleRefreshLayout.setOnRefreshListener(onRefreshListener);

        presenter = new HomePresenter(getContext(), HomeFragment.this);
        presenter.loadRecomends(false);
        presenter.loadBanners(false);
        adapter = new HomeAdapter(getContext());
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setCallback(callback);

        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext()).typeSelf(0, new EmptyDecor()).build());
    }

    private HomeAdapter.Callback callback = new HomeAdapter.Callback() {
        @Override
        public void onGoodsItem(Recommend recommend) {
            Bundle goodsArgs = new Bundle();
            goodsArgs.putInt(KEY_HOME,HOME_TAG);
            goodsArgs.putSerializable(KEY_RECOMMEND_GOODS,recommend);
            goodsArgs.putString(KEY_GOODS_ID, recommend.goodsId);
            goodsArgs.putString(KEY_GOODS_NAME, recommend.name);
            IntentUtil.startTemplateActivity(HomeFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.loadRecomends(false);
            presenter.loadBanners(false);
            mLocationClient.restart();
        }
    };

    private void setData(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY, type);
        IntentUtil.startTemplateActivity(HomeFragment.this, CategoryFragment.class, bundle, CategoryFragment.TAG_CATEGORY_FRAGMENT);
    }

    @OnClick(R.id.food)
    public void food() {
        setData(CATEGORY_FOOD);
    }

    @OnClick(R.id.flower)
    public void flower() {
        setData(CATEGORY_FLOWER);
    }

    @OnClick(R.id.drink)
    public void drink() {
        setData(CATEGORY_DRINK);
    }

    @OnClick(R.id.fruit)
    public void fruit() {
        setData(CATEGORY_FRUIT);
    }

    @Override
    protected void destroyView() {
        if (presenter != null) {
            presenter.destroy();
        }
        if (imgIDList != null) {
            imgIDList.clear();
        }
        if (imgTipList != null) {
            imgTipList.clear();
        }
        if (imgUrlList != null) {
            imgUrlList.clear();
        }
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    private BGABanner.Adapter bannerAdapter = new BGABanner.Adapter() {
        @Override
        public void fillBannerItem(BGABanner banner, View itemView, @Nullable Object model, int position) {
            Glide.with(getContext())
                    .load(model)
                    .apply(new RequestOptions().placeholder(R.drawable.holder).error(R.drawable.holder).dontAnimate().centerCrop())
                    .into((ImageView) itemView);
        }
    };

    private BGABanner.Delegate delegate = new BGABanner.Delegate() {
        @Override
        public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
            String shopId = imgIDList.get(position);
            String shopName = imgTipList.get(position);
            Bundle shopArgs = new Bundle();
            shopArgs.putString(KEY_SHOP_ID, shopId);
            shopArgs.putString(KEY_SHOP_NAME, shopName);
            IntentUtil.startTemplateActivity(HomeFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
        }
    };

    private void showMultipleEmptyLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showEmpty();
        }
    }

    private void showMultipleContentLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.setEnabled(true);
            multipleRefreshLayout.showContentOnly();
        }
    }

    @Override
    public void showProgress() {
        if (multipleRefreshLayout != null) {
            if (!multipleRefreshLayout.isLoading() && !multipleRefreshLayout.isRefreshing()) {
                multipleRefreshLayout.setEnabled(false);
                multipleRefreshLayout.showLoading(false);
            }
        }
    }

    @Override
    public void hideProgress() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.setEnabled(true);
            if (multipleRefreshLayout.isLoading()) {
                multipleRefreshLayout.hideLoading();
            }
            if (multipleRefreshLayout.isRefreshing()) {
                multipleRefreshLayout.tryRefreshFinished();
            }
        }
    }

    @Override
    public void onUnauthorized() {

    }

    @Override
    public void onLoadRecommendSuccess(Home home) {
        if (home != null) {
            goodskill = home.secondKill;
            goodsRecommend = home.recommend;

            showMultipleContentLayout();
            Glide.with(getContext())
                    .load(goodskill.goodsUrl)
                    .into(secondKill);


            Glide.with(getContext())
                    .load(goodsRecommend.goodsUrl)
                    .into(recommend);

            adapter.setData(home.list);
            adapter.notifyDataSetChanged();
        }
    }

    //新品推荐
    @OnClick(R.id.recommend)
    public void recommend() {
        Bundle goodsArgs = new Bundle();
        goodsArgs.putInt(KEY_HOME, HOME_TAG);
        goodsArgs.putSerializable(KEY_RECOMMEND, goodsRecommend);
        goodsArgs.putString(KEY_GOODS_ID, goodsRecommend.goodsId);
        goodsArgs.putString(KEY_GOODS_NAME, getString(R.string.new_recommend));
        IntentUtil.startTemplateActivity(HomeFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
    }

    //秒杀
    @OnClick(R.id.second_kill)
    public void SecondKill() {
        Bundle goodsArgs = new Bundle();
        goodsArgs.putInt(KEY_HOME, HOME_TAG);
        goodsArgs.putSerializable(KEY_RECOMMEND, goodskill);
        goodsArgs.putString(KEY_GOODS_ID, goodskill.goodsId);
        goodsArgs.putString(KEY_GOODS_NAME, getString(R.string.second_kill));
        IntentUtil.startTemplateActivity(HomeFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
    }

    @Override
    public void onLoadRecommendFailure(String msg) {

        showShortToast(msg);
        showMultipleErrorLayout();
    }

    private void showMultipleErrorLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showError();
        }
    }

    @Override
    public void onLoadBannerSuccess(List<Banner> bannerList) {

        if (bannerList != null) {
            showMultipleContentLayout();
            imgUrlList.clear();
            imgTipList.clear();
            imgIDList.clear();
            for (int i = 0; i < bannerList.size(); i++) {
                imgUrlList.add(bannerList.get(i).bannerUrl);
                imgTipList.add(bannerList.get(i).shopName);
                imgIDList.add(bannerList.get(i).shopId);
            }
            banner.setData(imgUrlList, imgTipList);
            banner.setAdapter(bannerAdapter);
            //点击事件
            banner.setDelegate(delegate);
        }
    }

    @Override
    public void onLoadBannerFailure(String msg) {
        showShortToast(msg);
        showMultipleEmptyLayout();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            String city = location.getCity();    //获取城市
            address.setText(city);
            locatedCity = city.substring(0, city.length() - 1);
        }
    }

    private String locatedCity = null;

    @OnClick(R.id.address)
    public void address() {

        CityPicker.getInstance()
                .setFragmentManager(getActivity().getSupportFragmentManager())
                .setLocatedCity(new LocatedCity(locatedCity))
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        address.setText(data == null ? String.format("%s市", locatedCity) : String.format("%s市", data.getName()));
                    }

                    @Override
                    public void onLocate() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                CityPicker.getInstance()
                                        .locateComplete(new LocatedCity(locatedCity), LocateState.SUCCESS);
                            }
                        }, 2000);
                    }
                }).show();
    }

    @OnClick(R.id.phone)
    public void phone() {
        String phoneNumber = "057187063728";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            getActivity().finish();
        }
    }
}
