package com.example.com.wisdomcommunity;

import android.content.Context;

import com.example.com.support_business.domain.personal.Info;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/13.
 */

public interface InfoContract extends BasicContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void loadInfoSuccess(Info info);

        void loadInfoFailure(String msg);

    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadInfo(String userId);

    }

}

