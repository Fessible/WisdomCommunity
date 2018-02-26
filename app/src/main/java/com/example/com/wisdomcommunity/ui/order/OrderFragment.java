package com.example.com.wisdomcommunity.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * 订单
 * Created by rhm on 2018/1/16.
 */

public class OrderFragment extends BaseFragment implements OrderContract.View {
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
        if (recordList != null) {
            adapter.setData(recordList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadorderRecordFailure(String msg) {
        showShortToast(msg);
    }
}
