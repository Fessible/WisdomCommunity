package com.example.com.wisdomcommunity.ui.shop.goodsdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.GoodsDetailContract;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;

/**
 * Created by rhm on 2018/2/24.
 */

public class GoodsDetailFragment extends BaseFragment implements GoodsDetailContract.View {
    public static final String TAG_GOODS_DETAIL_FRAGMENT = "GOODS_DETAIL_FRAGMENT";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.img_goods)
    ImageView imgGoods;

    @BindView(R.id.img_detail)
    ImageView imgDetail;

    @BindView(R.id.goods_name)
    TextView goodsName;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.remain)
    TextView remain;

    @BindView(R.id.standard)
    TextView standard;

    @BindView(R.id.buy_layout)
    RelativeLayout buyLayout;

    @BindView(R.id.buy)
    TextView buy;

    @BindView(R.id.number)
    TextView number;

    private GoodsDetailPresenter presenter;
    private String goodId;

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
        Bundle bundle = getArguments();
        String goodsName = bundle.getString(KEY_GOODS_NAME);
        goodId = bundle.getString(KEY_GOODS_ID);
        title.setText(goodsName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new GoodsDetailPresenter(getContext(), GoodsDetailFragment.this);
        presenter.loadGoodsDetail(goodId);
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
        if (goodsDetail != null) {
            int placeHolder = R.drawable.holder;
            Glide.with(getContext())
                    .load(goodsDetail.goodsUrl)
                    .apply(new RequestOptions().centerCrop().placeholder(placeHolder).fallback(placeHolder))
                    .into(imgGoods);

            Glide.with(getContext())
                    .load(goodsDetail.goodsUrl)
                    .apply(new RequestOptions().centerCrop().placeholder(placeHolder).fallback(placeHolder))
                    .into(imgDetail);
            goodsName.setText(goodsDetail.goodsName);
            price.setText(getContext().getString(R.string.sign_price, goodsDetail.price));
            standard.setText(getContext().getString(R.string.standard, goodsDetail.standard));
            remain.setText(getContext().getString(R.string.remain, String.valueOf(goodsDetail.remain)));
        }

    }

    @OnClick(R.id.buy)
    public void buy() {
        buy.setVisibility(View.GONE);
        buyLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.minus)
    public void minus() {

    }

    @OnClick(R.id.add)
    public void add() {

    }

    @Override
    public void loadGoodsDtailFailure(String msg) {
        showShortToast(msg);
    }
}
