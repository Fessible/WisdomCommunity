package com.example.com.wisdomcommunity.ui.person.info;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.com.support_business.domain.personal.Info;
import com.example.com.wisdomcommunity.R;
import com.example.com.wisdomcommunity.base.BaseAdapter;
import com.example.com.wisdomcommunity.base.BaseHolder;

import org.w3c.dom.Text;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.Item.VIEW_EMPTY;
import static com.example.com.wisdomcommunity.ui.person.info.EditInfoAdapter.Item.VIEW_STANDARD;
import static com.example.com.wisdomcommunity.ui.shop.ShopAdapter.Item.VIEW_HEADER;

/**
 * Created by rhm on 2018/2/28.
 */

public class EditInfoAdapter extends BaseAdapter<EditInfoAdapter.EditHolder> {
    final static int TYPE_HEAD_IMAGE = 0;
    final static int TYPE_NAME = 1;//用户名
    final static int TYPE_DISTRICT = 2;//小区名
    final static int TYPE_SIGNATURE = 3;//个性签名
    final static int TYPE_SEX = 4;//性别
    final static int TYPE_BACK = 5;//返回


    private final List<Item> itemList = new ArrayList<>();
    private OnItemClickListener clickListener;
    private Context context;
    private Info info;

    public EditInfoAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(observer);
    }

    public void setData(Info info) {
        this.info = info;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            itemList.add(new HeadItem(info != null ? info.headImage : null));
            itemList.add(new EmptyItem());
            itemList.add(new StandardItem(context.getString(R.string.user_name), info != null ? info.userName : "", TYPE_NAME));
            itemList.add(new StandardItem(context.getString(R.string.signature), info != null ? info.signature : "", TYPE_SIGNATURE));
            itemList.add(new StandardItem(context.getString(R.string.district), info != null ? info.districtName : "", TYPE_DISTRICT));
            itemList.add(new StandardItem(context.getString(R.string.sex), info != null ? (info.sex == 0 ? context.getString(R.string.men) : context.getString(R.string.woman)) : context.getString(R.string.men), TYPE_SEX));

        }
    };

    @Override
    protected void destroy() {
        itemList.clear();
        unregisterAdapterDataObserver(observer);
    }


    public void setValue(String value, int position) {
        StandardItem item = (StandardItem) itemList.get(position);
        item.content = value;
        notifyItemChanged(position);
    }

    public void setImage(String url, int position) {
        HeadItem item = (HeadItem) itemList.get(position);
        item.headUrl = url;
        notifyItemChanged(position);
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    @Override
    public EditHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditHolder holder = null;
        switch (viewType) {
            case VIEW_HEADER:
                holder = new HeaderHolder(context, parent);
                break;
            case VIEW_STANDARD:
                holder = new StandardHolder(context, parent);
                break;
            case VIEW_EMPTY:
                holder = new EmptyHolder(context, parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(EditHolder holder, int position) {
        holder.bindHolder(context, itemList.get(position), position, clickListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class HeaderHolder extends EditHolder<HeadItem> {

        @BindView(R.id.head_image)
        CircleImageView headImage;

        @BindView(R.id.back)
        TextView back;

        public HeaderHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_edit_photo);
        }

        @Override
        void bindHolder(Context context, final HeadItem item, final int position, final OnItemClickListener clickListener) {
            final int placeholderResId = R.drawable.icon_head_address;
            //头像设置
            Glide.with(context)
                    .load(item.headUrl)
                    .apply(new RequestOptions().placeholder(placeholderResId).centerCrop().fallback(placeholderResId))
                    .into(headImage);
            headImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(TYPE_HEAD_IMAGE, item.headUrl, position);
                    }
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(TYPE_BACK, null, position);
                    }
                }
            });
        }

    }

    static class StandardHolder extends EditHolder<StandardItem> {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        public StandardHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.item_edit_standard);
        }

        @Override
        void bindHolder(Context context, final StandardItem item, final int position, final OnItemClickListener clickListener) {
            content.setText(item.content);
            title.setText(item.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(item.type, item.content, position);
                    }
                }
            });
        }
    }

    static class EmptyHolder extends EditHolder<EmptyItem> {
        public EmptyHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_empty);
        }

        @Override
        void bindHolder(Context context, EmptyItem item, int position, OnItemClickListener clickListener) {

        }
    }


    static class HeadItem implements Item {
        private String headUrl;

        public HeadItem(String headUrl) {
            this.headUrl = headUrl;
        }

        @Override
        public int getViewType() {
            return VIEW_HEAD;
        }
    }

    static class StandardItem implements Item {
        private String title;
        private int type;
        private String content;

        public StandardItem(String title, String content, int type) {
            this.content = content;
            this.title = title;
            this.type = type;
        }

        @Override
        public int getViewType() {
            return VIEW_STANDARD;
        }
    }

    static class EmptyItem implements Item {
        @Override
        public int getViewType() {
            return VIEW_EMPTY;
        }
    }

    interface Item {
        int VIEW_HEAD = 0;
        int VIEW_STANDARD = 1;
        int VIEW_EMPTY = 2;

        @IntDef({
                VIEW_HEAD, VIEW_STANDARD, VIEW_EMPTY
        })

        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getViewType();
    }

    static abstract class EditHolder<II extends Item> extends BaseHolder {
        public EditHolder(Context context, ViewGroup parent, int adapterLayoutResId) {
            super(context, parent, adapterLayoutResId);
        }

        abstract void bindHolder(Context context, II item, int position, OnItemClickListener clickListener);
    }

    interface OnItemClickListener {
        void onItemClick(int type, String content, int position);
    }


}
