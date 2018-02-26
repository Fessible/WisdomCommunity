package com.example.com.wisdomcommunity.ui.order;

import android.os.Bundle;
import android.view.View;

import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderContract;

import java.util.List;

/**
 * 订单
 * Created by rhm on 2018/1/16.
 */

public class OrderFragment extends BaseFragment implements OrderContract.View {
    private OrderPresenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new OrderPresenter(getContext(), OrderFragment.this);
        presenter.loadOrderRecord();
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

    }

    @Override
    public void loadorderRecordFailure(String msg) {

    }
}
