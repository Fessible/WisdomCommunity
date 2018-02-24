package com.example.com.wisdomcommunity.mvp.base;

import android.content.Context;

import com.example.com.wisdomcommunity.R;

/**
 * Created by rhm on 2018/1/16.
 */

public interface BasicContract {
    public interface Model extends MVPContract.Model {

    }

    public interface View extends MVPContract.View {
        void showProgress();

        void hideProgress();

        void onUnauthorized();
    }


    abstract class Presenter<V extends View> implements MVPContract.Presenter {
        protected Context context;
        protected V view;
        protected final String networkError;
        protected final String serverResponseError;

        public Presenter(Context context, V view) {
            this.context = context;
            this.view = view;
            this.networkError = context.getString(R.string.text_network_error);
            this.serverResponseError = context.getString(R.string.text_server_error);
        }

        protected final void showProgress() {
            if (view != null) {
                view.showProgress();
            }
        }

        protected final void hideProgress() {
            if (view != null) {
                view.hideProgress();
            }
        }

        public void destroy() {
            context = null;
            view = null;
        }

        protected boolean isActivityLife() {
            return view != null;
        }
    }
}
