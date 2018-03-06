package com.example.com.wisdomcommunity.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderDetailContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_HEADER_SHOP;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_ORDER_LIST;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_PHONE;
import static com.example.com.wisdomcommunity.ui.order.OrderDetailAdapter.Item.VIEW_PRICE;
import static com.example.com.wisdomcommunity.ui.order.OrderFragment.KEY_ORDER_ID;

/**
 * Created by rhm on 2018/3/3.
 */

public class OrderDetailFragment extends BaseFragment implements OrderDetailContract.View {
    public static final String TAG_DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    public static final String KEY_SHOP_ID = "shop_id";
    private OrderDetailPresenter presenter;
    private OrderDetailAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int getResLayout() {
        return R.layout.fragment_template_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String orderId = bundle.getString(KEY_ORDER_ID);
        presenter = new OrderDetailPresenter(getContext(), OrderDetailFragment.this);
        presenter.loadDetail(orderId);
        title.setText(getContext().getString(R.string.order_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        adapter = new OrderDetailAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setCallback(callback);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .typeBetween(VIEW_HEADER_SHOP, VIEW_PRICE, new DividerDecor.Builder(getContext())
                        .dividerResId(R.drawable.icon_horizontal_line).build())
                .typeBetween(VIEW_ORDER_LIST, VIEW_PHONE, new DividerDecor.Builder(getContext())
                        .dividerResId(R.drawable.icon_horizontal_line).build())
                .build());

    }

    private OrderDetailAdapter.Callback callback = new OrderDetailAdapter.Callback() {
        @Override
        public void onCallback(String value, int type) {
            if (type == VIEW_HEADER_SHOP) {
                //店铺页面
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SHOP_ID, value);
            }
        }
    };

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
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

    @Override
    public void onLoadDetailSuccess(OrderDetail orderDetail) {
        if (adapter != null) {
            adapter.setData(orderDetail);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadDetailFailure(String msg) {
        showShortToast(msg);
    }
}
