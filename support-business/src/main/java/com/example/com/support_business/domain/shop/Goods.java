package com.example.com.support_business.domain.shop;

import java.io.Serializable;

/**
 * 商品列表
 * Created by rhm on 2018/1/16.
 */

public class Goods implements Serializable {
    /**
     * 商品ID
     */
    public String goodsId;

    /**
     * 商品名称
     */
    public String goodsName;

    /**
     * 商品图片
     */
    public String goodsUrl;

    /**
     * 商品价格
     */
    public String price;
    /**
     * 已售数量
     */
    public int sale;

    /**
     * 规格
     */
    public String standard;

    /**
     * 剩余量
     */
    public int remain;

}
