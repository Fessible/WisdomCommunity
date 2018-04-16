package com.example.com.wisdomcommunity.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.com.support_business.domain.order.OrderRecord;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderContract;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.MultipleRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshWizard;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 订单
 * Created by rhm on 2018/1/16.
 */

public class OrderFragment extends BaseFragment implements OrderContract.View {
    public static final String TAG_ORDER_FRAGMENT = "ORDER_FRAGMENT";
    public static final String KEY_ORDER_ID = "order_id";
    public static final int REQUEST_CODE = 1;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.multiple_refresh_layout)
    MultipleRefreshLayout multipleRefreshLayout;

    private OrderAdapter adapter;
    private OrderPresenter presenter;


    @Override
    public int getResLayout() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        multipleRefreshLayout.setRefreshWizard(new SwipeRefreshWizard(getContext(), multipleRefreshLayout));
        multipleRefreshLayout.setOnRefreshListener(onRefreshListener);

        presenter = new OrderPresenter(getContext(), OrderFragment.this);
        presenter.loadOrderRecord();
        adapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        adapter.setCallback(new OrderAdapter.Callback() {
            @Override
            public void onCallback(String value) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_ORDER_ID, value);
                IntentUtil.startTemplateActivityForResult(OrderFragment.this, OrderDetailFragment.class, bundle, OrderDetailFragment.TAG_DETAIL_FRAGMENT,REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                presenter.loadOrderRecord();
            }
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.loadOrderRecord();
        }
    };

    @Override
    protected void destroyView() {
        adapter.destroy();
    }

    private void showMultipleEmptyLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showEmpty();
        }
    }

    private void showMultipleErrorLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showError();
        }
    }

    private void showMultipleContentLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.setEnabled(true);
            multipleRefreshLayout.showContentOnly();
        }
    }

    @Override
    public void showProgress() {
        if (multipleRefreshLayout != null) {
            if (!multipleRefreshLayout.isLoading() && !multipleRefreshLayout.isRefreshing()) {
                multipleRefreshLayout.setEnabled(false);
                multipleRefreshLayout.showLoading(false);
            }
        }
    }

    @Override
    public void hideProgress() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.setEnabled(true);
            if (multipleRefreshLayout.isLoading()) {
                multipleRefreshLayout.hideLoading();
            }
            if (multipleRefreshLayout.isRefreshing()) {
                multipleRefreshLayout.tryRefreshFinished();
            }
        }
    }

    @Override
    public void onUnauthorized() {

    }

    @Override
    public void loadOrderRecordSuccess(List<OrderRecord> recordList) {
        showMultipleContentLayout();
        adapter.setData(recordList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void loadorderRecordFailure(String msg) {
        showShortToast(msg);
        showMultipleErrorLayout();
    }
}
