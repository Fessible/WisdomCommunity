package com.example.com.support_business.api;

import android.content.Context;

import com.example.com.support_business.SeverHelper;

/**
 * 网络请求处理
 * Created by rhm on 2018/1/16.
 */

public class CommunityServer {
    private Context context;
    private CommunityApi communityApi;

    private CommunityServer(Context context) {
        this.context = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        communityApi = SeverHelper.with().newRetrofit().create(CommunityApi.class);
    }
}
