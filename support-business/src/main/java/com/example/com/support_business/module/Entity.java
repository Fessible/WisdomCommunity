package com.example.com.support_business.module;

/**
 * Created by rhm on 2018/1/16.
 */

public class Entity {
    public static final int STATUS_OK = 0;
    public static final int STATUS_FAIL = 1;
    /**
     * 0：成功，1:失败
     * 大于等于0是表示服务端返回异常
     * 小于0表示特定异常
     */
    public int status;
    public String msg;

    public boolean isOk() {
        return status == STATUS_OK;
    }

    public boolean isServerError() {
        return status > 0;
    }

    public boolean isSpecialError() {
        return status < 0;
    }
}
