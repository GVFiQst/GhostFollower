package gv_fiqst.ghostfollower.internal.mvp;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseMvpPresenter implements MvpPresenter {

    protected final CompositeDisposable mCd;

    protected BaseMvpPresenter(CompositeDisposable cd) {
        mCd = cd;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void release() {
        mCd.dispose();
    }
}
