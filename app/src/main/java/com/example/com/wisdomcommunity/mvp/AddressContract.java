package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.util.List;

/**
 * Created by rhm on 2018/2/27.
 */

public interface AddressContract extends BasicContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void onLoadAddressSuccess(List<Address> addressList);

        void onLoadAddressFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadAddress(String userId);
    }

}
