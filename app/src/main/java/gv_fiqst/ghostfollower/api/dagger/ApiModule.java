package gv_fiqst.ghostfollower.api.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.api.impl.ApiChooserImpl;
import gv_fiqst.ghostfollower.api.impl.InstagramApiManager;
import gv_fiqst.ghostfollower.api.impl.TwitterApiManager;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;

@Module
public class ApiModule {


    @Provides
    @Singleton
    ApiChooser provideApiChooser(TwitterApiManager twitter, InstagramApiManager instagram) {
        return new ApiChooserImpl(twitter, instagram);
    }
}
