package gv_fiqst.ghostfollower.internal.activityinit;

import android.support.annotation.NonNull;

import gv_fiqst.ghostfollower.internal.mvp.MvpPresenter;


public interface PresenterOwner {

    @NonNull
    MvpPresenter getPresenter();
}
