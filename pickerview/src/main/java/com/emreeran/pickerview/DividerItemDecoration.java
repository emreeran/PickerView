package com.emreeran.pickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    private int mOrientation;

    private int mWidth, mHeight;

    private boolean isFromResource = false;

    public DividerItemDecoration(int color, int width, int height, int orientation) {
        mDivider = new ColorDrawable(color);
        mWidth = width;
        mHeight = height;
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, int resId) {
        mDivider = context.getResources().getDrawable(resId);
        setOrientation(orientation);
        isFromResource = true;
    }

    public void setOrientation(int orientation) {
        if (orientation != PickerView.HORIZONTAL && orientation != PickerView.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == PickerView.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));

            int bottom;
            if (isFromResource) {
                bottom = top + mDivider.getIntrinsicHeight();
            } else {
                bottom = top + mHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int dividerCount = parent.getChildCount() - 1;
        for (int i = 0; i < dividerCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));

            int right;
            if (isFromResource) {
                right = left + mDivider.getIntrinsicHeight();
            } else {
                right = left + mWidth;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();
        if (itemCount > 0 && position == itemCount - 1) {
            super.getItemOffsets(outRect, view, parent, state);
        } else {
            if (mOrientation == PickerView.VERTICAL) {
                if (isFromResource) {
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, 0, mHeight);
                }
            } else {
                if (isFromResource) {
                    outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                } else {
                    outRect.set(0, 0, mWidth, 0);
                }
            }
        }
    }
}
