package com.example.com.wisdomcommunity.ui.shop.goodsdetail;

import android.os.Bundle;
import android.view.View;

import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.GoodsDetailContract;

/**
 * Created by rhm on 2018/2/24.
 */

public class GoodsDetailFragment extends BaseFragment implements GoodsDetailContract.View {


    @Override
    public int getResLayout() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

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
    public void loadGoodsDetailSuccess(GoodsDetail goodsDetail) {

    }

    @Override
    public void loadGoodsDtailFailure(String msg) {

    }
}
