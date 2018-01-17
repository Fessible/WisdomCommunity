package com.example.com.support_business.domain.order;

import com.example.com.support_business.Constants;

import java.io.Serializable;

/**
 * 订单记录
 * Created by rhm on 2018/1/16.
 */

public class OrderRecord implements Serializable {
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
     * 总价
     */
    public String total;
}
