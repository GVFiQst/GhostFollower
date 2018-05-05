package gv_fiqst.ghostfollower.internal.colorreveal;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ColorRevealDrawable extends Drawable {
    @ColorInt
    private int mMainColor;

    private int mWidth = -1;
    private int mHeight = -1;
    private Paint mPaint;

    private final List<RevealInfo> mReveals = new ArrayList<>();
    private ColorRevealDrawableOptions mOptions;

    public ColorRevealDrawable(@ColorInt int mainColor) {
        mMainColor = mainColor;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        setOptions(new ColorRevealDrawableOptions());
    }

    public void setOptions(ColorRevealDrawableOptions opts) {
        mOptions = opts;

        invalidateSelf();
    }

    public void addReveal(@ColorInt int toColor, int x, int y, @Nullable AnimationListener listener) {
        int w = getIntrinsicWidth();
        int h = getIntrinsicHeight();

        Point p = getMostDistantPoint(w, h, x, y);
        RevealInfo info = new RevealInfo();
        info.xy = new Point(x, y);
        info.finalRadius = (float) Math.sqrt(Math.pow(p.x - info.xy.x, 2) + Math.pow(p.y - info.xy.y, 2));
        info.color = toColor;
        info.progress = 0;

        mReveals.add(info.start(listener));
        invalidateSelf();
    }

    private void remove(RevealInfo info) {
        mMainColor = info.color;
        mReveals.remove(info);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        if (w != mWidth) {
            mWidth = w;
        }
        if (h != mHeight) {
            mHeight = h;
        }

        canvas.drawColor(mMainColor);

        for (RevealInfo info : mReveals) {
            mPaint.setColor(info.color);
            canvas.drawCircle(info.xy.x, info.xy.y, info.radius, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // not used
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        // not used
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    private Point getMostDistantPoint(int w, int h, int x, int y) {
        float w2 = w / 2f;
        float h2 = h / 2f;

        boolean[] isLeftTop = new boolean[2];
        if (x > w2) {
            isLeftTop[0] = true;
        } else {
            isLeftTop[0] = false;
        }

        if (y > h2) {
            isLeftTop[1] = true;
        } else {
            isLeftTop[1] = false;
        }

        Log.d("lox", "getMostDistantPoint(" + w + ", " + h + ", " + x + ", " + y + ") returned "
                + "Point(" + (isLeftTop[0] ? "0" : "w") + ", " + (isLeftTop[0] ? "0" : "h") + ")");
        return new Point(
                isLeftTop[0] ? 0 : w, isLeftTop[1] ? 0 : h
        );
    }

    public void setMailColor(int color) {
        mMainColor = color;

        invalidateSelf();
    }

    public interface AnimationListener {
        void onUpdateAnimation(float progress);
    }

    private class RevealInfo implements ValueAnimator.AnimatorUpdateListener {
        public int color = 0;
        public float progress = 0;
        public float radius = 0;
        public float finalRadius = 0;
        public Point xy;

        private ValueAnimator mAnimator;
        private AnimationListener mListener;

        public RevealInfo start(AnimationListener listener) {
            mListener = listener;
            return start();
        }

        public RevealInfo start() {
            mAnimator = ValueAnimator.ofFloat(0, 1);
            mAnimator.setInterpolator(mOptions.interpolator);
            mAnimator.setDuration(mOptions.duration);
            mAnimator.addUpdateListener(this);
            mAnimator.start();

            return this;
        }

        public void update(float p) {
            if (mListener != null) {
                mListener.onUpdateAnimation(p);
            }

            if (p == 1) {
                xy = null;
                progress = 0;

                mAnimator.removeAllUpdateListeners();
                mAnimator = null;
                remove(this);
            } else {
                progress = p;
                radius = finalRadius * p;
            }

            invalidateSelf();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            update((float) animation.getAnimatedValue());
        }
    }
}
