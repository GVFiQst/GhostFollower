package gv_fiqst.ghostfollower.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gv_fiqst.ghostfollower.data.model.TwitterPost;
import io.reactivex.Single;

@Dao
public interface TwitterPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TwitterPost> twitterUser);

    @Query("SELECT * FROM twitter_posts WHERE sender_id = :id")
    Single<List<TwitterPost>> getAllForUser(String id);

    @Query("SELECT * FROM twitter_posts")
    Single<List<TwitterPost>> getAll();

    @Query("SELECT * FROM twitter_posts  ORDER BY timestamp DESC LIMIT :offset, :limit")
    Single<List<TwitterPost>> getAll(int offset, int limit);
}
