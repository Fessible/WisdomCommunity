package com.example.com.support_business.domain.home;

import java.io.Serializable;

/**
 * 商品推荐
 * Created by rhm on 2018/1/16.
 */

public class Recommand implements Serializable {
    /**
     * 商品图片
     */
    public String goodsUrl;

    /**
     * 商品名称
     */
    public String name;

    /**
     * 商品价格
     */
    public String price;
}
