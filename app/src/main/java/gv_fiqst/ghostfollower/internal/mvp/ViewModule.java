package gv_fiqst.ghostfollower.internal.mvp;

public class ViewModule<T extends MvpView> {
    protected T mView;

    public ViewModule(T view) {
        mView = view;
    }
}
