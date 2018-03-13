package com.example.com.wisdomcommunity.ui.person.address.add;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseFragment;
import com.example.com.wisdomcommunity.mvp.EditAddressContract;
import com.example.com.wisdomcommunity.view.itemdecoration.DividerDecor;
import com.example.com.wisdomcommunity.view.itemdecoration.FlexibleItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.KEY_ADDRESS;
import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.TITLE;
import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.TYPE;
import static com.example.com.wisdomcommunity.ui.person.address.AddressFragment.TYPE_ADD;
import static com.example.com.wisdomcommunity.ui.shop.shopdetail.ShopDetailFragment.KEY_POSITION;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddAddressFragment extends BaseFragment implements EditAddressContract.View {
    public static final String TAG_ADD_ADDRESS_FRAGMENT = "ADD_ADDRESS_FRAGMENT";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.delete)
    TextView delete;

    private AddAddressAdapter addAddressAdapter;
    private String strTitle;
    private int type;
    private int position;
    private Address address;
    private EditAddressContract.Presenter presenter;
    private String addressId;

    @Override
    public int getResLayout() {
        return R.layout.fragment_add_address;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        addAddressAdapter = new AddAddressAdapter(getContext());


        Bundle bundle = getArguments();
        if (bundle != null) {
            strTitle = bundle.getString(TITLE);
            type = bundle.getInt(TYPE);
            position = bundle.getInt(KEY_POSITION);
            address = (Address) bundle.getSerializable(KEY_ADDRESS);
            addressId = address.addressId;
            if (addAddressAdapter != null) {
                addAddressAdapter.setData(address);
            }
        }
        title.setText(strTitle);
        delete.setVisibility(type == TYPE_ADD ? View.GONE : View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(addAddressAdapter);
        recyclerView.addItemDecoration(new FlexibleItemDecoration.Builder(getContext())
                .defaultDecor(new DividerDecor.Builder(getContext())
                        .divider(getResources().getDrawable(R.drawable.icon_horizontal_line))
                        .build()).build());

        addAddressAdapter.notifyDataSetChanged();
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
        presenter = new AddAddressPresenter(getContext(), AddAddressFragment.this);
    }

    @OnClick(R.id.save)
    public void save() {
        if (addAddressAdapter != null && presenter != null) {
            if (TextUtils.isEmpty(addAddressAdapter.getName())) {
                showShortToast(getString(R.string.please_input_name));
                return;
            }
            if (TextUtils.isEmpty(addAddressAdapter.getPhone())) {
                showShortToast(getString(R.string.please_input_phone));
                return;
            }
//            if (TextUtils.isEmpty(addAddressAdapter.getDistrict())) {
//                showShortToast(getString(R.string.please_input_district));
//                return;
//            }
            if (TextUtils.isEmpty(addAddressAdapter.getDetailAddress())) {
                showShortToast(getString(R.string.please_input_address));
                return;
            }

            Address address = new Address();
            address.sex = addAddressAdapter.getSex();
            address.addressId = addressId;
            address.name = addAddressAdapter.getName();
            address.phone = addAddressAdapter.getPhone();
            address.district = addAddressAdapter.getDistrict();
            address.address = addAddressAdapter.getDetailAddress();
            presenter.save(address);
        }
    }

    @OnClick(R.id.delete)
    public void delete() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.wheather_delete_address)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (presenter != null) {
                            presenter.delete(addressId);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.opt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void destroyView() {
        addAddressAdapter.destroy();
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
    public void saveSuccess(String msg, Address address) {
        showShortToast(msg);
        if (address != null) {
            Intent intent = new Intent();
            intent.putExtra(KEY_ADDRESS, address);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void saveFailure(String msg) {
        showShortToast(msg);
    }

    @Override
    public void deleteSuccess(String msg, String addressId) {
        showShortToast(msg);
    }

    @Override
    public void deleteFailure(String msg) {
        showShortToast(msg);
    }
}
