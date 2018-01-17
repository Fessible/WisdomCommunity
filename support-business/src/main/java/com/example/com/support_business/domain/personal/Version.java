package com.example.com.support_business.domain.personal;

import java.io.Serializable;

/**
 * 版本更新
 * Created by rhm on 2018/1/16.
 */

public class Version implements Serializable{
    /**
     * 版本更新说明
     */
    public String directions;

    /**
     * 最新版本下载地址
     */
    public String latestVerUrl;
    /**
     * 最新版本号
     */
    public String latestVersion;
}
