package gv_fiqst.ghostfollower.internal.activityinit.annotations;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An activity that is annotated with this annotation
 * must provide a layout res. Then this layout res will be used in
 * {@link com.android.ma.trainee.mymechanic.internal.activityinit.ActivityInfo}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityInit {

    /**
     * Layout resource to be set as activity's content view
     */
    @LayoutRes
    int layout() default 0;

    /**
     * If set as true, your activity MUST implement PresenterOwner interface.
     */
    boolean hasPresenter() default false;

    String[] requiresArgs() default {};
}
