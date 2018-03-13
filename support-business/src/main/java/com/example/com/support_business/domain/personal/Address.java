package com.example.com.support_business.domain.personal;

import com.example.com.support_business.Constants;

import java.io.Serializable;

/**
 * 地址管理
 * Created by rhm on 2018/1/16.
 */

public class Address implements Serializable {
    /**
     * 地址
     */
    public String address;

    /**
     * 姓名
     */
    public String name;

    /**
     * 电话
     */
    public String phone;
    /**
     * 小区
     */
    public String district;
    /**
     * 性别
     */
    @Constants.Gender
    public int sex;

    /**
     * 地址ID
     */
    public String addressId;
}
