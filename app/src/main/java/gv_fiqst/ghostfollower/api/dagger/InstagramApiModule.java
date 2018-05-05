package gv_fiqst.ghostfollower.api.dagger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.api.impl.InstagramApiManager;
import gv_fiqst.ghostfollower.api.impl.gson.CustomGsonConverterFactory;
import gv_fiqst.ghostfollower.api.impl.jsoup.JsoupConverterFactory;
import gv_fiqst.ghostfollower.api.impl.service.InstagramService;
import gv_fiqst.ghostfollower.api.model.inst.InstagramSearchResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
public class InstagramApiModule {
    private static final String INSTAGRAM_BASE_URL = "https://www.instagram.com/";

    @Provides
    @Singleton
    @Named("inst")
    JsoupConverterFactory provideJsoupConverterFactory() {
        return JsoupConverterFactory.builder()
                .build();
    }

    @Provides
    @Singleton
    @Named("inst")
    CustomGsonConverterFactory provideCustomGsonConverterFactory() {
        return CustomGsonConverterFactory.builder()
                .allow(InstagramSearchResponse.class)
                .build();
    }

    @Provides
    @Singleton
    @Named("inst")
    Retrofit provideTwitterRetrofit(@Named("inst") JsoupConverterFactory jcf, @Named("inst") CustomGsonConverterFactory cgcf) {
        return new Retrofit.Builder()
                .baseUrl(INSTAGRAM_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(cgcf)
                .addConverterFactory(jcf)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    InstagramApiManager provideInstagramApiManager(InstagramService service) {
        return new InstagramApiManager(service);
    }
}
