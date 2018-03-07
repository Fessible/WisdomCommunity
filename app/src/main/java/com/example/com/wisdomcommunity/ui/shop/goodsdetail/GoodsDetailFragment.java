package com.example.com.wisdomcommunity.ui.shop.goodsdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.GoodsDetailContract;

import butterknife.BindView;

/**
 * Created by rhm on 2018/2/24.
 */

public class GoodsDetailFragment extends BaseFragment implements GoodsDetailContract.View {
    public static final String TAG_GOODS_DETAIL_FRAGMENT = "GOODS_DETAIL_FRAGMENT";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    private GoodsDetailPresenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new GoodsDetailPresenter(getContext(), GoodsDetailFragment.this);
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
    public void loadGoodsDetailSuccess(GoodsDetail goodsDetail) {

    }

    @Override
    public void loadGoodsDtailFailure(String msg) {

    }
}
