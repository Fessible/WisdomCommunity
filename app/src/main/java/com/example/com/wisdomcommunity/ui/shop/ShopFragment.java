package com.example.com.wisdomcommunity.ui.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.ShopContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.EmptyDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_HEADER;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/1/16.
 */

public class ShopFragment extends BaseFragment implements ShopContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ShopPresenter presenter;
    private ShopAdapter shopAdapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_layout;
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        presenter = new ShopPresenter(getContext(), ShopFragment.this);
        presenter.loadShopList(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        shopAdapter = new ShopAdapter(getContext());
        recyclerView.setAdapter(shopAdapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .typeBetween(VIEW_HEADER, VIEW_STANDARD, new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build())
                .typeBetween(VIEW_STANDARD, VIEW_STANDARD, new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build())
                .build());
    }

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
    public void onLoadShopListSuccess(List<ShopList> shopList) {
        if (shopList != null) {
            shopAdapter.setData(shopList);
            shopAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoadShopListFailure(String msg) {
        showShortToast(msg);
    }
}
