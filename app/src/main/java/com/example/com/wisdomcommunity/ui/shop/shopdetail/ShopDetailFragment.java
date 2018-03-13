package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.ShopDetailContract;
import com.example.com.wisdomcommunity.ui.shop.cart.CartFragment;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;
import static com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment.KEY_GOOD_ORDER;

/**
 * Created by rhm on 2018/2/24.
 */

public class ShopDetailFragment extends BaseFragment implements ShopDetailContract.View {
    public static final String TAG_SHOP_DETAIL_FRAGMENT = "SHOP_DETAIL_FRAGMENT";
    public static final String KEY_GOODS_NUM = "goods_number";
    public static final String KEY_POSITION = "position";
    public static final int REQUEST_GOODS = 1;
    private static final String KEY_GOODS_URL = "goods_url";
    private static final String KEY_ORDER_LIST = "order_list";
    private static final int REQUEST_CART = 2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.shop_layout)
    LinearLayout shopLayout;

    @BindView(R.id.make_order_layout)
    LinearLayout makeOrderLayout;

    @BindView(R.id.img_shop)
    ImageView imgShop;

    @BindView(R.id.work_time)
    TextView workTime;

    @BindView(R.id.shipment)
    TextView shipment;

    @BindView(R.id.shop_info)
    TextView shopInfo;

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.total_price)
    TextView txTotalPrice;

    private String shopId;
    private ShopDetailContract.Presenter presenter;
    private ShopDetailAdapter adapter;
    private String phoneNumber;
    private int clickCount;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        makeOrderLayout.setVisibility(View.GONE);
        adapter = new ShopDetailAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());

        Bundle bundle = getArguments();
        String shopName = bundle.getString(KEY_SHOP_NAME);
        shopId = bundle.getString(KEY_SHOP_ID);
        title.setText(shopName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ShopDetailPresenter(getContext(), ShopDetailFragment.this);
        presenter.loadShopDetail(shopId);
    }

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
        if (orderHashMap != null) {
            orderHashMap.clear();
        }
    }

    @Override
    public void onLoadShopDetailSuccess(ShopDetail shopDetail) {
        if (shopDetail != null) {
            phoneNumber = shopDetail.shopPhone;
            int placeHolder = R.drawable.app_icon;
            Glide.with(getContext()).load(shopDetail.shopUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder).centerCrop())
                    .into(imgShop);
            workTime.setText(getContext().getString(R.string.work_time, shopDetail.workTime));
            shipment.setText(getContext().getString(R.string.shipment, shopDetail.shipment));
            shopInfo.setText(shopDetail.info);
            adapter.setData(shopDetail.goodsList);
            adapter.notifyDataSetChanged();
            adapter.setCallback(callback);
        }
    }

    private int count;
    private float totalPrice;
    private HashMap<String, OrderDetail.Order> orderHashMap = new HashMap<>();
    private OrderDetail.Order order = new OrderDetail.Order();
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
            IntentUtil.startTemplateActivityForResult(ShopDetailFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT, REQUEST_GOODS);
        }

        @Override
        public void onAddPayBack(String goodsUrl, String goodsId, String goodsName, float price, int num) {
            count += 1;
            totalPrice += price;
            changeOrderhashMap(goodsUrl, goodsId, goodsName, price, num);
            showOrderlayout();
        }

        @Override
        public void onMinusPayBack(String goodsUrl, String goodsId, String goodsName, float price, int num) {
            count -= 1;
            totalPrice -= price;
            changeOrderhashMap(goodsUrl, goodsId, goodsName, price, num);
            showOrderlayout();
        }
    };

    private void changeOrderhashMap(String goodsUrl, String goodsId, String goodsName, float price, int num) {
        if (orderHashMap.isEmpty()) {
            addOrderHashMap(goodsUrl, goodsId, goodsName, price, num);
        } else {
            if (orderHashMap.containsKey(goodsId)) {
                order.number = num;
                orderHashMap.put(goodsId, order);
            } else {
                addOrderHashMap(goodsUrl, goodsId, goodsName, price, num);
            }
        }
    }

    //添加新的内容
    private void addOrderHashMap(String goodsUrl, String goodsId, String goodsName, float price, int num) {
        order.goodsUrl = goodsUrl;
        order.goodsName = goodsName;
        order.goodsId = goodsId;
        order.number = num;
        order.price = String.valueOf(price);
        orderHashMap.put(goodsId, order);
    }

    private void showOrderlayout() {
        if (count <= 0) {
            makeOrderLayout.setVisibility(View.GONE);
        } else {
            makeOrderLayout.setVisibility(View.VISIBLE);
            txTotalPrice.setText(String.valueOf(totalPrice));
        }
    }

    @Override
    public void onLoadShopDetailFailure(String msg) {
        showShortToast(msg);
    }

    @OnClick(R.id.phone)
    public void Phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.sure)
    public void makeOrder() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER_LIST, orderHashMap);
        IntentUtil.startTemplateActivityForResult(ShopDetailFragment.this, CartFragment.class, bundle, CartFragment.TAG_CART_FRAGMENT, REQUEST_CART);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GOODS) {
                OrderDetail.Order order = (OrderDetail.Order) data.getSerializableExtra(KEY_GOOD_ORDER);
                int position = data.getIntExtra(KEY_POSITION, 0);
                if (clickCount != order.number) {
                    adapter.setNum(order.number, position);
                    totalPrice += (order.number - clickCount) * Float.valueOf(order.price);
                    count += order.number - clickCount;
                    showOrderlayout();
                    changeOrderhashMap(order.goodsUrl, order.goodsId, order.goodsName, Float.valueOf(order.price), order.number);
                }
            }
        }
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
}
