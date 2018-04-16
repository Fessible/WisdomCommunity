package com.example.com.wisdomcommunity.ui.person.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.localsave.AccountSetUp;
import com.example.com.wisdomcommunity.mvp.AddressContract;
import com.example.com.wisdomcommunity.ui.person.address.add.AddAddressFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.MultipleRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshLayout;
import com.example.com.wisdomcommunity.view.SwipeRefreshWizard;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.example.com.wisdomcommunity.ui.shop.pay.PayFragment.KEY_TYPE;
import static com.example.com.wisdomcommunity.ui.shop.pay.PayFragment.TYPE_CHOOSE;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_POSITION;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddressFragment extends BaseFragment implements AddressContract.View {
    public static final String TAG_ADDRESS_FRAGMENT = "ADDRESS_FRAGMENT";
    public static final int REQUEST_ADD = 0;
    public static final int REQUEST_EDIT = 1;
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final int TYPE_EDIT = 0;//修改地址
    public static final int TYPE_ADD = 1;//添加地址
    public static final int TYPE_DELETE = 2;//添加地址
    public static final String KEY_ADDRESS = "address";

    @BindView(R.id.multiple_refresh_layout)
    MultipleRefreshLayout multipleRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddressAdapter addressAdapter;
    private AddressPresenter presenter;
    private List<Address> addressList = new ArrayList<>();
    private int type;

    @Override
    public int getResLayout() {
        return R.layout.fragment_address_manager;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(KEY_TYPE);
        }

        multipleRefreshLayout.setRefreshWizard(new SwipeRefreshWizard(getContext(), multipleRefreshLayout));
        multipleRefreshLayout.setOnRefreshListener(onRefreshListener);

        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        addressAdapter = new AddressAdapter(getContext());
        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new AddressPresenter(getContext(), AddressFragment.this);
        presenter.loadAddress(AccountSetUp.getUserId(getContext()));
        addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Address address, int position) {
                if (type == TYPE_CHOOSE) {
                    Intent data = new Intent();
                    data.putExtra(KEY_ADDRESS, address);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }

            @Override
            public void editItem(Address address, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(TITLE, getContext().getString(R.string.edit_address));
                bundle.putInt(TYPE, TYPE_EDIT);
                bundle.putInt(KEY_POSITION, position);
                bundle.putSerializable(KEY_ADDRESS, address);
                IntentUtil.startTemplateActivityForResult(AddressFragment.this, AddAddressFragment.class, bundle, AddAddressFragment.TAG_ADD_ADDRESS_FRAGMENT, REQUEST_EDIT);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.loadAddress(AccountSetUp.getUserId(getContext()));
        }
    };

    @OnClick(R.id.add_layout)
    public void add() {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getContext().getString(R.string.title_add_address));
        bundle.putInt(TYPE, TYPE_ADD);
        IntentUtil.startTemplateActivityForResult(AddressFragment.this, AddAddressFragment.class, bundle, AddAddressFragment.TAG_ADD_ADDRESS_FRAGMENT, REQUEST_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EDIT:
                    int position = data.getIntExtra(KEY_POSITION, 0);
                    int type = data.getIntExtra(TYPE, 0);
                    switch (type) {
                        case TYPE_DELETE:
                            if (addressAdapter != null) {
                                addressAdapter.removeItem(position);
                            }
                            break;
                        case TYPE_EDIT:
                            Address address = (Address) data.getSerializableExtra(KEY_ADDRESS);
                            if (addressAdapter != null) {
                                addressAdapter.setItemData(address, position);
                            }
                            break;
                    }
                    break;
                case REQUEST_ADD:
                    Address address = (Address) data.getSerializableExtra(KEY_ADDRESS);
                    if (addressList != null) {
                        addressList.add(address);
                        notifyData();
                    }
                    break;
            }
        }
    }

    private void notifyData() {
        if (addressAdapter != null) {
            addressAdapter.setData(addressList);
            addressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void destroyView() {
        addressAdapter.destroy();
    }

    private void showMultipleEmptyLayout() {
        if (multipleRefreshLayout != null) {
            multipleRefreshLayout.showEmpty();
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
    public void onLoadAddressSuccess(List<Address> addressList) {
        this.addressList = addressList;
        notifyData();
    }

    @Override
    public void onLoadAddressFailure(String msg) {
        showShortToast(msg);
        if (addressAdapter != null) {
            addressAdapter.notifyDataSetChanged();
        }
    }
}
