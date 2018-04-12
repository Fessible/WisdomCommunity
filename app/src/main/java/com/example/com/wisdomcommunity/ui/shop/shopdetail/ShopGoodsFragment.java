package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment.KEY_GOOD_ORDER;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.CategoryBean.TYPE_ALL;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.CategoryBean.TYPE_DISCOUNT;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_GOODS_NUM;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_GOODS_URL;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_POSITION;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.REQUEST_GOODS;

/**
 * Created by rhm on 2018/3/19.
 */

public class ShopGoodsFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.lv_product_category)
    ListView lvProductCategory;

    private View view;
    private Bundle savedInstanceState;
    private ShopDetail shopDetail;
    private ShopDetailAdapter adapter;
    private GoodsCallback goodsCallback;
    private int clickCount;
    private CategoryAdapter categoryAdapter;
    private int allCount;
    private int discountCount;
    private List<Goods> goodsList = new ArrayList<>();
    private List<Goods> discountList = new ArrayList<>();

    public void setShopDetail(ShopDetail shopDetail) {
        this.shopDetail = shopDetail;
        initView(view, savedInstanceState);
    }

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
            discountList = shopDetail.discountList;
            goodsList = shopDetail.goodsList;
            initRecyclerView(shopDetail);
            initCategory();
        }
    }

    //初始化分类栏
    private void initCategory() {
        List<CategoryBean> list = new ArrayList<>();
        list.add(new CategoryBean(getString(R.string.normal_goods), TYPE_ALL));
        list.add(new CategoryBean(getString(R.string.discount_goods), TYPE_DISCOUNT));
        categoryAdapter = new CategoryAdapter(getContext(), list);
        lvProductCategory.setAdapter(categoryAdapter);
        lvProductCategory.setOnItemClickListener(itemClick);
    }

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            categoryAdapter.setSelection(i);
            categoryAdapter.notifyDataSetChanged();
            if (adapter != null) {
                switch (i) {
                    case TYPE_ALL:
                        adapter.setData(goodsList, TYPE_ALL);
                        adapter.notifyDataSetChanged();
                        break;
                    case TYPE_DISCOUNT:
                        adapter.setData(discountList, TYPE_DISCOUNT);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    };

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
        adapter.setData(shopDetail.goodsList, TYPE_ALL);
        adapter.notifyDataSetChanged();
        adapter.setCallback(callback);
    }

    private ShopDetailAdapter.Callback callback = new ShopDetailAdapter.Callback() {
        @Override
        public void onCallback(Goods goods, int num, int position, int type) {
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
        public void onAddPayBack(View v, Goods goods, float price, int num, int type) {
            if (goodsCallback != null) {
                goodsCallback.onAdd(v, goods, price, num, type);
            }
        }

        @Override
        public void onMinusPayBack(Goods goods, float price, int num, int type) {
            if (goodsCallback != null) {
                goodsCallback.onMinus(goods, price, num, type);
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
            this.goodsList = goodsList;
            adapter.setData(goodsList, TYPE_ALL);
            adapter.notifyDataSetChanged();
        }
    }

    public void changeDiscount(List<Goods> discountList) {
        if (adapter != null) {
            this.discountList = discountList;
            adapter.setData(discountList, TYPE_DISCOUNT);
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

        void onAdd(View view, Goods goods, float price, int num, int type);

        void onMinus(Goods goods, float price, int num, int type);
    }
}
