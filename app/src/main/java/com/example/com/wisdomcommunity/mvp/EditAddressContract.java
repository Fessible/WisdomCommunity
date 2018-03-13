package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.personal.Address;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/13.
 */

public interface EditAddressContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        public void saveSuccess(String msg,Address address);

        public void saveFailure(String msg);

        public void deleteSuccess(String msg,String addressId);

        public void deleteFailure(String msg);
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void save(Address address);

        public abstract void delete(String addressId);
    }


}
