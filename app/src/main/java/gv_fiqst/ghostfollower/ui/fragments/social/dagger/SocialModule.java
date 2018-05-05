package gv_fiqst.ghostfollower.ui.fragments.social.dagger;


import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.internal.mvp.ViewModule;
import gv_fiqst.ghostfollower.ui.fragments.social.SocialFragmentContract;
import gv_fiqst.ghostfollower.ui.fragments.social.screen.SocialFragment;
import gv_fiqst.ghostfollower.ui.fragments.social.screen.SocialPresenter;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class SocialModule extends ViewModule<SocialFragment> {
    public SocialModule(SocialFragment view) {
        super(view);
    }

    @Provides
    @SocialScope
    SocialFragmentContract.View provideView() {
        return mView;
    }

    @Provides
    @SocialScope
    SocialFragmentContract.Presenter providePresenter(
            CompositeDisposable cd,
            SocialFragmentContract.View view,
            ApiChooser chooser,
            DbChooser repository
    ) {
        return new SocialPresenter(cd, view, chooser, repository);
    }

    @Provides
    @SocialScope
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
