package com.example.com.wisdomcommunity.ui.person.address.add;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.personal.Address;
import com.example.com.support_business.module.Entity;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.mvp.EditAddressContract;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rhm on 2018/3/13.
 */

public class AddAddressPresenter extends EditAddressContract.Presenter {
    AddAddressPresenter(Context context, EditAddressContract.View view) {
        super(context, view);
    }

    @Override
    public void save(final Address address) {
        if (destroyFlag.get()) {
            return;
        }

        if (!isMobile(address.phone)) {
            Toast.makeText(context, R.string.phone_error,Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();
        CommunityServer.with(context).editAddress(compositeTag, address, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {
                hideProgress();
            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.saveSuccess(entity.msg, address);
                        }
                    } else {
                        if (view != null) {
                            view.saveFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.saveFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null)
                    view.saveFailure(networkError);
            }
        });
    }

    // 判断手机号码是否正确
    public boolean isMobile(String phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    public void delete(final String addressId) {
        if (destroyFlag.get()) {
            return;
        }
        showProgress();
        CommunityServer.with(context).deleteAddress(compositeTag, addressId, new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {
                hideProgress();
            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.deleteSuccess(entity.msg, addressId);
                        }
                    } else {
                        if (view != null) {
                            view.deleteFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.deleteFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null)
                    view.deleteFailure(networkError);
            }
        });
    }
}
