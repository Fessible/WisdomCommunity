package com.example.com.support_business.domain.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rhm on 2018/3/15.
 */

public class Home implements Serializable {
    /**
     * 推荐列表
     */
    public List<Recommend> list;
    /**
     * 新品推荐ID
     */
    public String recommendId;

    /**
     * 新品推荐图片
     */
    public String recommendUrl;

    /**
     * 秒杀ID
     */
    public String secondKillId;

    /**
     * 秒杀图片
     */
    public String secondKillUrl;
}
