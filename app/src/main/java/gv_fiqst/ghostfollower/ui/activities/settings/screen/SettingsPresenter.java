package gv_fiqst.ghostfollower.ui.activities.settings.screen;

import android.util.Log;

import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.domain.util.BaseChooserPresenter;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.ui.activities.settings.SettingsContract;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class SettingsPresenter extends BaseChooserPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;

    public SettingsPresenter(CompositeDisposable cd, SettingsContract.View view, DbChooser db, ApiChooser api) {
        super(cd, db, api);

        mView = view;
    }

    @Override
    public void start() {
        super.start();

        update();
    }

    @Override
    protected void onError(Throwable throwable) {
        super.onError(throwable);

        mView.updateView(new ModelResult.Error<>(throwable));
    }

    private void update() {
        if (mRepo == null) {
            return;
        }

        mRepo.getAllSubscribed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> mView.updateView(new ModelResult.Success<>(list)),
                        error -> mView.updateView(new ModelResult.Error<>(error))
                );
    }

    @Override
    public void remove(SocialUser user) {
        if (mRepo == null) {
            return;
        }

        mRepo.unsubscribe(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mView.removed(user),
                        error -> {
                            Log.d("lox", "Error onRemove: ", error);
                            mView.showMessage(error.getMessage());
                        }
                );
    }
}
