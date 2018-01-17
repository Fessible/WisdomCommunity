package com.example.com.wisdomcommunity.ui.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rhm on 2018/1/16.
 */

public class ShopFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_layout;
    }

    //test
    private List<ShopList> shopLists = new ArrayList<>();

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        //initData();
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(getContext());
        recyclerView.setAdapter(shopAdapter);
        shopAdapter.setData(shopLists);
        shopAdapter.notifyDataSetChanged();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            ShopList shopList = new ShopList();
            shopList.shopName = "鲜果店";
            Goods goods = new Goods();
            goods.goodsName = "金桔";
            List<Goods> goodsList = new ArrayList<>();
            goodsList.add(goods);
            shopList.goodsList = goodsList;
            shopLists.add(shopList);
        }
    }
}
