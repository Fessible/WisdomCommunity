package com.example.com.support_business.domain.search;

import java.io.Serializable;

/**
 * Created by rhm on 2018/3/8.
 */

public class Search implements Serializable {
    /**
     * 商品ID
     */
    public String goodsId;
    /**
     * 商品名称
     */
    public String goodsName;
    /**
     * 商品图标
     */
    public String goodsUrl;
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
