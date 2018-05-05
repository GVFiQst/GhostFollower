package gv_fiqst.ghostfollower.app.dagger;

import javax.inject.Singleton;

import dagger.Component;
import gv_fiqst.ghostfollower.api.dagger.ApiModule;
import gv_fiqst.ghostfollower.api.dagger.InstagramApiModule;
import gv_fiqst.ghostfollower.api.dagger.RetrofitModule;
import gv_fiqst.ghostfollower.api.dagger.TwitterApiModule;
import gv_fiqst.ghostfollower.data.dagger.DbModule;
import gv_fiqst.ghostfollower.ui.activities.search.dagger.SearchComponent;
import gv_fiqst.ghostfollower.ui.activities.search.dagger.SearchModule;
import gv_fiqst.ghostfollower.ui.activities.search.dagger.SearchScope;
import gv_fiqst.ghostfollower.ui.activities.settings.dagger.SettingsComponent;
import gv_fiqst.ghostfollower.ui.activities.settings.dagger.SettingsModule;
import gv_fiqst.ghostfollower.ui.activities.settings.dagger.SettingsScope;
import gv_fiqst.ghostfollower.ui.fragments.social.dagger.SocialComponent;
import gv_fiqst.ghostfollower.ui.fragments.social.dagger.SocialModule;
import gv_fiqst.ghostfollower.ui.fragments.social.dagger.SocialScope;

@Singleton
@Component(modules = {
        AppModule.class,
        ApiModule.class,
        RetrofitModule.class,
        DbModule.class,
        TwitterApiModule.class,
        InstagramApiModule.class
})
public interface AppComponent {

    @SocialScope
    SocialComponent socialComponent(SocialModule module);

    @SettingsScope
    SettingsComponent settingsComponent(SettingsModule module);

    @SearchScope
    SearchComponent searchComponent(SearchModule module);
}
