package com.example.com.wisdomcommunity.view.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 向后参照
 */
public class FlexibleItemDecoration extends RecyclerView.ItemDecoration {

    private static final FlexibleDecor EMPTY_DECOR = new EmptyDecor();

    private final FlexibleDecor defaultDecor;
    private final Map<String, FlexibleDecor> typeDecorMap;
    private final boolean footerReferSelf;

    private FlexibleItemDecoration(Context context, FlexibleDecor defaultDecor, Map<String, FlexibleDecor> typeDecorMap, boolean footerReferSelf) {
        super();
        this.defaultDecor = defaultDecor != null ? defaultDecor : EMPTY_DECOR;
        this.typeDecorMap = typeDecorMap != null ? typeDecorMap : Collections.<String, FlexibleDecor>emptyMap();
        this.footerReferSelf = footerReferSelf;
    }

    private FlexibleDecor getFlexibleDecor(int viewType, int nextViewType) {
        FlexibleDecor flexibleDecor = typeDecorMap.get(Utils.typeBetweenKey(viewType, nextViewType));
        if (flexibleDecor == null) {
            flexibleDecor = typeDecorMap.get(Utils.typeToAllKey(viewType));
        }
        if (flexibleDecor == null) {
            flexibleDecor = typeDecorMap.get(Utils.allToTypeKey(nextViewType));
        }
        if (flexibleDecor == null) {
            if (viewType == nextViewType) {
                flexibleDecor = typeDecorMap.get(Utils.KEY_TYPE_SAME);
            } else {
                flexibleDecor = typeDecorMap.get(Utils.KEY_TYPE_XOR);
            }
        }
        return flexibleDecor;
    }

