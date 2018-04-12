package com.example.com.wisdomcommunity.ui.category;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.com.support_business.domain.home.Category;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.CategoryContract;
import com.example.com.wisdomcommunity.ui.shop.ShopAdapter;
import com.example.com.wisdomcommunity.ui.shop.ShopFragment;
import com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.MultipleRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshWizard;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;

import static com.example.com.support_business.Constants.CATEGORY_DRINK;
import static com.example.com.support_business.Constants.CATEGORY_FLOWER;
import static com.example.com.support_business.Constants.CATEGORY_FOOD;
import static com.example.com.support_business.Constants.CATEGORY_FRUIT;
import static com.example.com.wisdomcommunity.ui.home.HomeFragment.CATEGORY;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_HEADER;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_STANDARD;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_ID;
import static com.example.com.wisdomcommunity.ui.shop.ShopFragment.KEY_SHOP_NAME;

/**
 * Created by rhm on 2018/4/11.
 */

public class CategoryFragment extends BaseFragment implements CategoryContract.View {

    public static final String TAG_CATEGORY_FRAGMENT = "CATEGORY_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @BindView(R.id.multiple_refresh_layout)
    MultipleRefreshLayout multipleRefreshLayout;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CategoryContract.Presenter presenter;
    private CategoryAdapter adapter;
    private int category;

    @Override
    public int getResLayout() {
        return R.layout.fragment_template_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter = new CategoryPresenter(getContext(), CategoryFragment.this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getInt(CATEGORY);
            title.setText(getTitle(category));
            presenter.onShopList(category);
        }

        multipleRefreshLayout.setRefreshWizard(new SwipeRefreshWizard(getContext(), multipleRefreshLayout));
        multipleRefreshLayout.setOnRefreshListener(onRefreshListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        adapter.setCallback(callback);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private String getTitle(int type) {
        String title = null;
        switch (type) {
            case CATEGORY_FOOD:
                title = getString(R.string.food);
                break;
            case CATEGORY_DRINK:
                title = getString(R.string.drink);
                break;
            case CATEGORY_FLOWER:
                title = getString(R.string.flower);
                break;
            case CATEGORY_FRUIT:
                title = getString(R.string.fruit);
                break;
        }
        return title;
    }

    private CategoryAdapter.Callback callback = new CategoryAdapter.Callback() {
        @Override
        public void onItemClick(Category category) {
            Bundle shopArgs = new Bundle();
            shopArgs.putString(KEY_SHOP_ID, category.shopId);
            shopArgs.putString(KEY_SHOP_NAME, category.shopName);
            IntentUtil.startTemplateActivity(CategoryFragment.this, ShopDetailFragment.class, shopArgs, ShopDetailFragment.TAG_SHOP_DETAIL_FRAGMENT);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.onShopList(category);
        }
    };

    @Override
    protected void destroyView() {
        if (adapter != null) {
            adapter.destroy();
        }
    }

    @Override
    public void onLoadCategorySuccess(List<Category> categoryList) {
        if (categoryList != null && !categoryList.isEmpty()) {
            showMultipleContentLayout();
            adapter.setData(categoryList);
            adapter.notifyDataSetChanged();
        } else {
            showMultipleEmptyLayout();
        }
    }

    private void showMultipleEmptyLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showEmpty();
        }
    }

    @Override
    public void onLoadCategoryFailure(String msg) {
        showMultipleErrorLayout();
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
}
