package com.example.com.support_business.domain.shop;

import java.io.Serializable;

/**
 * 商品详情
 * Created by rhm on 2018/1/16.
 */

public class GoodsDetail implements Serializable {
    /**
     * 商品详情图片
     */
    public String goodsDetailUrl;
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
}
