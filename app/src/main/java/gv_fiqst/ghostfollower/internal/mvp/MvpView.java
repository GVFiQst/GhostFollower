package gv_fiqst.ghostfollower.internal.mvp;

public interface MvpView<Model> {
    void updateView(ModelResult<Model> result);
    void showMessage(String message);
}
