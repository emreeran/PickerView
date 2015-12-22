package com.emreeran.ehorizontalpicker;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Helper class to deal with indicator image animations
 * Created by Emre Eran on 22/12/15.
 */
public class IndicatorAnimationHelper {

    private int mPreviousTransformation = 0;
    private int mPreviousBounceTransformation = 0;

    public void animateHorizontally(final View view, final int deltaX) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int transformation = (int) (deltaX * interpolatedTime) - mPreviousTransformation;
                mPreviousTransformation += transformation;
                params.leftMargin = params.leftMargin + transformation;
                view.setLayoutParams(params);
            }
        };

        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public void animateHorizontallyWithBounce(final View view, final int deltaX) {
        if (deltaX != 0) {
            AnimationSet animationSet = new AnimationSet(true);

            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            int bounceMoveLength = 25;
            if (deltaX > 0) {
                bounceMoveLength = -25;
            }

            final int moveTransformation = deltaX - bounceMoveLength;
            Animation moveAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int transformation = (int) (moveTransformation * interpolatedTime) - mPreviousTransformation;
                    mPreviousTransformation += transformation;
                    params.leftMargin = params.leftMargin + transformation;
                    view.setLayoutParams(params);
                }
            };

            final int bounceTransformation = bounceMoveLength;
            Animation bounceAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int transformation = (int) (bounceTransformation * interpolatedTime) - mPreviousBounceTransformation;
                    mPreviousBounceTransformation += transformation;
                    params.leftMargin = params.leftMargin + transformation;
                    view.setLayoutParams(params);
                }
            };

            moveAnimation.setDuration(500);
            bounceAnimation.setDuration(250);
            bounceAnimation.setStartOffset(500);
            animationSet.addAnimation(moveAnimation);
            animationSet.addAnimation(bounceAnimation);
            view.startAnimation(animationSet);
        }
    }

    public void animateVertically(final View view, final int deltaY) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int transformation = (int) (deltaY * interpolatedTime) - mPreviousTransformation;
                mPreviousTransformation += transformation;
                params.topMargin = params.topMargin + transformation;
                view.setLayoutParams(params);
            }
        };

        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public void animateVerticallyWithBounce(final View view, final int deltaY) {
        if (deltaY != 0) {
            AnimationSet animationSet = new AnimationSet(true);

            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            int bounceMoveLength = 25;
            if (deltaY > 0) {
                bounceMoveLength = -25;
            }

            final int moveTransformation = deltaY - bounceMoveLength;
            Animation moveAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int transformation = (int) (moveTransformation * interpolatedTime) - mPreviousTransformation;
                    mPreviousTransformation += transformation;
                    params.topMargin = params.topMargin + transformation;
                    view.setLayoutParams(params);
                }
            };

            final int bounceTransformation = bounceMoveLength;
            Animation bounceAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int transformation = (int) (bounceTransformation * interpolatedTime) - mPreviousBounceTransformation;
                    mPreviousBounceTransformation += transformation;
                    params.topMargin = params.topMargin + transformation;
                    view.setLayoutParams(params);
                }
            };

            moveAnimation.setDuration(500);
            bounceAnimation.setDuration(250);
            bounceAnimation.setStartOffset(500);
            animationSet.addAnimation(moveAnimation);
            animationSet.addAnimation(bounceAnimation);
            view.startAnimation(animationSet);
        }
    }
}
