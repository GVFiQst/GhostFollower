package gv_fiqst.ghostfollower.internal.activityinit;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to provide automatization in initializing an Activity.
 * It sets a content view and creates bindings via ButterKnife.bind()
 */
public class ActivityHandler implements Application.ActivityLifecycleCallbacks {
    private static final ActivityHandler sSingleton = new ActivityHandler();

    public static Application.ActivityLifecycleCallbacks get() {
        return sSingleton;
    }

    private final Map<Activity, ActivityInfo> mActivities;

    private ActivityHandler() {
        mActivities = new HashMap<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        addActivity(activity, bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
    }

    private void addActivity(Activity activity, Bundle bundle) {
        if (mActivities.containsKey(activity)) {
            removeActivity(activity);
        }

        mActivities.put(activity, new ActivityInfo(activity, bundle));
    }

    private void removeActivity(Activity activity) {
        ActivityInfo info = mActivities.remove(activity);

        if (info != null) {
            info.release();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ActivityInfo info = mActivities.get(activity);

        if (info != null) {
            info.start();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ActivityInfo info = mActivities.get(activity);

        if (info != null) {
            info.stop();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
}
