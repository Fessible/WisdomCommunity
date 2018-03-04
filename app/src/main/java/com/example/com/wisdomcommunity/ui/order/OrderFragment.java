package com.example.com.wisdomcommunity.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderContract;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * 订单
 * Created by rhm on 2018/1/16.
 */

public class OrderFragment extends BaseFragment implements OrderContract.View {
    public static final String TAG_ORDER_FRAGMENT = "ORDER_FRAGMENT";
    public static final String KEY_ORDER_ID = "order_id";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private OrderAdapter adapter;
    private OrderPresenter presenter;


    @Override
    public int getResLayout() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new OrderPresenter(getContext(), OrderFragment.this);
        presenter.loadOrderRecord();
        adapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.img_line_n))
                        .build()).build());
        adapter.setCallback(new OrderAdapter.Callback() {
            @Override
            public void onCallback(String value) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_ORDER_ID, value);
                IntentUtil.startTemplateActivity(OrderFragment.this, OrderDetailFragment.class, bundle, OrderDetailFragment.TAG_DETAIL_FRAGMENT);
            }
        });
    }

    @Override
    protected void destroyView() {
        adapter.destroy();
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
    public void loadOrderRecordSuccess(List<OrderRecord> recordList) {
        adapter.setData(recordList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void loadorderRecordFailure(String msg) {
        showShortToast(msg);
    }
}
