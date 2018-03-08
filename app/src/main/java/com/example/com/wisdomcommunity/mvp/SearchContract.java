package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.search.Search;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.util.List;

/**
 * Created by rhm on 2018/3/8.
 */

public interface SearchContract {
    interface Model extends BasicContract.Model {

    }

    interface View extends BasicContract.View {
        void loadSearchSuccess(List<Search> search);

        void loadSearchFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View> {
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void loadSearch(String search);
    }

}
