package gv_fiqst.ghostfollower.util;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

public class MyCompat {
    private static final Compat sImpl;

    static {
        switch (Build.VERSION.SDK_INT) {
            default:
            case 14: sImpl = new CompatImpl14(); break;
            case 15: sImpl = new CompatImpl15(); break;
            case 16: sImpl = new CompatImpl16(); break;
            case 17: sImpl = new CompatImpl17(); break;
            case 18: sImpl = new CompatImpl18(); break;
            case 19: sImpl = new CompatImpl19(); break;
            case 20: sImpl = new CompatImpl20(); break;
            case 21: sImpl = new CompatImpl21(); break;
            case 22: sImpl = new CompatImpl22(); break;
            case 23: sImpl = new CompatImpl23(); break;
            case 24: sImpl = new CompatImpl24(); break;
            case 25: sImpl = new CompatImpl25(); break;
            case 26: sImpl = new CompatImpl26(); break;
            case 27: sImpl = new CompatImpl27(); break;
        }
    }

    public static Compat get() {
        return sImpl;
    }

    private MyCompat() {
    }

    @SuppressWarnings("SameParameterValue")
    public interface Compat {
        void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener l);

        void setStatusBarColor(Window window, int color);
        void applyStatusBarHeight(View view, boolean withMargin);
    }

    @RequiresApi(14)
    private static class CompatImpl14 implements Compat {

        @Override
        public void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener l) {
            observer.removeGlobalOnLayoutListener(l);
        }

        @Override
        public void setStatusBarColor(Window window, int color) {
        }

        @Override
        public void applyStatusBarHeight(View view, boolean withMargin) {
        }
    }

    @RequiresApi(15)
    private static class CompatImpl15 extends CompatImpl14 {
    }

    @RequiresApi(16)
    private static class CompatImpl16 extends CompatImpl15 {

        @Override
        public void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener l) {
            observer.removeOnGlobalLayoutListener(l);
        }
    }

    @RequiresApi(17)
    private static class CompatImpl17 extends CompatImpl16 {
    }

    @RequiresApi(18)
    private static class CompatImpl18 extends CompatImpl17 {
    }

    @RequiresApi(19)
    private static class CompatImpl19 extends CompatImpl18 {
    }

    @RequiresApi(20)
    private static class CompatImpl20 extends CompatImpl19 {
    }

    @RequiresApi(21)
    private static class CompatImpl21 extends CompatImpl20 {
        private int mStatusBarHeight = -1;

        @Override
        public void setStatusBarColor(Window window, int color) {
            window.getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    );

            window.setStatusBarColor(color);
        }

        @Override
        public void applyStatusBarHeight(View view, boolean withMargin) {
            if (mStatusBarHeight <= 0) {
                mStatusBarHeight = Util.getStatusBarHeight(view.getContext());
            }

            if (withMargin) {
                ViewGroup.LayoutParams p = view.getLayoutParams();
                if (p instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) p;

                    params.topMargin += mStatusBarHeight;
                    view.setLayoutParams(params);
                }
            } else {
                int top = view.getPaddingTop();
                int bottom = view.getPaddingBottom();
                int left = view.getPaddingLeft();
                int right = view.getPaddingRight();

                view.setPadding(
                        left, top + mStatusBarHeight,
                        right, bottom
                );
            }
        }
    }

    @RequiresApi(22)
    private static class CompatImpl22 extends CompatImpl21 {
    }

    @RequiresApi(23)
    private static class CompatImpl23 extends CompatImpl22 {
    }

    @RequiresApi(24)
    private static class CompatImpl24 extends CompatImpl23 {
    }

    @RequiresApi(25)
    private static class CompatImpl25 extends CompatImpl24 {
    }

    @RequiresApi(26)
    private static class CompatImpl26 extends CompatImpl25 {
    }

    @RequiresApi(27)
    private static class CompatImpl27 extends CompatImpl26 {
    }
}
