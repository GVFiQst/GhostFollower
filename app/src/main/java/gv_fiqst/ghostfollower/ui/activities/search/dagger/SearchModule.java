package gv_fiqst.ghostfollower.ui.activities.search.dagger;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.internal.mvp.ViewModule;
import gv_fiqst.ghostfollower.ui.activities.search.SearchContract;
import gv_fiqst.ghostfollower.ui.activities.search.screen.SearchActivity;
import gv_fiqst.ghostfollower.ui.activities.search.screen.SearchPresenter;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class SearchModule extends ViewModule<SearchActivity> {

    public SearchModule(SearchActivity view) {
        super(view);
    }

    @Provides
    @SearchScope
    SearchContract.View provideView() {
        return mView;
    }

    @Provides
    @SearchScope
    SearchContract.Presenter providePresenter(
            CompositeDisposable cd,
            SearchContract.View view,
            ApiChooser chooser,
            DbChooser repository
    ) {
        return new SearchPresenter(cd, view, chooser, repository);
    }

    @Provides
    @SearchScope
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
