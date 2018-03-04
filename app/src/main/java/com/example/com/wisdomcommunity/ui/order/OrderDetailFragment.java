package com.example.com.wisdomcommunity.ui.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.OrderDetailContract;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.order.OrderFragment.KEY_ORDER_ID;

/**
 * Created by rhm on 2018/3/3.
 */

public class OrderDetailFragment extends BaseFragment implements OrderDetailContract.View {
    public static final String TAG_DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private OrderDetailPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

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

    }

    @Override
    protected void destroyView() {

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

    }

    @Override
    public void onLoadDetailFailure(String msg) {
        showShortToast(msg);
    }
}
