package com.example.com.wisdomcommunity.ui.person.address.add;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.support_business.Constants;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.com.support_business.Constants.TYPE_NAME;
import static com.example.com.support_business.Constants.TYPE_OTHER;
import static com.example.com.support_business.Constants.TYPE_PHONE;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_CHOSE;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_SEX;
import static com.example.com.wisdomcommunity.ui.person.address.add.AddAddressAdapter.Item.VIEW_STANDARD;

/**
 * Created by rhm on 2018/2/26.
 */

public class AddAddressAdapter extends BaseAdapter<AddAddressAdapter.AddHolder> {

    private final static List<Item> itemList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public AddAddressAdapter(Context context) {
        this.mContext = context;
        registerAdapterDataObserver(adapter);
    }

    private RecyclerView.AdapterDataObserver adapter = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.add(new StandardItem(mContext.getString(R.string.name), mContext.getString(R.string.name_hint), TYPE_NAME));
            itemList.add(new SexItem());
            itemList.add(new StandardItem(mContext.getString(R.string.phone), mContext.getString(R.string.phont_hint), Constants.TYPE_PHONE));
            itemList.add(new ChoseItem());
            itemList.add(new StandardItem(mContext.getString(R.string.detail_address), mContext.getString(R.string.address_hint), Constants.TYPE_OTHER));
        }
    };


    @Override
    protected void destroy() {
        itemList.clear();
        unregisterAdapterDataObserver(adapter);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public AddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddHolder holder = null;
        switch (viewType) {
            case VIEW_STANDARD:
                holder = new StandardHolder(mContext, parent);
                break;
            case VIEW_CHOSE:
                holder = new ChoseHolder(mContext, parent);
                break;
            case VIEW_SEX:
                holder = new SexHolder(mContext, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AddHolder holder, int position) {
        holder.bindHolder(mContext, itemList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class StandardHolder extends AddHolder<StandardItem> {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.edit)
        TextView edit;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_standard);
        }

        @Override
        public void bindHolder(Context context, StandardItem Item, OnItemClickListener clickListener) {
            title.setText(Item.title);
            edit.setHint(Item.hint);
            switch (Item.type) {
                case TYPE_NAME:
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    break;
                case TYPE_PHONE:
                    edit.setInputType(InputType.TYPE_CLASS_PHONE);
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    break;
                case TYPE_OTHER:
                    break;
            }
        }
    }

    static class ChoseHolder extends AddHolder<ChoseItem> {
        public ChoseHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_chose_area);
        }

        @Override
        public void bindHolder(Context context, ChoseItem Item, OnItemClickListener clickListener) {
            if (clickListener != null) {
                clickListener.onItemClick();
            }
        }
    }

    static class SexHolder extends AddHolder<SexItem> {
        public SexHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_address_chose);
        }

        @Override
        public void bindHolder(Context context, SexItem Item, OnItemClickListener clickListener) {

        }
    }


    class StandardItem implements Item {
        private String title;
        private String hint;
        private int type;

        public StandardItem(String title, String hint, @Constants.Type int type) {
            this.title = title;
            this.hint = hint;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    class ChoseItem implements Item {

        @Override
        public int getViewType() {
            return VIEW_CHOSE;
        }
    }

    class SexItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_SEX;
        }
    }


    interface Item {

        int VIEW_STANDARD = 0;
        int VIEW_CHOSE = 1;
        int VIEW_SEX = 2;

        @IntDef({VIEW_STANDARD, VIEW_CHOSE, VIEW_SEX})
        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        public int getViewType();
    }

    static abstract class AddHolder<II extends Item> extends BaseHolder {

        public AddHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        public abstract void bindHolder(Context context, II Item, OnItemClickListener listener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

}
