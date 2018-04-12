package com.example.com.support_business.params;

import com.example.com.support_business.Constants;

/**
 * 编辑个人信息
 * Created by rhm on 2018/1/16.
 */

public class PersonParams {
    /**
     * 小区名称
     */
    public String districtName ;

    /**
     * 昵称
     */
    public String userName;

    /**
     * 用户Id
     */
    public String userId;

    /**
     * 性别
     */
    @Constants.Gender
    public int sex;

    /**
     * 个性签名
     */
    public String signature;
}

