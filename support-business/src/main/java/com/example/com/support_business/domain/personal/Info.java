package com.example.com.support_business.domain.personal;

import java.io.Serializable;

/**
 * 获取个人信息
 * Created by rhm on 2018/1/16.
 */

public class Info implements Serializable {
    /**
     * 小区名称
     */
    public String districtName;

    /**
     * 头像
     */
    public String headImage;

    /**
     * 性别
     */
    public int sex;

    /**
     * 个性签名
     */
    public String signature;
    /**
     * 用户名
     */
    public String userName;
}
