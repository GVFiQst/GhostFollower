package gv_fiqst.ghostfollower.app;

import android.app.Application;

import gv_fiqst.ghostfollower.app.dagger.AppComponent;
import gv_fiqst.ghostfollower.app.dagger.DaggerAppComponent;
import gv_fiqst.ghostfollower.internal.activityinit.ActivityHandler;


public class App extends Application {
    private static App sApp;
    private static AppComponent sAppComponent;

    public static App get() {
        return sApp;
    }

    public static AppComponent appComponent() {
        return sAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;
        sAppComponent = DaggerAppComponent.create();

        registerActivityLifecycleCallbacks(ActivityHandler.get());
    }
}
