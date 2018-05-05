package gv_fiqst.ghostfollower.internal.activityinit;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import gv_fiqst.ghostfollower.internal.activityinit.annotations.ActivityInit;
import gv_fiqst.ghostfollower.internal.activityinit.annotations.ToolbarInit;

class ActivityInfo {

    @Nullable
    private Unbinder mUnbinder;

    @Nullable
    private PresenterOwner mHandler;

    public ActivityInfo(Activity activity, Bundle savedState) {
        Class<? extends Activity> cls = activity.getClass();

        if (cls.isAnnotationPresent(ActivityInit.class)) {
            ActivityInit init = cls
                    .getAnnotation(ActivityInit.class);

            int layout = init.layout();
            if (layout != 0) {
                activity.setContentView(init.layout());
                mUnbinder = ButterKnife.bind(activity);
            }

            if (init.hasPresenter()) {
                if (!(activity instanceof PresenterOwner)) {
                    throw new IllegalArgumentException("If ActivityInit.hasPresenter set as true, your activity must implement PresenterOwner interface!");
                }

                mHandler = (PresenterOwner) activity;
            }

            String[] args = init.requiresArgs();
            if (args.length > 0) {
                Intent intent = activity.getIntent();

                for (String arg : args) {
                    assertArgs(intent, arg, cls.getSimpleName());
                }
            }
        }

        if (cls.isAnnotationPresent(ToolbarInit.class)) {
            ToolbarInit init = cls
                    .getAnnotation(ToolbarInit.class);

            Toolbar toolbar = Objects.requireNonNull(
                    activity.findViewById(init.id()),
                    "Activity annotated with ToolbarInit must have toolbar with an id = ToolbarInit.id"
            );

            if (activity instanceof AppCompatActivity) {
                AppCompatActivity compatActivity = (AppCompatActivity) activity;
                compatActivity.setSupportActionBar(toolbar);

                ActionBar actionBar = Objects.requireNonNull(compatActivity.getSupportActionBar());
                String name = getName(activity, init);

                actionBar.setTitle(name);
                actionBar.setDisplayHomeAsUpEnabled(init.showBack());
            }
        }
    }

    private void assertArgs(Intent intent, String arg, String activityClassName) {
        if (!intent.hasExtra(arg)) {
            throw new IllegalArgumentException("Activity " + activityClassName + " requires argument " + arg);
        }
    }

    private String getName(Context context, ToolbarInit init) {
        String name = init.toolbarTitle();
        if (name.isEmpty()) {
            try {
                name = context.getString(init.toolbarTitleId());
            } catch (Resources.NotFoundException e) {
                throw new IllegalArgumentException(
                        "You must specify one of toolbarTitle() or toolbarTitleId()",
                        e
                );
            }
        }

        return name;
    }

    public void start() {
        if (mHandler != null) {
            mHandler.getPresenter().start();
        }
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.getPresenter().stop();
        }
    }

    public void release() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }

        if (mHandler != null) {
            mHandler.getPresenter().release();
            mHandler = null;
        }
    }
}
