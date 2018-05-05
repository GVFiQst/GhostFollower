package gv_fiqst.ghostfollower.ui.activities.settings.dagger;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.internal.mvp.ViewModule;
import gv_fiqst.ghostfollower.ui.activities.settings.SettingsContract;
import gv_fiqst.ghostfollower.ui.activities.settings.screen.SettingsActivity;
import gv_fiqst.ghostfollower.ui.activities.settings.screen.SettingsPresenter;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class SettingsModule extends ViewModule<SettingsActivity> {

    public SettingsModule(SettingsActivity view) {
        super(view);
    }

    @Provides
    @SettingsScope
    SettingsContract.View provideView() {
        return mView;
    }

    @Provides
    @SettingsScope
    SettingsContract.Presenter providePresenter(
            CompositeDisposable cd,
            SettingsContract.View view,
            DbChooser repository,
            ApiChooser apiChooser
    ) {
        return new SettingsPresenter(cd, view, repository, apiChooser);
    }

    @Provides
    @SettingsScope
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
