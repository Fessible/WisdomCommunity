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
    public RecommendGoods recommend;
    public RecommendGoods secondKill;
}
