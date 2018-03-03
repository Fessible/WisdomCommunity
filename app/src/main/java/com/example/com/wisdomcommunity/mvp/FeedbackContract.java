package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.wisdomcommunity.mvp.base.BasicContract;

/**
 * Created by rhm on 2018/3/2.
 */

public interface FeedbackContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void submitSuccess(String msg);

        void submitFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void submitSuggestion(String suggestion);
    }

}
