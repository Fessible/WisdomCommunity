package com.example.com.wisdomcommunity.ui.forget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.com.support_business.api.CommunityServer;
import com.example.com.support_business.api.RestyServer;
import com.example.com.support_business.domain.login.User;
import com.example.com.support_business.module.Entity;
import com.example.com.support_business.module.ResultEntity;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.mvp.ForgetContract;
import com.example.com.wisdomcommunity.util.VerifyUtils;

import java.util.Date;

/**
 * Created by rhm on 2018/3/21.
 */

public class ForgetPresenter extends ForgetContract.Presenter {

    public ForgetPresenter(Context context, ForgetContract.View view) {
        super(context, view);
    }

    public void forgetPassword(String sms, CharSequence phone, CharSequence oldPassword, CharSequence newPassword) {
        if (checkPass(oldPassword, newPassword)) {
            forgetPassword(sms, phone.toString(), newPassword.toString());
        }
    }

    @Override
    public void forgetPassword(String sms, final String phone, final String password) {
        if (destroyFlag.get()) {
            return;
        }

        showProgress();
        CommunityServer.with(context).forgetPassword(compositeTag, sms, phone.toString(), password.toString(), new RestyServer.SSOCallback<Entity>() {
            @Override
            public void onUnauthorized() {

            }

            @Override
            public void onResponse(Date receivedDate, Date servedDate, Entity entity) {
                hideProgress();
                if (entity != null) {
                    if (entity.isOk()) {
                        if (view != null) {
                            view.editForgetPasswordSuccess(phone, password, entity.msg);
                        }
                    } else {
                        if (view != null) {
                            view.editForgetPassFailure(!TextUtils.isEmpty(entity.msg) ? entity.msg : serverResponseError);
                        }
                    }
                } else {
                    if (view != null) {
                        view.editForgetPassFailure(networkError);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgress();
                if (view != null) {
                    view.editForgetPassFailure(networkError);
                }
            }
        });
    }

    private boolean checkPass(final CharSequence password, CharSequence oldPassword) {

        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 12) {
            // 密码长度小于6或大于12
            Toast.makeText(context, R.string.password_length_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!VerifyUtils.matcherPassword(String.valueOf(password), 6, 12, true)) {
            // 密码格式有误
            Toast.makeText(context, R.string.password_error_format, Toast.LENGTH_SHORT).show();

            return false;
        }
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(context, R.string.please_enter_new_pass, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(oldPassword)) {
            Toast.makeText(context, R.string.enter_error_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
