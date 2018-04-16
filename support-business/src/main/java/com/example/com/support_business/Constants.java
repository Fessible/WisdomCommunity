package com.example.com.support_business;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 常量
 * Created by rhm on 2018/1/16.
 */

public interface Constants {
    /**********************
     * 性别
     **********************/
    /**
     * 男
     */
    public static final int GENDER_MALE = 0;
    /**
     * 女
     */
    public static final int GENDER_FEMALE = 1;

    @IntDef({GENDER_MALE,
            GENDER_FEMALE})

    @Retention(RetentionPolicy.SOURCE)
    public @interface Gender {
    }

    /****************************
     * 订单状态
     ****************************/
    /**
     * 已完成
     */
    public static final int STATUS_FINISHED = 0;
    /**
     * 待配送
     */
    public static final int STATUS_SENDING = 1;
    /**
     * 待付款
     */
    public static final int STATUS_WAIT_PAY = 2;

    /**
     * 已提交
     */
    public static final int STATUS_SUBMIT = 3;

    /**
     * 退款
     */
    public static final int STATUS_RETURN = 4;

    @IntDef({
            STATUS_FINISHED,
            STATUS_SENDING,
            STATUS_WAIT_PAY,
            STATUS_SUBMIT,
            STATUS_RETURN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATUS {
    }

    //地址
    public static final int TYPE_NAME = 0;
    public static final int TYPE_PHONE = 1;
    public static final int TYPE_OTHER = 2;

    @IntDef({TYPE_NAME, TYPE_PHONE, TYPE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    //分类
    public static final int CATEGORY_FOOD = 0;
    public static final int CATEGORY_FLOWER = 1;
    public static final int CATEGORY_DRINK = 2;
    public static final int CATEGORY_FRUIT = 3;

    @IntDef({CATEGORY_FOOD, CATEGORY_FLOWER, CATEGORY_FRUIT, CATEGORY_DRINK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {

    }

}
