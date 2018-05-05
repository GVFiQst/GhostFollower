package gv_fiqst.ghostfollower.internal.colorreveal;


import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


public class CircularColorRevealUtil implements View.OnTouchListener {
    protected View mTarget;
    private final ColorRevealDrawable mDrawable;

    public CircularColorRevealUtil(View target) {
        mTarget = target;
        mTarget.setOnTouchListener(this);
        ViewCompat.setBackground(mTarget, mDrawable = new ColorRevealDrawable(Color.BLACK));
    }

    public void setMainColor(int color) {
        mDrawable.setMailColor(color);
    }

    public void setOptions(ColorRevealDrawableOptions options) {
        mDrawable.setOptions(options);
    }

    public void startReveal(int toColor, int x, int y, ColorRevealDrawable.AnimationListener listener) {
        mDrawable.addReveal(toColor, x, y, listener);
    }

    public void startReveal(int x, int y) {
        startReveal(getColor(x, y), x, y, null);
    }

    public int getColor(int x, int y) {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    @Override
    @SuppressWarnings("all")
    // Gets touches in target view
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            startReveal((int) event.getX(), (int) event.getY());
        }

        return false;
    }
}
