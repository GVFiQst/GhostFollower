package gv_fiqst.ghostfollower.internal.activityinit.annotations;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ToolbarInit {

    @IdRes
    int id();

    String toolbarTitle() default "";

    @StringRes
    int toolbarTitleId() default 0;

    boolean showBack() default false;
}
