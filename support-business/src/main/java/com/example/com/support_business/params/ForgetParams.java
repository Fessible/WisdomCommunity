package com.example.com.support_business.params;

/**
 * Created by rhm on 2018/3/21.
 */

public class ForgetParams {
    /**
     * 验证码
     */
//    public String smsCode;

    public String phone;

    public String password;

    public ForgetParams(String smsCode, String phone, String password) {
//        this.smsCode = smsCode;
        this.password = password;
        this.phone = phone;
    }
}
