package com.example.com.wisdomcommunity.ui.person.set;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rhm on 2018/2/28.
 */

public class SetAdapter extends BaseAdapter<SetAdapter.SetHolder> {
    public static final int TYPE_SHARE = 0;//分享
    public static final int TYPE_ABOUT = 1;//关于
    public static final int TYPE_CACHE = 2;//缓存
    public static final int TYPE_UPDATE = 3;//更新

    private OnItemClickListener clickListener;
    private final List<SetItem> setItemList = new ArrayList<>();
    private Context mContext;

    public SetAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            setItemList.add(new SetItem(mContext.getString(R.string.set_share), TYPE_SHARE));
            setItemList.add(new SetItem(mContext.getString(R.string.set_cache), TYPE_CACHE));
            setItemList.add(new SetItem(mContext.getString(R.string.set_about), TYPE_ABOUT));
            setItemList.add(new SetItem(mContext.getString(R.string.set_update), TYPE_UPDATE));
        }
    };

    @Override
    protected void destroy() {
        setItemList.clear();
        unregisterAdapterDataObserver(observer);
    }

    @Override
    public SetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SetHolder holder = new SetHolder(mContext, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(SetHolder holder, int position) {
        holder.bindHolder(mContext, setItemList.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return setItemList.size();
    }


    static class SetItem {
        private String title;
        private int type;

        public SetItem(String title, int type) {
            this.title = title;
            this.type = type;
        }
    }

    static class SetHolder extends BaseHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.right_arrow)
        TextView rightArrow;

        @BindView(R.id.number)
        TextView cacheNumber;

        public SetHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_set);
        }

        public void bindHolder(Context context, final SetItem item, final OnItemClickListener listener) {
            title.setText(item.title);
            switch (item.type) {
                case TYPE_ABOUT:
                case TYPE_SHARE:
                    rightArrow.setVisibility(View.VISIBLE);
                    cacheNumber.setVisibility(View.GONE);
                    break;
                case TYPE_UPDATE:
                case TYPE_CACHE:
                    rightArrow.setVisibility(View.GONE);
                    cacheNumber.setVisibility(View.VISIBLE);
                    break;
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item.type);
                    }
                }
            });
        }

    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    interface OnItemClickListener {
        void onItemClick(int type);
    }
}
