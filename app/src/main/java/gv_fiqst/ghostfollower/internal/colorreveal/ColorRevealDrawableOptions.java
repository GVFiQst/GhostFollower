package gv_fiqst.ghostfollower.internal.colorreveal;


import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

public class ColorRevealDrawableOptions {
    int duration;
    Interpolator interpolator;

    public ColorRevealDrawableOptions() {
        this(-1, null);
    }

    public ColorRevealDrawableOptions(int duration) {
        this(duration, null);
    }

    public ColorRevealDrawableOptions(Interpolator interpolator) {
        this(-1, interpolator);
    }

    public ColorRevealDrawableOptions(int duration, Interpolator interpolator) {
        this.duration = duration < 0 ? 250 : duration;
        this.interpolator = interpolator == null ? new AccelerateInterpolator() : interpolator;
    }
}
