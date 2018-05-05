package gv_fiqst.ghostfollower.app.dagger;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.app.App;

@Module
public class AppModule {

    @Provides
    Context provideContext() {
        return App.get();
    }
}
