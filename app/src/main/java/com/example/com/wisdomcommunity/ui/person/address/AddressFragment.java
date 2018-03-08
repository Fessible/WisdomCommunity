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
import com.example.com.wisdomcommunity.mvp.AddressContract;
import com.example.com.wisdomcommunity.ui.person.address.add.AddAddressFragment;
import com.example.com.wisdomcommunity.util.IntentUtil;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddressFragment extends BaseFragment implements AddressContract.View {
    public static final String TAG_ADDRESS_FRAGMENT = "ADDRESS_FRAGMENT";
    public static final int REQUEST_CODE = 1;
    public static final String TITLE = "title";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddressAdapter addressAdapter;
    private AddressPresenter presenter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_address_manager;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());
        addressAdapter = new AddressAdapter(getContext());
        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new AddressPresenter(getContext(), AddressFragment.this);
        presenter.loadAddress("123");
        addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Bundle bundle = new Bundle();
                bundle.putString(TITLE,getContext().getString(R.string.edit_address));
                IntentUtil.startSecondActivityForResult(AddressFragment.this, AddAddressFragment.class, bundle,AddAddressFragment.TAG_ADD_ADDRESS_FRAGMENT,REQUEST_CODE);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    @OnClick(R.id.add_layout)
    public void add() {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE,getContext().getString(R.string.title_add_address));
        IntentUtil.startSecondActivityForResult(AddressFragment.this, AddAddressFragment.class, bundle,AddAddressFragment.TAG_ADD_ADDRESS_FRAGMENT,REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void destroyView() {
        addressAdapter.destroy();
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
    public void onLoadAddressSuccess(List<Address> addressList) {
        addressAdapter.setData(addressList);
        addressAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadAddressFailure(String msg) {
        showShortToast(msg);
    }
}
