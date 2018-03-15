package com.example.com.support_business.domain.order;


import com.example.com.support_business.Constants;

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
    public String address;

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
    @Constants.STATUS
    public int orderStatus;

    /**
     * 下单时间
     */
    public long orderTime;

    /**
     * 支付金额
     */
    public String pay;

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
    public int shipment;

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

    /**
     * 店铺ID
     */
    public String shopId;


    public static class Order implements Serializable {
        /**
         * 商品图片
         */
        public String goodsUrl;
        /**
         * 商品ID
         */
        public String goodsId;
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

        /**
         * 剩余量
         */
        public int remain;

        public Order() {

        }

        public Order(String goodsId, String goodsName, int number, String price,int remain) {
            this.goodsName = goodsName;
            this.goodsId = goodsId;
            this.number = number;
            this.price = price;
            this.remain = remain;
        }
    }
}