    private FlexibleDecor getFlexibleDecor(RecyclerView parent, View view) {
        FlexibleDecor flexibleDecor = null;
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && adapter.getItemCount() > 0) {
            final int adapterPosition = parent.getChildAdapterPosition(view);
            if (adapterPosition == adapter.getItemCount() - 1) {
                flexibleDecor = typeDecorMap.get(Utils.KEY_FOOTER);
                if (flexibleDecor == null) {
                    // 最后一个，没有参照物
                    if (footerReferSelf) {
                        final int viewType = adapter.getItemViewType(adapterPosition);
                        flexibleDecor = getFlexibleDecor(viewType, viewType);
                    } else {
                        flexibleDecor = defaultDecor;
                    }
                }
            } else {
                final int viewType = adapter.getItemViewType(adapterPosition);
                final int nextViewType = adapter.getItemViewType(adapterPosition + 1);
                flexibleDecor = getFlexibleDecor(viewType, nextViewType);
            }

            if (flexibleDecor == null) {
                flexibleDecor = defaultDecor;
            }
        } else {
            flexibleDecor = EMPTY_DECOR;
        }
        return flexibleDecor;
    }

    // 第一行
    protected final boolean isHeaderRank(RecyclerView parent, View view) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        //final int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                // group index =＝ 0
                return lookup.getSpanGroupIndex(adapterPosition, spanCount) == 0;
            } else {
                // span index == 0
                // group index != previous position group index
                return lookup.getSpanIndex(adapterPosition, spanCount) == 0;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return adapterPosition == 0;
            } else {
                return true;
            }
        }
        return false;
    }

    // 最后一行
    protected final boolean isFooterRank(RecyclerView parent, View view) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                // group index == last group index
                return lookup.getSpanGroupIndex(adapterPosition, spanCount)
                        == lookup.getSpanGroupIndex(itemCount - 1, spanCount);
            } else {
                // next position span index == 0
                // group index != next position group index
                if (adapterPosition == itemCount - 1) {
                    int size = 0;
                    for (int i = adapterPosition; i >= 0 &&
                            lookup.getSpanGroupIndex(adapterPosition, spanCount)
                                    == lookup.getSpanGroupIndex(i, spanCount); i --) {
                        size += lookup.getSpanSize(i);
                    }
                    return size == spanCount;
                } else {
                    return lookup.getSpanIndex(adapterPosition + 1, spanCount) == 0;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return adapterPosition == itemCount - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    // 第一列
    protected final boolean isHeaderRow(RecyclerView parent, View view) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        //final int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                // span index == 0
                // group index != previous position group index
                return lookup.getSpanIndex(adapterPosition, spanCount) == 0;
            } else {
                // group index == 0
                return lookup.getSpanGroupIndex(adapterPosition, spanCount) == 0;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return adapterPosition == 0;
            } else {
                return true;
            }
        }
        return false;
    }

    // 最后一列
    protected final boolean isFooterRow(RecyclerView parent, View view) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                // next position span index == 0
                // group index != next position group index
                if (adapterPosition == itemCount - 1) {
                    int size = 0;
                    for (int i = adapterPosition; i >= 0 &&
                            lookup.getSpanGroupIndex(adapterPosition, spanCount)
                                    == lookup.getSpanGroupIndex(i, spanCount); i --) {
                        size += lookup.getSpanSize(i);
                    }
                    return size == spanCount;
                } else {
                    return lookup.getSpanIndex(adapterPosition + 1, spanCount) == 0;
                }
            } else {
                // group index == last group index
                return lookup.getSpanGroupIndex(adapterPosition, spanCount)
                        == lookup.getSpanGroupIndex(itemCount - 1, spanCount);
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return adapterPosition == itemCount - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    // 类似 View 的 LayoutParams 的 margin
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        FlexibleDecor flexibleDecor = getFlexibleDecor(parent, view);
        FlexibleEdge flexibleEdge = new FlexibleEdge(
                isHeaderRank(parent, view),
                isHeaderRow(parent, view),
                isFooterRank(parent, view),
                isFooterRow(parent, view));
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            final int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            switch (orientation) {
                case LinearLayoutManager.HORIZONTAL: {
                    flexibleDecor.decorRect(outRect, parent, view, flexibleEdge, LinearLayoutManager.HORIZONTAL);
                    break;
                }
                case LinearLayoutManager.VERTICAL: {
                    flexibleDecor.decorRect(outRect, parent, view, flexibleEdge, LinearLayoutManager.VERTICAL);
                    break;
                }
            }
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = parent.getChildAt(i);

            FlexibleDecor flexibleDecor = getFlexibleDecor(parent, child);
            FlexibleEdge flexibleEdge = new FlexibleEdge(
                    isHeaderRank(parent, child),
                    isHeaderRow(parent, child),
                    isFooterRank(parent, child),
                    isFooterRow(parent, child));

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                switch (orientation) {
                    case LinearLayoutManager.HORIZONTAL: {
                        flexibleDecor.decorDraw(c, parent, child, flexibleEdge, LinearLayoutManager.HORIZONTAL);
                        break;
                    }
                    case LinearLayoutManager.VERTICAL: {
                        flexibleDecor.decorDraw(c, parent, child, flexibleEdge, LinearLayoutManager.VERTICAL);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = parent.getChildAt(i);

            FlexibleDecor flexibleDecor = getFlexibleDecor(parent, child);
            FlexibleEdge flexibleEdge = new FlexibleEdge(
                    isHeaderRank(parent, child),
                    isHeaderRow(parent, child),
                    isFooterRank(parent, child),
                    isFooterRow(parent, child));

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                switch (orientation) {
                    case LinearLayoutManager.HORIZONTAL: {
                        flexibleDecor.decorDrawOver(c, parent, child, flexibleEdge, LinearLayoutManager.HORIZONTAL);
                        break;
                    }
                    case LinearLayoutManager.VERTICAL: {
                        flexibleDecor.decorDrawOver(c, parent, child, flexibleEdge, LinearLayoutManager.VERTICAL);
                        break;
                    }
                }
            }
        }
    }

    public static class Builder {

        private Context context;

        private FlexibleDecor defaultDecor;
        private Map<String, FlexibleDecor> typeDecorMap = new HashMap<String, FlexibleDecor>();

        private boolean footerReferSelf;

        public Builder(Context context) {
            super();
            this.context = context;
        }

        /**
         * 默认分割线
         */
        public Builder defaultDecor(FlexibleDecor defaultDecor) {
            this.defaultDecor = defaultDecor;
            return this;
        }

        /**
         * 默认两种不同视图之间的分割线
         */
        public Builder typeXOR(FlexibleDecor decor) {
            typeDecorMap.put(Utils.KEY_TYPE_XOR, decor);
            return this;
        }

        /**
         * 默认两种相同视图之间的分割线
         */
        public Builder typeSame(FlexibleDecor decor) {
            typeDecorMap.put(Utils.KEY_TYPE_SAME, decor);
            return this;
        }

        /**
         * 同种 ViewType 视图之间的分割线
         */
        public Builder typeSame(int viewType, FlexibleDecor decor) {
            return typeBetween(viewType, viewType, decor);
        }

        /**
         * 两种 ViewType 之间的分割线
         */
        public Builder typeBetween(int viewType, int nextViewType, FlexibleDecor decor) {
            typeDecorMap.put(Utils.typeBetweenKey(viewType, nextViewType), decor);
            return this;
        }

        /**
         * ViewType 视图与其他视图之间的分割线
         */
        public Builder typeSelf(int viewType, FlexibleDecor decor) {
            return typeToAll(viewType, decor).allToType(viewType, decor);
        }

        public Builder typeToAll(int viewType, FlexibleDecor decor) {
            typeDecorMap.put(Utils.typeToAllKey(viewType), decor);
            return this;
        }

        public Builder allToType(int nextViewType, FlexibleDecor decor) {
            typeDecorMap.put(Utils.allToTypeKey(nextViewType), decor);
            return this;
        }

        public Builder footer(FlexibleDecor decor) {
            typeDecorMap.put(Utils.KEY_FOOTER, decor);
            return this;
        }

        public Builder footerReferSelf(boolean referSelf) {
            footerReferSelf = referSelf;
            return this;
        }

        public FlexibleItemDecoration build() {
            return new FlexibleItemDecoration(context, defaultDecor, typeDecorMap, footerReferSelf);
        }
    }

    static class Utils {
        public static final String KEY_TYPE_SAME = "type_same";
        public static final String KEY_TYPE_XOR = "type_xor";
        public static final String FORMAT_TYPE_BETWEEN_KEY = "type_between_%1$d_and_%2$d";
        public static final String FORMAT_TYPE_TO_ALL_KEY = "type_%1$d_to_all";
        public static final String FORMAT_ALL_TO_TYPE_KEY = "all_to_type_%1$d";
        public static final String KEY_FOOTER = "footer";

        public static String typeBetweenKey(int viewType, int nextViewType) {
            return String.format(Locale.getDefault(), FORMAT_TYPE_BETWEEN_KEY, viewType, nextViewType);
        }

        public static String typeToAllKey(int viewType) {
            return String.format(Locale.getDefault(), FORMAT_TYPE_TO_ALL_KEY, viewType);
        }

        public static String allToTypeKey(int nextViewType) {
            return String.format(Locale.getDefault(), FORMAT_ALL_TO_TYPE_KEY, nextViewType);
        }
    }
}
