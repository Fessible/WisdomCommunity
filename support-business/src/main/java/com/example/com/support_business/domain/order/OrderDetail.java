package com.example.com.support_business.domain.order;


import java.io.Serializable;
import java.util.List;

/**
 * 订单详情
 * Created by rhm on 2018/1/16.
 */

public class OrderDetail implements Serializable {
    /**
     * 收货地址
     */
    public String addrress;

    /**
     * 商品清单
     */
    public List<Order> details;

    /**
     * 订单号
     */
    public String orderId;
    /**
     * 订单状态
     */
    public String orderStatus;

    /**
     * 下单时间
     */
    public long orderTime;

    /**
     * 支付金额
     */
    public String price;

    /**
     * 收货人
     */
    public String receiverName;
    /**
     * 收货人电话
     */
    public String receiverPhone;

    /**
     * 备注
     */
    public String remark;

    /**
     * 配送费
     */
    public String shipment;

    /**
     * 店铺名称
     */
    public String shopName;

    /**
     * 店铺电话
     */
    public String shopPhone;

    /**
     * 商品总价
     */
    public String total;


    public static class Order {
        /**
         * 商品名称
         */
        public String goodsName;
        /**
         * 商品数量
         */
        public int number;
        /**
         * 价格
         */
        public String price;
    }
}
