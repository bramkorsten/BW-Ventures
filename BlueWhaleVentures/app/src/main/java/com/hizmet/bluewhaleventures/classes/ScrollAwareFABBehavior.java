package com.hizmet.bluewhaleventures.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
            super();
        }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        //child -> Floating Action Button
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
//            child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new AccelerateInterpolator()).start();
            child.animate().withEndAction(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.INVISIBLE);
                }
            }).scaleX(0).setInterpolator(new AccelerateInterpolator()).start();
            child.animate().scaleY(0).setInterpolator(new AccelerateInterpolator()).start();
        } else if (dyConsumed < 0) {
            child.setVisibility(View.VISIBLE);
//            child.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            child.animate().scaleX(1).setInterpolator(new DecelerateInterpolator()).start();
            child.animate().scaleY(1).setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}
