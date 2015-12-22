package com.emreeran.ehorizontalpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Horizontal or vertical scrollable view extended from RecyclerView with equal size items,
 * Item heights or widths are calculated according to mItemsPerScreen
 * value for horizontal and vertical orientation respectively
 * An indicator view can be assigned to move along with selected items.
 * <p>
 * Needs an adapter to be assigned to get view items. Extend your adapter with PickerView.Adapter class.
 * Implement onCreateView to inflate items, onBindView to manipulate views and on onViewClicked to manage onClick events.
 * <p>
 * Created by Emre Eran on 21/12/15.
 */
public class PickerView extends RecyclerView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation;
    private int mParentWidth, mParentHeight;
    private int mItemsPerScreen;
    private int mItemWidth, mItemHeight;
    private int mDividerWidth, mDividerHeight;
    private View mIndicator;
    private int mCurrentPosition = 0;
    private int mLastIndicatorPosition;
    private boolean mIndicatorBounceToggle;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PickerView, 0, 0);

        try {
            mOrientation = attributes.getInt(R.styleable.PickerView_orientation, -1);

            switch (mOrientation) {
                case HORIZONTAL:
                    setHorizontalScrollBarEnabled(false);
                    LinearLayoutManager horizonralLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    setLayoutManager(horizonralLayoutManager);
                    break;
                case VERTICAL:
                    setVerticalScrollBarEnabled(false);
                    LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    setLayoutManager(verticalLayoutManager);
                    break;
                default:
                    throw new RuntimeException("Orientation should be set to horizontal or vertical");
            }

            mItemsPerScreen = attributes.getInteger(R.styleable.PickerView_item_per_screen, 4);
            mDividerWidth = attributes.getDimensionPixelSize(R.styleable.PickerView_divider_width, 0);
            mDividerHeight = attributes.getDimensionPixelSize(R.styleable.PickerView_divider_height, 0);
            final int dividerColor = attributes.getColor(R.styleable.PickerView_divider_color, Color.BLACK);
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mDividerWidth > 0) {
                        int height = getMeasuredHeight();
                        addItemDecoration(new DividerItemDecoration(dividerColor, mDividerWidth, height, mOrientation));
                    } else if (mDividerHeight > 0) {
                        int width = getMeasuredWidth();
                        addItemDecoration(new DividerItemDecoration(dividerColor, width, mDividerHeight, mOrientation));
                    }
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentWidth = w;
        mParentHeight = h;

        if (mOrientation == HORIZONTAL) {
            // Total of items per screen - 1 dividers are shown on screen
            int offsetTotal = mDividerWidth * (mItemsPerScreen - 1);
            mItemWidth = (mParentWidth - offsetTotal) / mItemsPerScreen;

            if (mIndicator != null) {
                mLastIndicatorPosition = 0;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                params.leftMargin = ((mItemWidth + mDividerWidth) / 2) - (params.width / 2);
                mIndicator.setLayoutParams(params);
            }
        } else {
            // Total of items per screen - 1 dividers are shown on screen
            int offsetTotal = mDividerHeight * (mItemsPerScreen - 1);
            mItemHeight = (mParentHeight - offsetTotal) / mItemsPerScreen;

            if (mIndicator != null) {
                mLastIndicatorPosition = 0;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                params.topMargin = ((mItemHeight + mDividerHeight) / 2) - (params.height / 2);
                mIndicator.setLayoutParams(params);
            }
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof Adapter) {
            Adapter ePickerAdapter = (Adapter) adapter;
            ePickerAdapter.setPickerView(this);
            super.setAdapter(adapter);
        }
    }

    @Override
    public ViewHolder findViewHolderForAdapterPosition(int position) {
        return super.findViewHolderForAdapterPosition(position);
    }

    @Override
    public ViewHolder findViewHolderForLayoutPosition(int position) {
        return super.findViewHolderForLayoutPosition(position);
    }

    public void scrollToPosition(int position) {
        if (mCurrentPosition != position) {
            Adapter.SimpleHolder holder = (Adapter.SimpleHolder) findViewHolderForAdapterPosition(position);
            if (holder != null) {
                scrollToView(holder.mRootView, position);
            }
        }
    }

    public void setIndicator(View view) {
        setIndicator(view, false);
    }

    public void setIndicator(View view, boolean bounceAnimation) {
        mIndicator = view;
        mLastIndicatorPosition = 0;
        mIndicatorBounceToggle = bounceAnimation;

        if (mOrientation == HORIZONTAL) {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                    params.leftMargin -= dx;
                    mIndicator.setLayoutParams(params);
                }
            });
        } else {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                    params.topMargin -= dy;
                    mIndicator.setLayoutParams(params);
                }
            });
        }
    }

    public void scrollToView(View view, int position) {
        mCurrentPosition = position;
        int dx, dy;
        if (mOrientation == HORIZONTAL) {
            dx = view.getLeft() - (mParentWidth / 2) + (mItemWidth / 2);
            dy = 0;
        } else {
            dx = 0;
            dy = view.getTop() - (mParentHeight / 2) + (mItemHeight / 2);
        }

        smoothScrollBy(dx, dy);

        if (mIndicator != null) {
            int positionChange = position - mLastIndicatorPosition;
            mLastIndicatorPosition = position;

            if (mOrientation == HORIZONTAL) {
                int deltaX = positionChange * (mItemWidth + mDividerWidth);
                IndicatorAnimationHelper helper = new IndicatorAnimationHelper();

                if (mIndicatorBounceToggle) {
                    helper.animateHorizontallyWithBounce(mIndicator, deltaX);
                } else {
                    helper.animateHorizontally(mIndicator, deltaX);
                }
            } else {
                int deltaY = positionChange * (mItemHeight + mDividerHeight);
                IndicatorAnimationHelper helper = new IndicatorAnimationHelper();

                if (mIndicatorBounceToggle) {
                    helper.animateVerticallyWithBounce(mIndicator, deltaY);
                } else {
                    helper.animateVertically(mIndicator, deltaY);
                }
            }
        }
    }

    public static abstract class Adapter extends RecyclerView.Adapter<Adapter.SimpleHolder> implements PickerAdapter {

        private PickerView mPickerView;

        @Override
        public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = onCreateView(parent);
            View rootView = view.getRootView();
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            if (mPickerView.mOrientation == PickerView.HORIZONTAL) {
                layoutParams.width = mPickerView.mItemWidth;
            } else {
                layoutParams.height = mPickerView.mItemHeight;
            }

            rootView.setLayoutParams(layoutParams);
            return new SimpleHolder(rootView);
        }

        @Override
        public void onBindViewHolder(SimpleHolder holder, final int position) {
            View view = holder.mRootView;
            view.setId(position);
            onBindView(view, position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPickerView.scrollToView(v, position);
                    onViewClicked(v, position);
                }
            });
        }

        public void setPickerView(PickerView pickerView) {
            mPickerView = pickerView;
        }

        public class SimpleHolder extends RecyclerView.ViewHolder {
            View mRootView;

            public SimpleHolder(View itemView) {
                super(itemView);
                mRootView = itemView;
            }
        }
    }
}
