package com.example.com.support_business.domain.shop;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺详情
 * Created by rhm on 2018/1/16.
 */

public class ShopDetail implements Serializable {
    /**
     * 商品列表
     */
    public List<Goods> goodsList;

    /**
     * 店铺简介
     */
    public String info;

    /**
     * 店铺图片
     */
    public String shopUrl;
}
