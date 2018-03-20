package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment.KEY_GOOD_ORDER;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_GOODS_NUM;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_GOODS_URL;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_POSITION;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.REQUEST_GOODS;

/**
 * Created by rhm on 2018/3/19.
 */

public class ShopGoodsFragment extends BaseFragment {
    private View view;
    private Bundle savedInstanceState;
    private ShopDetail shopDetail;
    private ShopDetailAdapter adapter;
    private GoodsCallback goodsCallback;
    private int clickCount;

    public void setShopDetail(ShopDetail shopDetail) {
        this.shopDetail = shopDetail;
        initView(view, savedInstanceState);
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_goods;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        this.view = view;
        this.savedInstanceState = savedInstanceState;
        init();
    }

    private void init() {
        if (shopDetail != null) {
            initRecyclerView(shopDetail);
        }
    }

    //初始化recyclerview
    private void initRecyclerView(ShopDetail shopDetail) {
        adapter = new ShopDetailAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        recyclerView.setAdapter(adapter);
        adapter.setData(shopDetail.goodsList);
        adapter.notifyDataSetChanged();
        adapter.setCallback(callback);
    }

    private ShopDetailAdapter.Callback callback = new ShopDetailAdapter.Callback() {
        @Override
        public void onCallback(Goods goods, int num, int position) {
            clickCount = num;
            Bundle goodsArgs = new Bundle();
            goodsArgs.putString(KEY_GOODS_ID, goods.goodsId);
            goodsArgs.putString(KEY_GOODS_NAME, goods.goodsName);
            goodsArgs.putString(KEY_GOODS_URL, goods.goodsUrl);
            goodsArgs.putInt(KEY_GOODS_NUM, num);
            goodsArgs.putInt(KEY_POSITION, position);
            IntentUtil.startTemplateActivityForResult(ShopGoodsFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT, REQUEST_GOODS);
        }

        @Override
        public void onAddPayBack(View v, Goods goods, float price, int num) {
            if (goodsCallback != null) {
                goodsCallback.onAdd(v, goods, price, num);
            }
        }

        @Override
        public void onMinusPayBack(Goods goods, float price, int num) {
            if (goodsCallback != null) {
                goodsCallback.onMinus(goods, price, num);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GOODS) {
                OrderDetail.Order order = (OrderDetail.Order) data.getSerializableExtra(KEY_GOOD_ORDER);
                int position = data.getIntExtra(KEY_POSITION, 0);
                if (clickCount != order.number) {
                    if (goodsCallback != null && adapter != null) {
                        adapter.setNum(order.number, position);
                        Goods goods = new Goods();
                        goods.goodsUrl = order.goodsUrl;
                        goods.goodsId = order.goodsId;
                        goods.remain = order.remain;
                        goods.goodsName = order.goodsName;
                        goodsCallback.onClickItem(goods, Float.valueOf(order.price), order.number, order.number - clickCount);
                    }
                }
            }
        }
    }

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }

    public void changeGoods(List<Goods> goodsList) {
        if (adapter != null) {
            adapter.setData(goodsList);
            adapter.notifyDataSetChanged();
        }
    }

    public void clear() {
        if (adapter != null) {
            adapter.setNum(0);
            adapter.notifyDataSetChanged();
        }
    }

    public void setGoodsCallback(GoodsCallback callback) {
        this.goodsCallback = callback;
    }

    public interface GoodsCallback {
        void onClickItem(Goods goods, float price, int number, int count);

        void onAdd(View view, Goods goods, float price, int num);

        void onMinus(Goods goods, float price, int num);
    }
}
