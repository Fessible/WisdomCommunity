package com.example.com.support_business.domain.shop;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺列表
 * Created by rhm on 2018/1/16.
 */

public class ShopList implements Serializable {
    /**
     * 商品列表
     */
    public List<Goods> goodsList;

    /**
     * 店铺ID
     */
    public String shopId;

    /**
     * 店铺名称
     */
    public String shopName;

    /**
     * 店铺图片
     */
    public String shopUrl;


}
