package gv_fiqst.ghostfollower.app;


import android.support.annotation.IntDef;

public interface ManagerType {
    int FACEBOOK = 0;
    int TWITTER = 1;
    int INSTAGRAM = 2;

    @IntDef({FACEBOOK, TWITTER, INSTAGRAM})
    @interface Def {}
}
