package com.example.com.support_business.domain.home;

import java.io.Serializable;

/**
 * 商品推荐
 * Created by rhm on 2018/1/16.
 */

public class Recommend implements Serializable {
    /**
     * 商品图片
     */
    public String goodsUrl;

    /**
     * 商品名称
     */
    public String name;

    /**
     * 商品ID
     */
    public String goodsId;

    /**
     * 店铺Id
     */
    public String shopId;
    /**
     * 店铺名
     */
    public String shopName;
}
