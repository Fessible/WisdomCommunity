package com.example.com.wisdomcommunity.ui.search;

import android.support.annotation.IntDef;

import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.search.SearchAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/3/8.
 */

public class SearchAdapter {
    interface Item {
        int VIEW_STANDARD = 0;
        int VIEW_EMPTY = 1;
    }

    @IntDef({
            VIEW_STANDARD,
            VIEW_EMPTY
    })
    @interface ViewType {
    }
}
