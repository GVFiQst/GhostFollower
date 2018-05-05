package gv_fiqst.ghostfollower.data.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gv_fiqst.ghostfollower.data.model.TwitterUser;
import io.reactivex.Single;

@Dao
public interface TwitterUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TwitterUser twitterUser);

    @Delete
    void delete(TwitterUser twitterUser);

    @Query("SELECT * FROM twitter_users")
    Single<List<TwitterUser>> getAll();
}
