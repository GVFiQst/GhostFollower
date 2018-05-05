package gv_fiqst.ghostfollower.internal.colorreveal;


import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

interface Colorable {
    void setColor(int color);
    void release();

    class ColorableTextView implements Colorable {
        private TextView mWrapped;

        public ColorableTextView(TextView wrapped) {
            mWrapped = wrapped;
        }

        @Override
        public void setColor(int color) {
            if (mWrapped != null) {
                mWrapped.setTextColor(color);
            }
        }

        @Override
        public void release() {
            mWrapped = null;
        }
    }

    class ColorableToolbar implements Colorable {
        private Toolbar mToolbar;

        public ColorableToolbar(Toolbar toolbar) {
            mToolbar = toolbar;
        }

        @Override
        public void setColor(int color) {
            if (mToolbar != null) {
                mToolbar.setNavigationIcon(
                        transform(mToolbar.getNavigationIcon(), color)
                );

                mToolbar.setOverflowIcon(
                        transform(mToolbar.getOverflowIcon(), color)
                );
            }
        }

        private Drawable transform(Drawable drawable, int color) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }

            return drawable;
        }

        @Override
        public void release() {
            mToolbar = null;
        }
    }

    class ColorableSlidingTabStrip implements Colorable {
        private View mTabStrip;
        private Method mChangeColorMethod;

        @SuppressLint("PrivateApi")
        public ColorableSlidingTabStrip(View tabStrip) {
            mTabStrip = tabStrip;

            try {
                mChangeColorMethod = mTabStrip.getClass()
                        .getDeclaredMethod("setSelectedIndicatorColor", int.class);
                mChangeColorMethod.setAccessible(true);
            } catch (Exception e) {
                Log.d("lox", "", e);
                mTabStrip = null;
            }
        }

        @Override
        public void setColor(int color) {
            if (mTabStrip != null) {
                try {
                    mChangeColorMethod.invoke(mTabStrip, color);
                } catch (Exception e) {
                    Log.d("lox", "", e);
                    mTabStrip = null;
                }
            }
        }

        @Override
        public void release() {
            mTabStrip = null;
            mChangeColorMethod = null;
        }
    }
}
