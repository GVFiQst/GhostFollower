package gv_fiqst.ghostfollower.data.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.data.impl.DbChooserImpl;
import gv_fiqst.ghostfollower.data.impl.GfDatabase;
import gv_fiqst.ghostfollower.data.impl.InstagramRepository;
import gv_fiqst.ghostfollower.data.impl.TwitterRepository;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;


@Module
public class DbModule {

    @Provides
    @Singleton
    GfDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, GfDatabase.class, "GhostFollowerDatabase").build();
    }

    @Provides
    @Singleton
    TwitterRepository provideRoomRepository(GfDatabase database) {
        return new TwitterRepository(database);
    }

    @Provides
    @Singleton
    InstagramRepository provideInstagramRepository(GfDatabase database) {
        return new InstagramRepository(database);
    }

    @Provides
    @Singleton
    DbChooser provideDbChooser(TwitterRepository twitter, InstagramRepository instagram) {
        return new DbChooserImpl(twitter, instagram);
    }
}
