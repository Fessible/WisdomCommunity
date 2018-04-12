package com.example.com.support_business.params;

/**
 * Created by rhm on 2018/3/21.
 */

public class RegisterParams {
    /**
     * 用户名
     */
//    public String userName;

    /**
     * 验证码
     */
//    public String smsCode;

    /**
     * 电话号码
     */
    public String phone;

    /**
     * 密码
     */
    public String password;

    public RegisterParams(String name, String phone, String password) {
//        this.userName = name;
//        this.smsCode = smsCode;
        this.phone = phone;
        this.password = password;
    }
}
