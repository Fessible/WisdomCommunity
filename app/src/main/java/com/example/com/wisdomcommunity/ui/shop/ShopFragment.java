package com.example.com.wisdomcommunity.ui.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.ShopContract;
import com.example.com.wisdomcommunity.ui.search.SearchFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.MultipleRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshWizard;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.EmptyDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_HEADER;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/1/16.
 */

public class ShopFragment extends BaseFragment implements ShopContract.View {
    public static final String TAG_SHOP_FRAGMENT = "SHOP_FRAGMENT";
    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_GOODS_ID = "goods_id";
    public static final String KEY_SHOP_NAME = "shop_name";
    public static final String KEY_GOODS_NAME = "goods_name";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_view)
    EditText searchView;

    @BindView(R.id.multiple_refresh_layout)
    MultipleRefreshLayout multipleRefreshLayout;

    private ShopPresenter presenter;
    private ShopAdapter shopAdapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        multipleRefreshLayout.setRefreshWizard(new SwipeRefreshWizard(getContext(), multipleRefreshLayout));
        multipleRefreshLayout.setOnRefreshListener(onRefreshListener);


        presenter = new ShopPresenter(getContext(), ShopFragment.this);
        presenter.loadShopList(false);

        searchView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        shopAdapter = new ShopAdapter(getContext());
        recyclerView.setAdapter(shopAdapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .typeBetween(VIEW_HEADER, VIEW_STANDARD, new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build())
                .typeBetween(VIEW_STANDARD, VIEW_STANDARD, new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build())
                .build());
        shopAdapter.setCallback(callback);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.loadShopList(false);
        }
    };

    private ShopAdapter.Callback callback = new ShopAdapter.Callback() {
        @Override
        public void onCallback(String value, String name, int type) {
            switch (type) {
                case VIEW_HEADER://店铺
                    Bundle shopArgs = new Bundle();
                    shopArgs.putString(KEY_SHOP_ID, value);
                    shopArgs.putString(KEY_SHOP_NAME, name);
                    IntentUtil.startTemplateActivity(ShopFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
                    break;
                case VIEW_STANDARD://商品
                    Bundle goodsArgs = new Bundle();
                    goodsArgs.putString(KEY_GOODS_ID, value);
                    goodsArgs.putString(KEY_GOODS_NAME, name);
                    IntentUtil.startTemplateActivity(ShopFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
                    break;
            }
        }
    };

    @Override
    protected void destroyView() {
        if (shopAdapter != null) {
            shopAdapter.destroy();
        }
    }

    private void showMultipleEmptyLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showEmpty();
        }
    }

    private void showMultipleErrorLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showError();
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
    public void onLoadShopListSuccess(List<ShopList> shopList) {
        if (shopList != null) {
            showMultipleContentLayout();
            shopAdapter.setData(shopList);
            shopAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.search_view)
    public void search(){
        IntentUtil.startTemplateActivity(ShopFragment.this, SearchFragment.class, SearchFragment.TAG_SEARCH_FRAGMENT);
    }

    @Override
    public void onLoadShopListFailure(String msg) {
        showShortToast(msg);
        showMultipleErrorLayout();
    }
}
