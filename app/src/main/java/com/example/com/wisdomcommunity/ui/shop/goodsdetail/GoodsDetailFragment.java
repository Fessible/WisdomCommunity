package com.example.com.wisdomcommunity.ui.shop.goodsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.home.Recommend;
import com.example.com.support_business.domain.home.RecommendGoods;
import com.example.com.support_business.domain.order.OrderDetail;
import com.example.com.support_business.domain.shop.Goods;
import com.example.com.support_business.domain.shop.GoodsDetail;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.GoodsDetailContract;
import com.example.com.wisdomcommunity.ui.home.HomeFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.home.HomeFragment.HOME_TAG;
import static com.example.com.wisdomcommunity.ui.home.HomeFragment.KEY_HOME;
import static com.example.com.wisdomcommunity.ui.home.HomeFragment.KEY_RECOMMEND;
import static com.example.com.wisdomcommunity.ui.home.HomeFragment.KEY_RECOMMEND_GOODS;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_GOODS_NUM;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_POSITION;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopGoodsFragment.KEY_GOODS;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopGoodsFragment.KEY_TYPE;

/**
 * Created by rhm on 2018/2/24.
 */

public class GoodsDetailFragment extends BaseFragment implements GoodsDetailContract.View {
    public static final String TAG_GOODS_DETAIL_FRAGMENT = "GOODS_DETAIL_FRAGMENT";
    public static final String KEY_GOOD_ORDER = "goods_order";
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
    private String name;
    private int count;
    private int leave;
    private String strPrice;
    private int position;
    private int homeTag = 0;
    private int type = 0;
    private Goods goods;
    private RecommendGoods recommendGoods;
    private Recommend recommend;

    @Override
    public int getResLayout() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(navigationListener);
        Bundle bundle = getArguments();
        if (bundle != null) {
            goods = (Goods) bundle.getSerializable(KEY_GOODS);
            name = bundle.getString(KEY_GOODS_NAME);
            type = bundle.getInt(KEY_TYPE);
            goodId = bundle.getString(KEY_GOODS_ID);
            count = bundle.getInt(KEY_GOODS_NUM);
            position = bundle.getInt(KEY_POSITION);
            title.setText(name);
            homeTag = bundle.getInt(KEY_HOME);

            recommendGoods = (RecommendGoods) bundle.getSerializable(KEY_RECOMMEND);
            recommend = (Recommend) bundle.getSerializable(KEY_RECOMMEND_GOODS);
        }
        if (count > 0) {
            buyLayout.setVisibility(View.VISIBLE);
            buy.setVisibility(View.GONE);
            number.setText(String.valueOf(count));
        } else {
            number.setText("0");
            buyLayout.setVisibility(View.GONE);
            buy.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data = new Intent();
            OrderDetail.Order order = new OrderDetail.Order(goodId, name, count, strPrice, leave);
            data.putExtra(KEY_TYPE, type);
            data.putExtra(KEY_GOODS, goods);
            data.putExtra(KEY_GOOD_ORDER, order);
            data.putExtra(KEY_POSITION, position);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    };

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
            leave = goodsDetail.remain;
            strPrice = goodsDetail.price;
        }

    }

    @OnClick(R.id.buy)
    public void buy() {
        if (homeTag != HOME_TAG) {
            buy.setVisibility(View.GONE);
            buyLayout.setVisibility(View.VISIBLE);
            count = 1;
            number.setText(String.valueOf(count));
        } else {
            //到店铺中
            if (recommendGoods != null) {
                Bundle shopArgs = new Bundle();
                shopArgs.putString(KEY_SHOP_ID, recommendGoods.shopId);
                shopArgs.putString(KEY_SHOP_NAME, recommendGoods.shopName);
                IntentUtil.startTemplateActivity(GoodsDetailFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
                getActivity().finish();
            } else if (recommend != null) {
                Bundle shopArgs = new Bundle();
                shopArgs.putString(KEY_SHOP_ID, recommend.shopId);
                shopArgs.putString(KEY_SHOP_NAME, recommend.shopName);
                IntentUtil.startTemplateActivity(GoodsDetailFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
                getActivity().finish();
            }
        }

    }

    @OnClick(R.id.minus)
    public void minus() {
        if (count > 0) {
            number.setText(String.valueOf(--count));
        }
    }

    @OnClick(R.id.add)
    public void add() {
        if (count < leave) {
            number.setText(String.valueOf(++count));
        } else {
            showShortToast(getString(R.string.no_enough_remain));
        }
    }

    @Override
    public void loadGoodsDtailFailure(String msg) {
        showShortToast(msg);
    }
}
