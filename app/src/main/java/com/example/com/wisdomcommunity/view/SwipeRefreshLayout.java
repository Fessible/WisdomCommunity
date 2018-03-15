package com.example.com.wisdomcommunity.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("deprecation")
public class SwipeRefreshLayout extends FrameLayout {

    private static final int MULTIPLE = 3;

    // states
    public static final int STATE_RESET = 0;// 初始
    public static final int STATE_PULL = 1;// 拉动
    public static final int STATE_RELEASE = 2;// 释放
    public static final int STATE_REFRESH = 3;// 刷新

    @IntDef({
            STATE_RESET,
            STATE_PULL,
            STATE_RELEASE,
            STATE_REFRESH
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    // invalid
    private static final int INVALID_POINTER = -1;

    // auxiliary
    private int touchSlop;
    private Rect rect;
    private View capturedView;
    private ScrollerCompat scroller;
    private int activePointerId;
    private float initialMotionY;
    private int initialScrollY;

    // Strip
    private RefreshFilter refreshFilter;

    private RefreshWizard refreshWizard;

    @State
    private int refreshState;

    private boolean refreshFlag;
    private OnRefreshListener onRefreshListener;
    private OnScrollListener onScrollListener;

    public SwipeRefreshLayout(Context context) {
        super(context);
        setup();
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup() {
        // 启动 onDraw()方法，否则onDraw()方法不可用
//		super.setWillNotDraw(false);

        // 捕获 View.setOnKeyListener
        super.setFocusable(true);
        super.setFocusableInTouchMode(true);

        // 初始化状态
        refreshState = STATE_RESET;

        refreshFlag = false;

        scroller = ScrollerCompat.create(getContext(), new LinearInterpolator());
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        rect = new Rect();

        setRefreshFilter(new UniversalRefreshFilter());
    }

    /**
     * 初始化完毕
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkLegal();
    }

    /**
     * 检测是否合法
     */
    private void checkLegal() {
//		int count = getChildCount();
//		if (count > 1) {
//			throw new IllegalArgumentException("SwipeRefreshLayout Usage Error.");
//		}
    }

    public void setRefreshFilter(RefreshFilter filter) {
        refreshFilter = filter;
    }

    public interface RefreshFilter {
        /** 底层 View 是否支持下拉刷新功能 */
        public boolean captureChild(View child);

        /** 底层 View 是否滑动到最顶部 */
        public boolean canChildScrollUp(ViewGroup parent, View child);
    }

    public void setRefreshWizard(RefreshWizard wizard) {
        refreshWizard = wizard;
    }

    public static abstract class RefreshWizard {

        public RefreshWizard(Context context, SwipeRefreshLayout parent) {
            super();
        }

        public abstract int getRefreshHeight();

        public abstract void onStateChanged(@State int state);
        public abstract void onScrollChanged(int scrollY);

        /**
         * 线性布局用的
         *
         * 预先衡量视图
         */
        protected final void measureView(View child) {
            ViewGroup.LayoutParams params = child.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
            int paramsHeight = params.height;
            int childHeightSpec;
            if (paramsHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(paramsHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            child.measure(childWidthSpec, childHeightSpec);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        onRefreshListener = listener;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public interface OnScrollListener {
        public void onScrollChanged(int scrollY);
    }

    /**
     * 外部调用
     */
    public void tryRefresh(boolean notify) {
        if (!isAllowDown()) {
            return;
        }
        refreshFlag = notify;
        refreshState = STATE_REFRESH;
        onStateChanged(refreshState);
        animateOffsetToRefresh();
    }

    /**
     * 外部调用
     */
    public void tryRefreshFinished() {
        if (!isAllowDown()) {
            return;
        }
        refreshFlag = false;
        refreshState = STATE_RESET;
        onStateChanged(refreshState);
        animateOffsetToOrigin();
    }

    /**
     * 是否允许下拉
     */
    public boolean isAllowDown() {
        return isEnabled() && onRefreshListener != null;
    }

    /**
     * 是否处于刷新中
     */
    public boolean isRefreshing() {
        return refreshState == STATE_REFRESH;
    }

    /**
     * 是否处于原始状态
     */
    private boolean isReset() {
        return refreshState == STATE_RESET;
    }

    /**
     * 是否处于释放即可刷新状态
     */
    private boolean isRelease() {
        return refreshState == STATE_RELEASE;
    }

    private int getRefreshHeight() {
        return refreshWizard != null ? refreshWizard.getRefreshHeight() : 0;
    }

    private void onStateChanged(@State int state) {
        if (refreshWizard != null) {
            refreshWizard.onStateChanged(state);
        }
    }

    /**
     * 获取当前Touch点下，支持下拉刷新的视图
     */
    private View captureView(ViewGroup parent, MotionEvent event) {
        View capturedView = null;
        if (parent != null && event != null) {
            float xf = event.getX();
            float yf = event.getY();

            float scrollXFloat = xf + parent.getScrollX();
            float scrollYFloat = yf + parent.getScrollY();
            Rect frame = rect;

            int count = parent.getChildCount();
            for (int i = count - 1; i >= 0; --i) {
                View child = parent.getChildAt(i);
                // 若子view可见或者有动画在执行
                if (child != null && (child.getVisibility() == View.VISIBLE || child.getAnimation() != null)) {
                    // 获取子view的布局坐标区域
                    child.getHitRect(frame);

                    float xc = scrollXFloat - child.getLeft();
                    float yc = scrollYFloat - child.getTop();

                    int xcInt = Math.round(xc);
                    int ycInt = Math.round(yc);

                    // 若子view 区域包含当前touch点击区域
                    if (frame.contains(xcInt, ycInt)) {
                        if (refreshFilter != null && refreshFilter.captureChild(child)) {
                            capturedView = child;
                            break;
                        }
                    }
                }
            }
        }
        return capturedView;
    }

    private boolean canChildScrollUp(View view) {
        return refreshFilter != null && refreshFilter.canChildScrollUp(this, view);
    }

    /**
     * 拦截Touch事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isAllowDown()) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                // 数据置初始状态
                activePointerId = INVALID_POINTER;
                initialMotionY = 0;
                initialScrollY = 0;
                capturedView = null;

                // 首先拦截down事件，并记录
                activePointerId = MotionEventCompat.getPointerId(ev, 0);
                initialMotionY = MotionEventCompat.getY(ev, 0);
                initialScrollY = getScrollY();
                capturedView = captureView(this, ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 拦截 MotionEvent
                if (activePointerId != INVALID_POINTER) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                    if (pointerIndex >= 0) {
                        float deltaY = MotionEventCompat.getY(ev, pointerIndex) - initialMotionY;
                        if (Math.abs(deltaY) > touchSlop && deltaY > 0 && !canChildScrollUp(capturedView)) {
                            return true;
                        }
                        if (isRefreshing() && deltaY < 0 && getScrollY() < 0) {
                            return true;
                        }
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
            default: {
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 实现Touch事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isAllowDown()) {
            return super.onTouchEvent(ev);
        }
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (activePointerId != INVALID_POINTER) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                    if (pointerIndex >= 0) {
                        float deltaY = MotionEventCompat.getY(ev, pointerIndex) - initialMotionY;
                        // 只能本层处理，必须return
                        if (deltaY - initialScrollY >= 0) {// 向下拉动
                            if (!canChildScrollUp(capturedView)) {
                                deltaY = deltaY - initialScrollY;
                                /** 分段函数 **/
                                if (!isRefreshing()) {
                                    deltaY = deltaY / MULTIPLE;
                                } else {
                                    if (deltaY > getRefreshHeight()) {
                                        deltaY = getRefreshHeight() + (deltaY - getRefreshHeight()) * (getHeight() / MULTIPLE - getRefreshHeight()) / (getHeight() - getRefreshHeight());
                                    }
                                }
                                if (!isRefreshing()) {
                                    if (deltaY >= getRefreshHeight()) {
                                        if (!isRelease()) {
                                            refreshState = STATE_RELEASE;
                                            onStateChanged(refreshState);
                                        }
                                    } else if (deltaY < getRefreshHeight()) {
                                        if (isRelease()) {
                                            refreshState = STATE_PULL;
                                            onStateChanged(refreshState);
                                        } else if (isReset()) {
                                            refreshState = STATE_PULL;
                                        }
                                    }
                                }
                                int scrollY = -Math.round(deltaY);
                                scrollTo(0, scrollY);
                                return true;
                            }
                        }
                        if (isRefreshing() && deltaY < 0 && getScrollY() < 0) {
                            deltaY = getScrollY() - deltaY;
                            int scrollY = deltaY <= 0 ? Math.round(deltaY) : 0;
                            scrollTo(0, scrollY);
                            return true;
                        }
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                // 不能return true/false，否则导致 ListView 滚动非常卡
                if (!isRefreshing()) {
                    if (Math.abs(getScrollY()) >= getRefreshHeight()) {
                        refreshFlag = true;
                        refreshState = STATE_REFRESH;
                        onStateChanged(refreshState);
                        animateOffsetToRefresh();
                    } else {
                        refreshFlag = false;
                        refreshState = STATE_RESET;
                        animateOffsetToOrigin();
                    }
                } else {
                    if (Math.abs(getScrollY()) >= getRefreshHeight()) {
                        animateOffsetToRefresh();
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == activePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            activePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            initialMotionY = MotionEventCompat.getY(ev, newPointerIndex);
        }
    }

    /**
     * 刷新位置
     */
    private void animateOffsetToRefresh() {
        final int scrollToTop = getRefreshHeight() + getScrollY();

        final int startY = getScrollY();
        final int dy = -scrollToTop;
        final int duration = scrollToTop;
        swipeVDirection(startY, dy, duration);
    }

    /**
     * 初始位置
     */
    private void animateOffsetToOrigin() {
        final int scrollToTop = getScrollY();

        final int startY = getScrollY();
        final int dy = -scrollToTop;
        final int duration = scrollToTop;
        swipeVDirection(startY, dy, duration);
    }

    /**
     * 垂直方向滑动配置
     *
     * @param startY
     *            滑动起始的Y轴坐标
     * @param dy
     *            滑动的相对距离
     * @param duration
     *            滑动效果的时间
     */
    private void swipeVDirection(int startY, int dy, int duration) {
        scroller.abortAnimation();
        scroller.startScroll(0, startY, 0, dy, Math.abs(duration) * 2);
        postInvalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (refreshWizard != null) {
            refreshWizard.onScrollChanged(getScrollY());
        }
        if (onScrollListener != null) {
            onScrollListener.onScrollChanged(getScrollY());
        }
    }

    /**
     * 在mScroller.startScroll()方法被调用之后调用此方法
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!isAllowDown()) {
            return;
        }
        // 如果还在滚动就返回true，反之返回false
        if (scroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(scroller.getCurrX(), scroller.getCurrY());
                postInvalidate();
            } else {
                if (!scroller.isFinished()) {
                    postInvalidate();
                }
            }
            if (scroller.isFinished() && refreshFlag) {
                refreshFlag = false;
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } else {
            return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(@NonNull FrameLayout.LayoutParams source) {
            super(source);
        }
    }
}
