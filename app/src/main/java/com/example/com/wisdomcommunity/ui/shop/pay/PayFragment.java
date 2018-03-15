package com.example.com.wisdomcommunity.ui.shop.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.Constants;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.order.OrderDetailFragment;
import com.example.com.wisdomcommunity.ui.person.address.AddressFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailFragment.KEY_ORDER_DETAIL;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.KEY_ADDRESS;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;
import static com.example.com.wisdomcommunity.ui.shop.pay.RemarkFragment.KEY_REMARK;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_ORDER_LIST;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_SHIPMENT;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_SHOP;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_TOTAL_MONEY;

/**
 * Created by rhm on 2018/3/14.
 * 确认订单
 */

public class PayFragment extends BaseFragment {
    public static final String TAG_PAY_FRAGMENT = "PAY_FRAGMENT";
    public static final int REQUEST_ADDRESS = 1;
    public static final int REQUEST_REMARK = 2;
    public static final String KEY_TYPE = "address_type";
    public static final int TYPE_CHOOSE = 1;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.total_price)
    TextView totalPrice;

    private PayAdapter adapter;
    private int remarkPosition;
    private int addressPosition;
    private ShopDetail shopDetail;
    private String shopId;
    private String shopName;
    private Address address;
    private int ishipment;
    private String strPrice;//实际支付
    private String total;//总价
    private String remark;
    private List<OrderDetail.Order> orderList;

    @Override
    public int getResLayout() {
        return R.layout.fragment_pay_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        title.setText(R.string.make_order);
        toolbar.setNavigationOnClickListener(navigationListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayAdapter(getContext());
        recyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            shopDetail = (ShopDetail) bundle.getSerializable(KEY_SHOP);
            shopId = bundle.getString(KEY_SHOP_ID);
            shopName = bundle.getString(KEY_SHOP_NAME);

            strPrice = bundle.getString(KEY_TOTAL_MONEY);
            totalPrice.setText(strPrice);
            ishipment = bundle.getInt(KEY_SHIPMENT);
            total = String.valueOf(Float.valueOf(strPrice) - ishipment);
            orderList = (List<OrderDetail.Order>) bundle.getSerializable(KEY_ORDER_LIST);
            adapter.setData(orderList, ishipment, total);
            adapter.notifyDataSetChanged();
        }

        adapter.setCallback(callback);
    }

    private View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    };

    private PayAdapter.Callback callback = new PayAdapter.Callback() {
        @Override
        public void remarkItem(int position) {
            remarkPosition = position;
            IntentUtil.startTemplateActivityForResult(PayFragment.this, RemarkFragment.class, RemarkFragment.TAG_REMARK_FRAGMENT, REQUEST_REMARK);
        }

        @Override
        public void addressItem(int position) {
            addressPosition = position;
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_TYPE, TYPE_CHOOSE);
            IntentUtil.startTemplateActivityForResult(PayFragment.this, AddressFragment.class, bundle, AddressFragment.TAG_ADDRESS_FRAGMENT, REQUEST_ADDRESS);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_REMARK:
                    if (data != null && adapter != null) {
                        remark = data.getStringExtra(KEY_REMARK);
                        adapter.setRemark(remark, remarkPosition);
                    }
                    break;
                case REQUEST_ADDRESS:
                    if (data != null && adapter != null) {
                        address = (Address) data.getSerializableExtra(KEY_ADDRESS);
                        adapter.setAddress(address.name + " " + address.phone, address.address, addressPosition);
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.buy)
    public void buy() {
        if (address == null) {
            showShortToast(getContext().getString(R.string.add_address));
            return;
        }

        Bundle bundle = new Bundle();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.shopPhone = shopDetail.shopPhone;
        orderDetail.shopId = shopId;
        orderDetail.shopName = shopName;
        //收货人
        orderDetail.address = address.address;
        orderDetail.receiverPhone = address.phone;
        orderDetail.receiverName = address.name;
        orderDetail.remark = remark;
        orderDetail.orderTime = new Date().getTime();//获取当前时间的时间戳

        orderDetail.orderId = orderDetail.orderTime + String.valueOf(Math.abs(UUID.randomUUID().hashCode()));

        orderDetail.shipment = ishipment;
        orderDetail.total = total;
        orderDetail.pay = strPrice;

        orderDetail.orderStatus = Constants.STATUS_SUBMIT;
        orderDetail.details = orderList;

        bundle.putSerializable(KEY_ORDER_DETAIL, orderDetail);

        IntentUtil.startTemplateActivity(PayFragment.this, OrderDetailFragment.class, bundle, OrderDetailFragment.TAG_DETAIL_FRAGMENT);
        getActivity().finish();
    }

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }
}
