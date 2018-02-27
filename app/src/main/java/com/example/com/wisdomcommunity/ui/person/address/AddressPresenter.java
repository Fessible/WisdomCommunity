package com.example.com.wisdomcommunity.ui.person.address;

import android.content.Context;
import android.text.TextUtils;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.support_business.module.ListEntity;
import com.example.com.wisdomcommunity.mvp.AddressContract;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddressPresenter extends AddressContract.Presenter {
    private String compositeTag = UUID.randomUUID().toString();
    private AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public AddressPresenter(Context context, AddressContract.View view) {
        super(context, view);
    }

    @Override
    public void loadAddress(String userId) {
        if (destroyFlag.get()) {
            return;
        }

        showProgress();
        CommunityServer.with(context).address(compositeTag, false, userId, new RestyServer.SSOCallback<ListEntity<Address>>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, ListEntity<Address> entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.onLoadAddressSuccess(entity.result);
                        }
                    } else {
                        if (view != null) {
                            view.onLoadAddressFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.onLoadAddressFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.onLoadAddressFailure(networkError);
                }
            }
        });
    }


}
