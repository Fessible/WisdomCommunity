package com.example.com.wisdomcommunity.ui.person.address.add;

import android.content.Context;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.mvp.EditAddressContract;

/**
 * Created by rhm on 2018/3/13.
 */

public class AddAddressPresenter extends EditAddressContract.Presenter {
    public AddAddressPresenter(Context context, EditAddressContract.View view) {
        super(context, view);
    }

    @Override
    public void save(Address address) {

    }

    @Override
    public void delete(String addressId) {

    }
}
