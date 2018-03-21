package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rhm on 2018/3/20.
 */

public class CategoryBean {
    /**
     * 类型名称
     */
    public String name;

    /**
     * 类型
     */
    @CategoryType
    public int categoryType;

    /**
     * 选择的数量
     */
    public int count;


    public final static int TYPE_ALL = 0;//全部商品
    public final static int TYPE_DISCOUNT = 1;//打折商品

    public CategoryBean(String name, int type) {
        this.name = name;
        this.categoryType = type;
    }

    @IntDef({TYPE_ALL, TYPE_DISCOUNT})

    @Retention(RetentionPolicy.SOURCE)
    @interface CategoryType {
    }
}
