package com.example.com.wisdomcommunity;

import android.os.Bundle;
import android.util.Log;

import com.example.com.wisdomcommunity.base.BaseActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rhm on 2018/3/4.
 */

public class TestActivity extends BaseActivity {
    private String TAG = "http_test";
    @Override
    public int getResLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        String url = "http://39.108.158.246:8080/community/login/login.do";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+call+"_>");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: "+response);
            }
        });
    }
}
