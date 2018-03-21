package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/21.
 */

public interface ForgetContract  {
    public interface Model extends BasicContract.Model {

    }

    public interface View extends BasicContract.View {
        void editForgetPasswordSuccess(String phone, String password, String msg);

        void editForgetPassFailure(String msg);
    }

    public abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void forgetPassword(String sms, String phone, String password);
    }

}
