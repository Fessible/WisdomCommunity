package com.example.com.wisdomcommunity.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.com.support_business.domain.search.Search;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.SearchContract;
import com.example.com.wisdomcommunity.ui.shop.goodsdetail.GoodsDetailFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_GOODS_NAME;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;

/**
 * Created by rhm on 2018/3/8.
 */

public class SearchFragment extends BaseFragment implements SearchContract.View {
    public static final String TAG_SEARCH_FRAGMENT = "SEARCH_FRAGMENT";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_view)
    EditText searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchAdapter searchAdapter;
    private SearchContract.Presenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_shop_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toolbar.setNavigationIcon(R.drawable.selector_btn_back);
        toolbar.setNavigationOnClickListener(onNavigationListener);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build())
                .build());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(getContext());
        searchAdapter.setCallback(callback);
        recyclerView.setAdapter(searchAdapter);

        searchView.addTextChangedListener(textWatcher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new SearchPresenter(getContext(), SearchFragment.this);
    }

    private SearchAdapter.Callback callback = new SearchAdapter.Callback() {
        @Override
        public void onSearchItem(String search) {
            if (!TextUtils.isEmpty(search) && presenter != null) {
                presenter.loadSearch(search);
            }
        }

        @Override
        public void onShopItem(String shopID, String shopName) {
            Bundle shopArgs = new Bundle();
            shopArgs.putString(KEY_SHOP_ID, shopID);
            shopArgs.putString(KEY_SHOP_NAME, shopName);
            IntentUtil.startTemplateActivity(SearchFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
        }

        @Override
        public void onGoodsItem(String goodsID, String goodsName) {
            Bundle goodsArgs = new Bundle();
            goodsArgs.putString(KEY_GOODS_ID, goodsID);
            goodsArgs.putString(KEY_GOODS_NAME, goodsName);
            IntentUtil.startTemplateActivity(SearchFragment.this, GoodsDetailFragment.class, goodsArgs, GoodsDetailFragment.TAG_GOODS_DETAIL_FRAGMENT);
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchAdapter.setStandardData(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnClickListener onNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };

    @Override
    protected void destroyView() {
        if (presenter != null) {
            presenter.destroy();
        }

    }

    @Override
    public void loadSearchSuccess(List<Search> search) {
        if (searchAdapter != null) {
            if (search != null && !search.isEmpty()) {
                searchAdapter.setData(search);
            } else {
                searchAdapter.setEmptyData();
            }
        }

    }

    @Override
    public void loadSearchFailure(String msg) {

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
}
