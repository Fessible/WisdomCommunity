package com.example.com.wisdomcommunity.ui.shop.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.ui.person.address.AddressFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.KEY_ADDRESS;
import static com.example.com.wisdomcommunity.ui.shop.pay.RemarkFragment.KEY_REMARK;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_ORDER_LIST;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_SHIPMENT;
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
            String strPrice = bundle.getString(KEY_TOTAL_MONEY);
            totalPrice.setText(strPrice);
            int shipment = bundle.getInt(KEY_SHIPMENT);
            List<OrderDetail.Order> orderList = (List<OrderDetail.Order>) bundle.getSerializable(KEY_ORDER_LIST);
            adapter.setData(orderList, shipment, String.valueOf(Float.valueOf(strPrice) - shipment));
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
                        String remark = data.getStringExtra(KEY_REMARK);
                        adapter.setRemark(remark, remarkPosition);
                    }
                    break;
                case REQUEST_ADDRESS:
                    if (data != null && adapter != null) {
                        Address address = (Address) data.getSerializableExtra(KEY_ADDRESS);
                        adapter.setAddress(address.name + " " + address.phone, address.address, addressPosition);
                    }
                    break;
            }
        }
    }

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }
}
