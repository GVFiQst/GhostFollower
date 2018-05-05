package gv_fiqst.ghostfollower.ui.activities.search.dagger;

import dagger.Subcomponent;
import gv_fiqst.ghostfollower.internal.mvp.Injectable;
import gv_fiqst.ghostfollower.ui.activities.search.screen.SearchActivity;

@SearchScope
@Subcomponent(modules = SearchModule.class)
public interface SearchComponent extends Injectable<SearchActivity> {
}
