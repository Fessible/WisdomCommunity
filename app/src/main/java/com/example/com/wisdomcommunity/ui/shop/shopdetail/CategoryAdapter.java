package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhm on 2018/3/20.
 */

public class CategoryAdapter extends BaseAdapter {
    private List<CategoryBean> categoryList = new ArrayList<>();
    private Context context;
    private int selection = 0;

    public CategoryAdapter(Context context, List<CategoryBean> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_category, null);
            viewholder = new Viewholder();
            viewholder.catogray = view.findViewById(R.id.tv_catogray);
            viewholder.count = view.findViewById(R.id.tv_count);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }

        //设置值
        viewholder.catogray.setText(categoryList.get(i).name);
        int count = categoryList.get(i).count;
        if (count > 0) {
            viewholder.count.setVisibility(View.VISIBLE);
            viewholder.count.setText(String.valueOf(count));
        } else {
            viewholder.count.setVisibility(View.GONE);
        }
        if (i == selection) {
            viewholder.catogray.setBackgroundResource(R.drawable.rect_left_blue);
            viewholder.catogray.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            viewholder.catogray.setBackgroundResource(R.drawable.empty_background);
            viewholder.catogray.setTextColor(context.getResources().getColor(R.color.gray));
        }
        return view;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    class Viewholder {
        TextView catogray;
        TextView count;
    }
}
