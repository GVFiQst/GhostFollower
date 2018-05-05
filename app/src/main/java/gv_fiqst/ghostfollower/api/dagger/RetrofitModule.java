package gv_fiqst.ghostfollower.api.dagger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.api.impl.service.InstagramService;
import gv_fiqst.ghostfollower.api.impl.service.TwitterService;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    TwitterService provideTwitterService(@Named("tweet") Retrofit retrofit) {
        return retrofit.create(TwitterService.class);
    }

    @Provides
    @Singleton
    InstagramService provideInstagramService(@Named("inst") Retrofit retrofit) {
        return retrofit.create(InstagramService.class);
    }
}
