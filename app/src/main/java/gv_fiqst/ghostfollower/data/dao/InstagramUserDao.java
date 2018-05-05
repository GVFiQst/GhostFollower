package gv_fiqst.ghostfollower.data.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gv_fiqst.ghostfollower.data.model.InstagramUser;
import io.reactivex.Single;

@Dao
public interface InstagramUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstagramUser twitterUser);

    @Delete
    void delete(InstagramUser twitterUser);

    @Query("SELECT * FROM inst_users")
    Single<List<InstagramUser>> getAll();
}
