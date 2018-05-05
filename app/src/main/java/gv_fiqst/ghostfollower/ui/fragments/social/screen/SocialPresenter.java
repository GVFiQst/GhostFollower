package gv_fiqst.ghostfollower.ui.fragments.social.screen;

import android.util.Log;

import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.domain.util.BaseChooserPresenter;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.ui.fragments.social.SocialFragmentContract;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class SocialPresenter extends BaseChooserPresenter implements SocialFragmentContract.Presenter {
    private SocialFragmentContract.View mView;

    public SocialPresenter(
            CompositeDisposable cd,
            SocialFragmentContract.View view,
            ApiChooser apiChooser,
            DbChooser dbChooser
    ) {
        super(cd, dbChooser, apiChooser);

        mView = view;
    }

    @Override
    public void setType(int type) {
        super.setType(type);

        if (mRepo == null || mApi == null) {
            return;
        }

        mView.onLoadingStarted();
        mRepo.getAllPosts(0, 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        p -> {
                            if (p.isEmpty()) {
                                load();
                            } else {
                                mView.updateView(new ModelResult.Success<>(p));
                            }
                        },
                        err -> Log.e("lox", "SocialPresenter#start: ", err)
                );
    }

    @Override
    public void start() {
        super.start();

        load();
    }

    @Override
    protected void onError(Throwable throwable) {
        mView.updateView(new ModelResult.Error<>(throwable));
    }

    @Override
    public void load() {
        if (mRepo == null || mApi == null) {
            return;
        }

        mView.onLoadingStarted();
        loadNew()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        p -> mView.updateView(new ModelResult.Success<>(p)),
                        err -> Log.e("lox", "SocialPresenter#start: ", err)
                );
    }

    private Single<List<SocialPost>> loadNew() {
        return mRepo.getAllSubscribed()
                .flatMap(mApi::getFeed)
                .flatMapCompletable(mRepo::update)
                .andThen(mRepo.getAllPosts(0, 15));
    }

    @Override
    public void load(int offset, int limit) {
        if (mRepo == null || mApi == null) {
            return;
        }

        mView.onLoadingStarted();
        mRepo.getAllPosts(offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        p -> mView.onLimitedData(new ModelResult.Success<>(p)),
                        err -> Log.e("lox", "SocialPresenter#load(" + offset + ", " + limit + "): ", err)
                );
    }
}
