package gv_fiqst.ghostfollower.ui.activities.search.screen;

import android.util.Log;

import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.domain.util.BaseChooserPresenter;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.ui.activities.search.SearchContract;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchPresenter extends BaseChooserPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;

    private Disposable mCurrent;

    public SearchPresenter(CompositeDisposable cd, SearchContract.View view, ApiChooser apiChooser, DbChooser dbChooser) {
        super(cd, dbChooser, apiChooser);

        mView = view;
    }

    @Override
    protected void onError(Throwable throwable) {
        super.onError(throwable);

        mView.updateView(new ModelResult.Error<>(throwable));
    }

    @Override
    public void search(String q) {
        if (mRepo == null || mApi == null) {
            return;
        }

        mCd.add(mApi
                .findUsers(q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::populateResult,
                        this::populateError
                ));
    }

    private void populateError(Throwable throwable) {
        mView.updateView(new ModelResult.Error<>(throwable));
    }

    @Override
    public void save(SearchContract.SaveWrapper wrapper) {
        if (mRepo == null || mApi == null) {
            return;
        }

        mCurrent = mRepo.subscribe(wrapper.getUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            mCurrent = null;
                            mView.onUserAdded(wrapper);
                        },
                        error -> {
                            mCurrent = null;
                            Log.e("lox", "ERROR SAVING: ", error);
                        }
                );

        mCd.add(mCurrent);
    }

    @Override
    public void cancel() {
        if (mCurrent != null && !mCurrent.isDisposed()) {
            mCurrent.dispose();
            mCurrent = null;
        }
    }

    private void populateResult(List<SocialUser> list) {
        mView.updateView(new ModelResult.Success<>(list));
    }
}
