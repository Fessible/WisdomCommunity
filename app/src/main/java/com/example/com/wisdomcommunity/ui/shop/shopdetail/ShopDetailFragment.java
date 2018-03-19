package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.ShopDetailContract;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.ui.shop.pay.PayFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.FakeAddImageView;
import com.example.com.wisdomcommunity.view.PointFTypeEvaluator;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String KEY_GOODS_URL = "goods_url";
    public static final String KEY_ORDER_LIST = "order_list";
    private static final int REQUEST_CART = 2;
    public static final String KEY_SHIPMENT = "shipment";
    public static final String KEY_TOTAL_MONEY = "total_price";
    public static final String KEY_SHOP = "shop_detail";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.title)
//    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.detail_layout)
    RelativeLayout shopLayout;

    @BindView(R.id.make_order_layout)
    RelativeLayout makeOrderLayout;

    @BindView(R.id.img_shop)
    ImageView imgShop;

    @BindView(R.id.work_time)
    TextView workTime;

    @BindView(R.id.shipment)
    TextView shipment;
/*
    @BindView(R.id.shop_info)
    TextView shopInfo;*/

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.total_price)
    TextView txTotalPrice;

    @BindView(R.id.shopping_cart_bottom)
    ImageView shopCart;

    @BindView(R.id.shopping_cart_total_num)
    TextView cartTotalNum;

    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private String shopId;
    private ShopDetailContract.Presenter presenter;
    private ShopDetailAdapter adapter;
    private String phoneNumber;
    private int clickCount;
    private int fee;//配送费
    private String strPrice;
    private String shopName;
    private DialogCartAdapter cartAdapter;

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
        adapter = new ShopDetailAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());

        Bundle bundle = getArguments();
        if (bundle != null) {
            shopName = bundle.getString(KEY_SHOP_NAME);
            shopId = bundle.getString(KEY_SHOP_ID);
//            title.setText(shopName);
            collapsingToolbarLayout.setTitle(shopName);
//            collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleGravity(Gravity.START);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        }

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

    private ShopDetail shopDetail;

    @Override
    public void onLoadShopDetailSuccess(ShopDetail shopDetail) {
        if (shopDetail != null) {
            this.shopDetail = shopDetail;
            phoneNumber = shopDetail.shopPhone;
            fee = shopDetail.shipment;
            int placeHolder = R.drawable.app_icon;
            Glide.with(getContext()).load(shopDetail.shopUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder).centerCrop())
                    .into(imgShop);
            workTime.setText(getContext().getString(R.string.work_time, shopDetail.workTime));
            shipment.setText(getContext().getString(R.string.shipment, String.valueOf(fee)));
//            shopInfo.setText(shopDetail.info);
            adapter.setData(shopDetail.goodsList);
            goodsList = shopDetail.goodsList;
            adapter.notifyDataSetChanged();
            adapter.setCallback(callback);
        }
    }

    private List<Goods> goodsList = new ArrayList<>();
    private int count;
    private float totalPrice;
    private HashMap<String, OrderDetail.Order> orderHashMap = new HashMap<>();
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
        public void onAddPayBack(View view, Goods goods, float price, int num) {
            add(price);
            changeOrderhashMap(goods, price, num);

            int[] addLocation = new int[2];
            int[] cartLocation = new int[2];
            int[] recycleLocation = new int[2];
            view.getLocationInWindow(addLocation);
            shopCart.getLocationInWindow(cartLocation);
            recyclerView.getLocationInWindow(recycleLocation);

            PointF start = new PointF();
            PointF end = new PointF();
            PointF control = new PointF();


            start.x = addLocation[0];
            start.y = addLocation[1] - 60;
            end.x = cartLocation[0] + 40;
            end.y = cartLocation[1] - 100;
            control.x = end.x + 40;
            control.y = start.y - 60;
//            final ImageView imageView = new ImageView(getActivity());
            final FakeAddImageView imageView = new FakeAddImageView(getActivity());
            shopLayout.addView(imageView);
            imageView.setBackgroundResource(R.drawable.icon_add_n);
            imageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
            imageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
            imageView.setVisibility(View.VISIBLE);
            ObjectAnimator addAnimator = ObjectAnimator.ofObject(imageView, "mPointF",
                    new PointFTypeEvaluator(control), start, end);

            addAnimator.setInterpolator(new AccelerateInterpolator());

            addAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    imageView.setVisibility(View.GONE);
                    shopLayout.removeView(imageView);
                    showOrderlayout();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    imageView.setVisibility(View.VISIBLE);
                }
            });

            ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shopCart, "scaleX", 1.2f, 1.0f);
            ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shopCart, "scaleY", 1.2f, 1.0f);
            scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
            scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
            animatorSet.setDuration(800);
            animatorSet.start();
        }

        @Override
        public void onMinusPayBack(Goods goods, float price, int num) {
            minus(price);
            changeOrderhashMap(goods, price, num);
            showOrderlayout();
        }
    };

    //减少商品数量
    private void minus(float price) {
        count -= 1;
        totalPrice -= price;
    }

    private void add(float price) {
        count += 1;
        totalPrice += price;
    }

    //使用Hashmap 来存储内容
    private void changeOrderhashMap(Goods goods, float price, int num) {
        if (orderHashMap.isEmpty()) {
            addOrderHashMap(goods, price, num);
        } else {
            if (orderHashMap.containsKey(goods.goodsId)) {
                if (num == 0) {
                    orderHashMap.remove(goods.goodsId);
                } else {
                    OrderDetail.Order order = orderHashMap.get(goods.goodsId);
                    order.number = num;
                    orderHashMap.put(goods.goodsId, order);
                }
            } else {
                addOrderHashMap(goods, price, num);
            }
        }
    }

    private void changeOrderhashMap(OrderDetail.Order order) {
        orderHashMap.put(order.goodsId, order);
    }


    private void addOrderHashMap(Goods goods, float price, int num) {
        OrderDetail.Order order = new OrderDetail.Order();
        order.goodsUrl = goods.goodsUrl;
        order.goodsName = goods.goodsName;
        order.goodsId = goods.goodsId;
        order.remain = goods.remain;
        order.number = num;
        order.price = String.valueOf(price);
        orderHashMap.put(goods.goodsId, order);
    }

    private void showOrderlayout() {
        cartTotalNum.setText(String.valueOf(count));
        if (count <= 0) {
            txTotalPrice.setText(getString(R.string.sign_price, "00.00"));
            sure.setVisibility(View.GONE);
            cartTotalNum.setVisibility(View.GONE);
            shopCart.setBackgroundResource(R.drawable.icon_cart_un);
        } else {
            sure.setVisibility(View.VISIBLE);
            cartTotalNum.setVisibility(View.VISIBLE);
            shopCart.setBackgroundResource(R.drawable.icon_cart_n);
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            strPrice = decimalFormat.format(totalPrice);
            txTotalPrice.setText(getString(R.string.sign_price, strPrice));
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
        List<OrderDetail.Order> orderList = new ArrayList<>();
        for (Map.Entry<String, OrderDetail.Order> entry : orderHashMap.entrySet()) {
            orderList.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SHOP, shopDetail);
        bundle.putString(KEY_SHOP_ID, shopId);
        bundle.putString(KEY_SHOP_NAME, shopName);
        bundle.putString(KEY_TOTAL_MONEY, strPrice);
        bundle.putInt(KEY_SHIPMENT, fee);

        bundle.putSerializable(KEY_ORDER_LIST, (Serializable) orderList);
        IntentUtil.startTemplateActivity(ShopDetailFragment.this, PayFragment.class, bundle, PayFragment.TAG_PAY_FRAGMENT);
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
                    Goods goods = new Goods();
                    goods.goodsUrl = order.goodsUrl;
                    goods.goodsId = order.goodsId;
                    goods.remain = order.remain;
                    goods.goodsName = order.goodsName;
                    changeOrderhashMap(goods, Float.valueOf(order.price), order.number);
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

    //点击购物车
    @OnClick(R.id.shopping_cart_bottom)
    public void onshopCart() {
        if (count > 0) {
            shoBottomShopCart();
        }
    }

    //显示购物车
    public void shoBottomShopCart() {
        View view = createBootomShopCart();
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            if (orderHashMap.size() != 0) {
                bottomSheetLayout.showWithSheetView(view);
            }
        }
        bottomSheetLayout.addOnSheetDismissedListener(new OnSheetDismissedListener() {
            @Override
            public void onDismissed(BottomSheetLayout bottomSheetLayout) {
                changeShopGoods();
            }
        });
    }

    //创建购物车布局
    private View createBootomShopCart() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shop_cart_bottom, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        final TextView clear = view.findViewById(R.id.txt_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCart();
            }
        });

        RecyclerView rvProduct = view.findViewById(R.id.rv_product);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new DialogCartAdapter(getContext());
        rvProduct.setAdapter(cartAdapter);
        cartAdapter.setCallback(carCallback);
        parseHashMap(orderHashMap);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.wheather_clear_cart)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                clearCart();
                                bottomSheetLayout.dismissSheet();
                            }
                        })
                        .setNegativeButton(R.string.opt_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    private DialogCartAdapter.Callback carCallback = new DialogCartAdapter.Callback() {
        @Override
        public void onDelete(final int position, final OrderDetail.Order order, final Float price) {
            new AlertDialog.Builder(getContext())
                    .setMessage("是否删除该商品？")
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (cartAdapter != null) {
                                cartAdapter.remove(position);
                                checkIsEmpty();
                                minus(price);
                                orderHashMap.remove(order.goodsId);
                                showOrderlayout();
                            }
                        }
                    })
                    .setNegativeButton(R.string.opt_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        @Override
        public void onAddItem(OrderDetail.Order order, float price, int num) {
            add(price);
            changeOrderhashMap(order);
            showOrderlayout();
        }

        @Override
        public void onMinusItem(OrderDetail.Order order, float price, int num) {
            minus(price);
            changeOrderhashMap(order);
            showOrderlayout();
        }


        private void checkIsEmpty() {
            if (cartAdapter != null && adapter != null) {
                if (cartAdapter.getItemCount() == 0) {
                    bottomSheetLayout.dismissSheet();
                    orderHashMap.clear();
                    adapter.setNum(0);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    };

    private void changeShopGoods() {
        if (goodsList != null && !goodsList.isEmpty() && adapter != null) {
            for (Goods goods : goodsList) {
                if (orderHashMap.containsKey(goods.goodsId)) {
                    goods.num = orderHashMap.get(goods.goodsId).number;
                } else {
                    goods.num = 0;
                }
            }
            adapter.setData(goodsList);
            adapter.notifyDataSetChanged();
        }
    }

    private void parseHashMap(HashMap<String, OrderDetail.Order> orderHashMap) {
        List<OrderDetail.Order> orderList = new ArrayList<>();
        for (Map.Entry<String, OrderDetail.Order> entry : orderHashMap.entrySet()) {
            orderList.add(entry.getValue());
        }
        if (cartAdapter != null) {
            cartAdapter.setOrderData(orderList);
            cartAdapter.notifyDataSetChanged();
        }
    }

    //清空购物车
    public void clearCart() {
        if (cartAdapter != null) {
            cartAdapter.destroy();
            cartAdapter.notifyDataSetChanged();
        }
    }
}
