package com.example.com.wisdomcommunity.ui.shop.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.shop.pay.PayFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_ORDER_LIST;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_SHIPMENT;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_TOTAL_MONEY;

/**
 * Created by rhm on 2018/3/12.
 * 购物车
 */

public class CartFragment extends BaseFragment {
    public static final String TAG_CART_FRAGMENT = "CART_FRAGMENT";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.total_price)
    TextView totalPrice;

    @BindView(R.id.shipment)
    TextView txShipment;

    private CartAdapter adapter;
    private Float totalPay;
    private int shipment;


    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_cart;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        title.setText(R.string.title_shop_cart);

        Bundle bundle = getArguments();

        toolbar.setNavigationOnClickListener(navigationListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());

        if (bundle != null) {
            HashMap<String, OrderDetail.Order> orderHashMap = (HashMap<String, OrderDetail.Order>) bundle.getSerializable(KEY_ORDER_LIST);
            shipment = bundle.getInt(KEY_SHIPMENT);
            String total = bundle.getString(KEY_TOTAL_MONEY);
            totalPay = Float.valueOf(total) + shipment;
            parseHashMap(orderHashMap);

            totalPrice.setText(String.valueOf(totalPay));
            txShipment.setText(getContext().getString(R.string.shipment_fee, String.valueOf(shipment)));
        }

        adapter.setCallback(callback);

    }

    private CartAdapter.Callback callback = new CartAdapter.Callback() {
        @Override
        public void onDelete(final int position, OrderDetail.Order order, final Float price) {
            new AlertDialog.Builder(getContext())
                    .setMessage("是否删除该商品？")
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (adapter != null) {
                                adapter.remove(position);
                                checkIsEmpty();
                                totalPay -= price;
                                showTotalPrice();
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
            totalPay += price;
            showTotalPrice();
        }

        @Override
        public void onMinusItem(OrderDetail.Order order, float price, int num) {
            totalPay -= price;
            showTotalPrice();
        }
    };

    private void showTotalPrice() {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String strPrice = decimalFormat.format(totalPay);
        totalPrice.setText(strPrice);
    }

    private void checkIsEmpty() {
        if (adapter != null) {
            if (adapter.getItemCount() == 0) {
                showEmpty(true);
            }
        }
    }

    private void parseHashMap(HashMap<String, OrderDetail.Order> orderHashMap) {
        List<OrderDetail.Order> orderList = new ArrayList<>();
        for (Map.Entry<String, OrderDetail.Order> entry : orderHashMap.entrySet()) {
            orderList.add(entry.getValue());
        }
        if (adapter != null) {
            adapter.setOrderData(orderList);
            adapter.notifyDataSetChanged();
        }
    }

    private View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            getActivity().finish();
        }
    };

    @OnClick(R.id.delete)
    public void Delete() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.wheather_clear_cart)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (adapter != null) {
                            adapter.destroy();
                            adapter.notifyDataSetChanged();
                            showEmpty(true);
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

    @OnClick(R.id.buy)
    public void buy() {
        IntentUtil.startTemplateActivity(CartFragment.this, PayFragment.class, PayFragment.TAG_PAY_FRAGMENT);
    }

    @BindView(R.id.delete)
    ImageView delete;

    @BindView(R.id.buy_layout)
    RelativeLayout buyLayout;

    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;

    private void showEmpty(boolean isEmpty) {
        delete.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        buyLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }
}
