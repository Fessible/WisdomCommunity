package com.example.com.wisdomcommunity.ui.shop.pay;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by rhm on 2018/3/14.
 * 确认订单
 */

public class PayFragment extends BaseFragment {
    public static final String TAG_PAY_FRAGMENT = "PAY_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PayAdapter adapter;

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
    }

    private View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    };

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }
}
