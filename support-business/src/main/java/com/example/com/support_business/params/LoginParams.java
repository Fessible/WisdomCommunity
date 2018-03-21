package com.example.com.support_business.params;

/**
 * 登录
 * Created by rhm on 2018/1/16.
 */

public class LoginParams {
    /**
     * 密码
     */
    public String password;
    /**
     * 手机号
     */
    public String phone;

    public LoginParams(String phone, String password) {
        this.password = password;
        this.phone = phone;
    }
}
