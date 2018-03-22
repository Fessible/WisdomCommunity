package com.example.com.support_business.domain.shop;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺详情
 * Created by rhm on 2018/1/16.
 */

public class ShopDetail implements Serializable {
    /**
     * 店铺名称
     */
    public String shopName;

    /**
     * 店铺Id
     */
    public String shopId;

    /**
     * 折扣
     */
    public String discount;

    /**
     * 满减
     */
    public String minus;

    /**
     * 配送费
     */
    public int shipment;
    /**
     * 商品列表
     */
    public List<Goods> goodsList;

    /**
     * 打折商品
     */
    public List<Goods> discountList;

    /**
     * 店铺简介
     */
    public String info;

    /**
     * 店铺图片
     */
    public String shopUrl;

    /**
     * 营业时间
     */
    public String workTime;

    /**
     * 联系电话
     */
    public String shopPhone;
}
