package gv_fiqst.ghostfollower.data.impl;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import gv_fiqst.ghostfollower.data.dao.InstagramUserDao;
import gv_fiqst.ghostfollower.data.dao.TwitterPostDao;
import gv_fiqst.ghostfollower.data.dao.TwitterUserDao;
import gv_fiqst.ghostfollower.data.model.InstagramUser;
import gv_fiqst.ghostfollower.data.model.TwitterPost;
import gv_fiqst.ghostfollower.data.model.TwitterUser;

@Database(entities = {TwitterUser.class, TwitterPost.class, InstagramUser.class}, version = 1)
public abstract class GfDatabase extends RoomDatabase {
    public abstract TwitterUserDao twitterUserDao();
    public abstract TwitterPostDao twitterPostDao();
    public abstract InstagramUserDao instagramUserDao();
}
