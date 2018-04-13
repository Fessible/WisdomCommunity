package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.support.annotation.IntDef;

import com.example.com.support_business.domain.shop.Goods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

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

    /**
     * 列表
     */
    public List<Goods> goodsList;


    public final static int TYPE_ALL = 0;//全部商品
    public final static int TYPE_DISCOUNT = 1;//打折商品

    public CategoryBean(String name, int type, List<Goods> goodsList) {
        this.name = name;
        this.categoryType = type;
        this.goodsList = goodsList;
    }

    @IntDef({TYPE_ALL, TYPE_DISCOUNT})

    @Retention(RetentionPolicy.SOURCE)
    @interface CategoryType {
    }
}
