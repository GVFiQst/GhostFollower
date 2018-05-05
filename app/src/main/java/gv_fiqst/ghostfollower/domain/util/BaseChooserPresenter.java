package gv_fiqst.ghostfollower.domain.util;

import android.util.Log;

import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.ApiManager;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.domain.repo.DbRepository;
import gv_fiqst.ghostfollower.internal.mvp.BaseMvpPresenter;
import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseChooserPresenter extends BaseMvpPresenter {
    private DbChooser mDbChooser;
    private ApiChooser mApiChooser;

    protected ApiManager mApi;
    protected DbRepository mRepo;

    protected BaseChooserPresenter(CompositeDisposable cd, DbChooser dbChooser, ApiChooser apiChooser) {
        super(cd);

        mDbChooser = dbChooser;
        mApiChooser = apiChooser;
    }

    public void setType(@ManagerType.Def int type) {
        try {
            mRepo = getDb(type);
            mApi = getApi(type);
        } catch (IllegalTypeException e) {
            onError(e);
        }
    }

    protected void onError(Throwable throwable) {
        Log.d("lox", "", throwable);
    }

    protected ApiManager getApi(int type) throws IllegalTypeException {
        ApiManager manager = mApiChooser.getManager(type);
        if (manager == null) {
            throw new IllegalTypeException("No Api manager implemented for type: " + type);
        }

        return manager;
    }

    protected DbRepository getDb(int type) throws IllegalTypeException {
        DbRepository repository = mDbChooser.getDbRepository(type);
        if (repository == null) {
            throw new IllegalTypeException("No Database repository implemented for type: " + type);
        }

        return repository;
    }

    public static class IllegalTypeException extends Exception {
        public IllegalTypeException() {
        }

        public IllegalTypeException(String message) {
            super(message);
        }

        public IllegalTypeException(String message, Throwable cause) {
            super(message, cause);
        }

        public IllegalTypeException(Throwable cause) {
            super(cause);
        }
    }
}
