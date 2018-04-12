package com.example.com.wisdomcommunity.mvp;

import android.content.Context;

import com.example.com.support_business.domain.home.Category;
import com.example.com.support_business.domain.shop.ShopList;
import com.example.com.wisdomcommunity.mvp.base.BasicContract;

import java.util.List;

/**
 * Created by rhm on 2018/4/11.
 */

public interface CategoryContract {
    interface Model extends BasicContract.Model {
    }

    interface View extends BasicContract.View {
        void onLoadCategorySuccess(List<Category> categoryList);

        void onLoadCategoryFailure(String msg);
    }

    abstract class Presenter extends BasicContract.Presenter<View>{
        public Presenter(Context context, View view) {
            super(context, view);
        }

        public abstract void onShopList(int type);
    }
}
