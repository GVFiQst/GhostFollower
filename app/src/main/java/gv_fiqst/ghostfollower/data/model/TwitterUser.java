package gv_fiqst.ghostfollower.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "twitter_users")
public class TwitterUser {

    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "twitter_id")
    private String twitterId;

    @ColumnInfo(name = "img_url")
    private String imageUrl;

    public TwitterUser(String name, @NonNull String twitterId, String imageUrl) {
        this.name = name;
        this.twitterId = twitterId;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public TwitterUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public TwitterUser setTwitterId(String twitterId) {
        this.twitterId = twitterId;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public TwitterUser setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public String toString() {
        return "TwitterUser{" +
                " name='" + name + '\'' +
                ", twitterId='" + twitterId + '\'' +
                ", imageUrl='" + imageUrl + "' }";
    }
}
