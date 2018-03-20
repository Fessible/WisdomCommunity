package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.shop.ShopDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by rhm on 2018/3/19.
 */

public class ShopInfoFragment extends BaseFragment {

    @BindView(R.id.minus)
    TextView minus;//满减

    @BindView(R.id.discount)
    TextView discount; //打折

    @BindView(R.id.info)
    TextView info;

    @BindView(R.id.shipment)
    TextView shipment;

    @BindView(R.id.work_time)
    TextView workTime;

    private ShopDetail shopDetail;
    private View view;
    private Bundle savedInstanceState;

    public void setShopDetial(ShopDetail shopDetial) {
        this.shopDetail = shopDetial;
        initView(view,savedInstanceState);
    }


    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_info;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        this.view = view;
        this.savedInstanceState = savedInstanceState;
        init();
    }

    private void init() {
        if (shopDetail != null) {
            if (shopDetail.discount != null) {
                discount.setVisibility(View.VISIBLE);
                discount.setText(shopDetail.discount);
            } else {
                discount.setText(View.GONE);
            }

            if (shopDetail.minus != null) {
                minus.setVisibility(View.VISIBLE);
                minus.setText(shopDetail.minus);
            } else {
                minus.setVisibility(View.GONE);
            }

            info.setText(shopDetail.info);
            workTime.setText(getString(R.string.work_time,shopDetail.workTime));
            shipment.setText(getString(R.string.shipment, String.valueOf(shopDetail.shipment)));
        }
    }

    @Override
    protected void destroyView() {

    }
}
