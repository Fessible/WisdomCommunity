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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.localsave.ShopCart;
import com.example.com.wisdomcommunity.mvp.ShopDetailContract;
import com.example.com.wisdomcommunity.ui.shop.pay.PayFragment;
import com.example.com.wisdomcommunity.util.DensityUtil;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.FakeAddImageView;
import com.example.com.wisdomcommunity.view.PointFTypeEvaluator;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.MainActivity.ACTION_SHOP_CART_CHANGED;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;

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

    @BindView(R.id.detail_layout)
    RelativeLayout shopLayout;

    @BindView(R.id.make_order_layout)
    RelativeLayout makeOrderLayout;

    @BindView(R.id.img_shop)
    ImageView imgShop;

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

    @BindView(R.id.table_layout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private ShopGoodsFragment shopGoodsFragment;
    private ShopInfoFragment shopInfoFragment;
    private String shopId;
    private ShopDetailContract.Presenter presenter;
    private String phoneNumber;
    private boolean isClick;//判断是否操作了购物车的数值
    private String strPrice;
    private String shopName;
    private DialogCartAdapter cartAdapter;
    private ShopDetail shopDetail;
    private List<Goods> goodsList = new ArrayList<>();
    private int count;
    private float totalPrice;
    private HashMap<String, OrderDetail.Order> orderHashMap = new HashMap<>();
    private List<Goods> discountList = new ArrayList<>();

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
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopName = bundle.getString(KEY_SHOP_NAME);
            shopId = bundle.getString(KEY_SHOP_ID);

            collapsingToolbarLayout.setTitle(shopName);
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
            collapsingToolbarLayout.setExpandedTitleMargin(70, 0, 0, 100);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

            initViewPager();
        }

    }


    //设置TabLayout下划线的宽度
    private void reflex(final TabLayout tableLayout) {
        tableLayout.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout mTabStrip = (LinearLayout) tableLayout.getChildAt(0);
                int dp10 = DensityUtil.dip2px(tableLayout.getContext(), 50);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    //拿到tabView的mTextView属性
                    try {
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTetxtView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //字多宽线多宽，测量TextView的宽度
                        int width = 0;
                        width = mTetxtView.getWidth();
                        if (width == 0) {
                            mTetxtView.measure(0,0);
                            width = mTetxtView.getMeasuredWidth();
                        }

                        //设tab左右边距为10dp
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private void initViewPager() {

        shopInfoFragment = new ShopInfoFragment();
        shopGoodsFragment = new ShopGoodsFragment();

        Fragment[] fragments = {shopGoodsFragment, shopInfoFragment};
        String[] subTitles = {getString(R.string.goods_detail), getString(R.string.shop_info)};//标题

        tabLayout.addTab(tabLayout.newTab().setText(subTitles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(subTitles[1]));
        ShopDetailPagerAdapter adapter = new ShopDetailPagerAdapter(getChildFragmentManager(), fragments, subTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        reflex(tabLayout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ShopDetailPresenter(getContext(), ShopDetailFragment.this);
        presenter.loadShopDetail(shopId);
    }

    @Override
    protected void destroyView() {
        ShopCart.setShopCart(getContext(), orderHashMap);
        ShopCart.setCount(getContext(), count);
        ShopCart.setTotalPrice(getContext(), new DecimalFormat(".00").format(totalPrice));
        ShopCart.setShop(getContext(), shopDetail);

        Intent intent = new Intent();
        intent.setAction(ACTION_SHOP_CART_CHANGED);
        getActivity().sendBroadcast(intent);

        if (orderHashMap != null) {
            orderHashMap.clear();
        }
    }

    @Override
    public void onLoadShopDetailSuccess(ShopDetail shopDetail) {
        if (shopDetail != null) {
            this.shopDetail = shopDetail;
            this.shopDetail.shopId = shopId;
            this.shopDetail.shopName = shopName;
            this.shopDetail.shipment = shopDetail.shipment;
            phoneNumber = shopDetail.shopPhone;
            int placeHolder = R.drawable.app_icon;
            Glide.with(getContext()).load(shopDetail.shopUrl)
                    .apply(new RequestOptions()
                            .fallback(placeHolder)
                            .placeholder(placeHolder).centerCrop())
                    .into(imgShop);
            goodsList = shopDetail.goodsList;
            discountList = shopDetail.discountList;
            initShopInfoFragment(shopDetail);
            initShopGoodsFragment(shopDetail);


            //获取本地购物车信息
            int count = ShopCart.getCount(getContext());
            if (count > 0) {
                this.count = count;
                this.orderHashMap = ShopCart.getShopCart(getContext());
                totalPrice = Float.parseFloat(ShopCart.getTotalPrice(getContext()));
                showOrderlayout();
                isClick = true;
                changeShopGoods();
            }
        }
    }

    private void initShopGoodsFragment(ShopDetail shopDetail) {
        if (shopGoodsFragment != null) {
            shopGoodsFragment.setShopDetail(shopDetail);
            shopGoodsFragment.setGoodsCallback(goodsCallback);
        }
    }

    private ShopGoodsFragment.GoodsCallback goodsCallback = new ShopGoodsFragment.GoodsCallback() {
        @Override
        public void onClickItem(Goods goods, float price, int number, int num) {
            count += num;
            totalPrice += price * num;
            showOrderlayout();
            int type = 0;

            changeOrderhashMap(goods, price, number, type);
        }

        @Override
        public void onAdd(View view, Goods goods, float price, int num, int type) { //点击加号时的动态效果
            add(price);
            changeOrderhashMap(goods, price, num, type);
            addAnimation(view);
        }

        @Override
        public void onMinus(Goods goods, float price, int num, int type) {//点击减号时的效果
            minus(price);
            changeOrderhashMap(goods, price, num, type);
            showOrderlayout();
        }
    };

    private void initShopInfoFragment(ShopDetail shopDetail) {
        if (shopInfoFragment != null) {
            shopInfoFragment.setShopDetial(shopDetail);
        }
    }


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
    private void changeOrderhashMap(Goods goods, float price, int num, int type) {
        if (orderHashMap.isEmpty()) {
            addOrderHashMap(goods, price, num, type);
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
                addOrderHashMap(goods, price, num, type);
            }
        }
    }

    private void changeOrderhashMap(OrderDetail.Order order) {
        orderHashMap.put(order.goodsId, order);
    }


    private void addOrderHashMap(Goods goods, float price, int num, int type) {
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
            showEmptyCart();
        } else {
            sure.setVisibility(View.VISIBLE);
            cartTotalNum.setVisibility(View.VISIBLE);
            shopCart.setBackgroundResource(R.drawable.icon_cart_n);
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            strPrice = decimalFormat.format(totalPrice);
            txTotalPrice.setText(getString(R.string.sign_price, strPrice));
        }
    }

    private void showEmptyCart() {
        txTotalPrice.setText(getString(R.string.sign_price, "00.00"));
        sure.setVisibility(View.GONE);
        cartTotalNum.setVisibility(View.GONE);
        shopCart.setBackgroundResource(R.drawable.icon_cart_un);
        count = 0;
    }


    //加号贝塞尔动图
    private void addAnimation(View view) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shopCart.getLocationInWindow(cartLocation);

        PointF start = new PointF();
        PointF end = new PointF();
        PointF control = new PointF();

        start.x = addLocation[0];
        start.y = addLocation[1] - 60;
        end.x = cartLocation[0] + 40;
        end.y = cartLocation[1] - 100;
        control.x = end.x + 40;
        control.y = start.y - 60;
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

        ObjectAnimator scaleAnimatorX = new ObjectAnimator()
                .ofFloat(shopCart, "scaleX", 1.2f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator()
                .ofFloat(shopCart, "scaleY", 1.2f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();
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
        bundle.putString(KEY_TOTAL_MONEY, strPrice);
        bundle.putSerializable(KEY_ORDER_LIST, (Serializable) orderList);

        IntentUtil.startTemplateActivity(ShopDetailFragment.this, PayFragment.class, bundle, PayFragment.TAG_PAY_FRAGMENT);
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
            showBottomShopCart();
        }
    }

    //显示购物车
    public void showBottomShopCart() {
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
            isClick = true;
            add(price);
            changeOrderhashMap(order);
            showOrderlayout();
        }

        @Override
        public void onMinusItem(OrderDetail.Order order, float price, int num) {
            isClick = true;
            minus(price);
            changeOrderhashMap(order);
            showOrderlayout();
        }

        //检测购物车是否被清空了
        private void checkIsEmpty() {
            if (cartAdapter != null) {

                if (cartAdapter.getItemCount() == 0) {
                    bottomSheetLayout.dismissSheet();
                    clearCart();
                }
            }

        }
    };

    private void changeShopGoods() {
        if (isClick) {
            if (goodsList != null && !goodsList.isEmpty() && discountList != null && !discountList.isEmpty() && shopGoodsFragment != null) {

                for (Goods goods : goodsList) {
                    if (orderHashMap.containsKey(goods.goodsId)) {
                        goods.num = orderHashMap.get(goods.goodsId).number;
                    } else {
                        goods.num = 0;
                    }
                }
                shopGoodsFragment.changeGoods(goodsList);//更改商品信息

                for (Goods goods : discountList) {
                    if (orderHashMap.containsKey(goods.goodsId)) {
                        goods.num = orderHashMap.get(goods.goodsId).number;
                    } else {
                        goods.num = 0;
                    }
                }
                shopGoodsFragment.changeDiscount(discountList);
            }
        }
        isClick = false;
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
        orderHashMap.clear();
        if (cartAdapter != null && shopGoodsFragment != null) {
            shopGoodsFragment.clear();
            cartAdapter.destroy();
        }
        showEmptyCart();
    }


}
