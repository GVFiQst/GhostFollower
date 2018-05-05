package gv_fiqst.ghostfollower.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "inst_users")
public class InstagramUser {

    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "instagram_id")
    private String id;

    @ColumnInfo(name = "img_url")
    private String imageUrl;


    public InstagramUser(String name, @NonNull String id, String imageUrl) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public InstagramUser setName(String name) {
        this.name = name;
        return this;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public InstagramUser setId(@NonNull String id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public InstagramUser setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public String toString() {
        return "InstagramUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
