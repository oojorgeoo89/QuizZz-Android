package rv.jorge.quizzz.screens.support;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * ViewPager with scrolling disabled.
 *
 * To scroll to the previous/next page, call setCurrentItem() programmatically.
 *
 */

public class NonScrollableViewPager extends ViewPager {

    public NonScrollableViewPager(Context context) {
        super(context);
    }

    public NonScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

}
