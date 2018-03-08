package com.example.com.wisdomcommunity.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.home.Banner;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.HomeContract;
import com.example.com.wisdomcommunity.ui.shop.ShopFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

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

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.banner)
    BGABanner banner;

    private HomePresenter presenter;

    private List<String> imgUrlList = new ArrayList<>();
    private List<String> imgTipList = new ArrayList<>();
    private List<String> imgIDList = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        presenter = new HomePresenter(getContext(), HomeFragment.this);
        presenter.loadRecomends(false);
        presenter.loadBanners(false);
        adapter = new HomeAdapter(getContext());
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setCallback(callback);
    }

    private HomeAdapter.Callback callback = new HomeAdapter.Callback() {
        @Override
        public void onGoodsItem(String goodsID, String goodsName) {
            Bundle goodsArgs = new Bundle();
            goodsArgs.putString(KEY_GOODS_ID, goodsID);
            goodsArgs.putString(KEY_GOODS_NAME, goodsName);
            IntentUtil.startTemplateActivity(HomeFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
        }
    };

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


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onUnauthorized() {

    }

    @Override
    public void onLoadRecommendSuccess(List<Recommend> recommendList) {
        if (recommendList != null) {
            adapter.setData(recommendList);
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onLoadRecommendFailure(String msg) {
        showShortToast(msg);
    }

    @Override
    public void onLoadBannerSuccess(List<Banner> bannerList) {
        if (bannerList != null) {
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
    }

}
