package com.example.com.support_business.params;

import java.io.Serializable;

/**
 * Created by rhm on 2018/4/12.
 */
public class CategoryParams implements Serializable{
    public int category;

    public CategoryParams(int category) {
        this.category = category;
    }
}
