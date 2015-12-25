package com.emreeran.pickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Horizontal or vertical scrollable view extended from RecyclerView with equal size items,
 * Item heights or widths are calculated according to mItemsPerScreen
 * value for horizontal and vertical orientation respectively
 * An indicator view can be assigned to move along with selected items
 * Needs an adapter to be assigned to get view items. Extend your adapter with PickerView.Adapter class.
 * Implement onCreateView to inflate items, onBindView to manipulate views and on onSelectPosition to manage onClick events.
 * <p/>
 * Created by Emre Eran on 21/12/15.
 */
public class PickerView extends RecyclerView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation;
    private int mParentWidth, mParentHeight;
    private int mItemsPerScreen;

    private int mItemSize;
    private int mDividerSize;

    private View mIndicator;
    private int mCurrentPosition = 0;
    private int mIndicatorOffset;
    private boolean mIndicatorBounceToggle;
    private boolean mScrollOnClick = true;

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
            mDividerSize = attributes.getDimensionPixelSize(R.styleable.PickerView_divider_size, 0);
            mItemsPerScreen = attributes.getInteger(R.styleable.PickerView_item_per_screen, -1);
            final int dividerColor = attributes.getColor(R.styleable.PickerView_divider_color, Color.BLACK);
            LinearLayoutManager layoutManager;

            switch (mOrientation) {
                case HORIZONTAL:
                    setHorizontalScrollBarEnabled(false);
                    layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (mDividerSize > 0) {
                                int height = getMeasuredHeight();
                                addItemDecoration(new DividerItemDecoration(dividerColor, mDividerSize, height, mOrientation));
                            }
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    break;
                case VERTICAL:
                    setVerticalScrollBarEnabled(false);
                    layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (mDividerSize > 0) {
                                int width = getMeasuredWidth();
                                addItemDecoration(new DividerItemDecoration(dividerColor, width, mDividerSize, mOrientation));
                            }
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    break;
                default:
                    throw new RuntimeException("Orientation should be set to horizontal or vertical");
            }

            setLayoutManager(layoutManager);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentWidth = w;
        mParentHeight = h;

        if (mItemsPerScreen > 0) {
            if (mOrientation == HORIZONTAL) {
                // Total of items per screen - 1 dividers are shown on screen
                int offsetTotal = mDividerSize * (mItemsPerScreen - 1);
                mItemSize = (mParentWidth - offsetTotal) / mItemsPerScreen;
            } else {
                // Total of items per screen - 1 dividers are shown on screen
                int offsetTotal = mDividerSize * (mItemsPerScreen - 1);
                mItemSize = (mParentHeight - offsetTotal) / mItemsPerScreen;
            }

            initializeIndicatorPosition();
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
        scrollToPosition(position, null);
    }

    public void scrollToPosition(int position, @Nullable OnScrolledToViewListener listener) {
        if (mCurrentPosition != position) {
            Adapter.SimpleHolder holder = (Adapter.SimpleHolder) findViewHolderForAdapterPosition(position);
            // TODO problem here
            if (holder != null) {
                scrollToView(holder.mRootView);
                scrollIndicatorToPosition(position);
                mCurrentPosition = position;
                if (listener != null) {
                    listener.onScrolled(holder.mRootView);
                }
                ((Adapter)getAdapter()).setLastItem(holder.mRootView);
            }
        }
    }

    public void setIndicator(View view) {
        setIndicator(view, false);
    }

    public void setIndicator(View view, boolean bounceAnimation) {
        mIndicator = view;
        mIndicatorBounceToggle = bounceAnimation;
        mIndicatorOffset = 0;

        if (mOrientation == HORIZONTAL) {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                    params.leftMargin -= dx;
                    mIndicator.setLayoutParams(params);
                    mIndicatorOffset -= dx;
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
                    mIndicatorOffset -= dy;
                }
            });
        }
    }

    public void scrollToView(View view) {
        int dx, dy;
        if (mOrientation == HORIZONTAL) {
            dx = view.getLeft() - (mParentWidth / 2) + (view.getWidth() / 2);
            dy = 0;
        } else {
            dx = 0;
            dy = view.getTop() - (mParentHeight / 2) + (view.getHeight() / 2);
        }

        smoothScrollBy(dx, dy);
    }

    /**
     * If an indicator view is set, moves it to the alignment of the picker item in the given position.
     * To find the distance to scroll, the position of destination view on the screen, the location of the view of origin on the
     * screen {@link PickerView.Adapter#mLastLocation}, the offset created by scrolling by user {@link PickerView#mIndicatorOffset} is
     * taken into account.
     *
     * @param position View position the indicator will be scrolled to
     */
    public void scrollIndicatorToPosition(int position) {
        if (mIndicator != null) {
            Adapter.SimpleHolder toHolder = (Adapter.SimpleHolder) findViewHolderForAdapterPosition(position);
            View to = toHolder.mRootView;
            IndicatorAnimationHelper helper = new IndicatorAnimationHelper();

            if (mOrientation == HORIZONTAL) {
                Adapter adapter = ((Adapter) getAdapter());
                int deltaX = to.getLeft() - adapter.mLastLocation - mIndicatorOffset;

                // If width of the view moved from and moved to differ, add half of the difference
                deltaX += (to.getWidth() - adapter.mLastSize) / 2;


                if (mIndicatorBounceToggle) {
                    helper.animateHorizontallyWithBounce(mIndicator, deltaX);
                } else {
                    helper.animateHorizontally(mIndicator, deltaX);
                }
            } else {
                Adapter adapter = ((Adapter) getAdapter());
                int deltaY = to.getTop() - adapter.mLastLocation - mIndicatorOffset;

                // If height of the view moved from and moved to differ, add half of the difference
                deltaY += (to.getHeight() - adapter.mLastSize) / 2;

                if (mIndicatorBounceToggle) {
                    helper.animateVerticallyWithBounce(mIndicator, deltaY);
                } else {
                    helper.animateVertically(mIndicator, deltaY);
                }
            }
            mIndicatorOffset = 0;
        }
    }

    public void initializeIndicatorPosition() {
        if (mIndicator != null) {
            if (mOrientation == HORIZONTAL) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                params.leftMargin = ((mItemSize + mDividerSize) / 2) - (params.width / 2);
                mIndicator.setLayoutParams(params);

            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                params.topMargin = ((mItemSize + mDividerSize) / 2) - (params.height / 2);
                mIndicator.setLayoutParams(params);

            }
        }
    }

    public static abstract class Adapter extends RecyclerView.Adapter<Adapter.SimpleHolder> implements PickerAdapter {

        private PickerView mPickerView;
        private boolean isSizeSet = false;
        private int mLastLocation = 0;
        private int mLastSize;

        private int mSelectedView = 0;

        @Override
        public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = onCreateView(parent);
            final View rootView = view.getRootView();
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();

            if (mPickerView.mItemsPerScreen > 0) {
                if (mPickerView.mOrientation == PickerView.HORIZONTAL) {
                    layoutParams.width = mPickerView.mItemSize;
                } else {
                    layoutParams.height = mPickerView.mItemSize;
                }

                mLastSize = mPickerView.mItemSize;

            } else if (!isSizeSet) {
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mPickerView.mOrientation == HORIZONTAL) {
                            mPickerView.mItemSize = rootView.getWidth();
                            mLastSize = rootView.getWidth();
                        } else {
                            mPickerView.mItemSize = rootView.getHeight();
                            mLastSize = rootView.getHeight();
                        }
                        mPickerView.initializeIndicatorPosition();
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                isSizeSet = true;
            }

            rootView.setLayoutParams(layoutParams);
            return new SimpleHolder(rootView);
        }

        @Override
        public void onBindViewHolder(SimpleHolder holder, final int position) {
            View view = holder.mRootView;
            view.setId(position);
            onBindView(view, position);

            if (mSelectedView == position) {
                onSelectView(view, position);
            }

            Log.d("picker", "bind: " + position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPickerView.mScrollOnClick) {
                        mPickerView.scrollToView(v);
                    }

                    mPickerView.scrollIndicatorToPosition(position);
                    mPickerView.mCurrentPosition = position;

                    setLastItem(v);
                    onSelectPosition(position);
                    mSelectedView = position;
                    notifyItemChanged(position);
                }
            });
        }

        public void setLastItem(View view) {
            if (mPickerView.mOrientation == HORIZONTAL) {
                mLastLocation = view.getLeft();
                mLastSize = view.getWidth();
            } else {
                mLastLocation = view.getTop();
                mLastSize = view.getHeight();
            }
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

    public interface OnScrolledToViewListener {
        void onScrolled(View view);
    }
}
