package gv_fiqst.ghostfollower.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by GV_FiQst on 05.05.2018.
 */

@Entity(tableName = "twitter_posts")
public class TwitterPost {

    @NonNull
    @PrimaryKey
    @ColumnInfo(index = true, name = "id")
    private String mId;

    @ColumnInfo(index = true, name = "sender_name")
    private String mSenderName;

    @ColumnInfo(index = true, name = "sender_id")
    private String mSenderSubName;

    @ColumnInfo(name = "sender_img")
    private String mSenderImageUrl;

    @ColumnInfo(name = "status")
    private String mStatus;

    @ColumnInfo(name = "post_url")
    private String mPostUrl;

    @ColumnInfo(name = "attachment")
    private String mAttachmentImageUrl;

    @ColumnInfo(name = "content_text")
    private String mText;

    @ColumnInfo(index = true, name = "timestamp")
    private long mTimestamp;

    public TwitterPost(@NonNull String mId, String mSenderName, String mSenderSubName,
                       String mSenderImageUrl, String mStatus, String mPostUrl,
                       String mAttachmentImageUrl, String mText, long mTimestamp) {
        this.mId = mId;
        this.mSenderName = mSenderName;
        this.mSenderSubName = mSenderSubName;
        this.mSenderImageUrl = mSenderImageUrl;
        this.mStatus = mStatus;
        this.mPostUrl = mPostUrl;
        this.mAttachmentImageUrl = mAttachmentImageUrl;
        this.mText = mText;
        this.mTimestamp = mTimestamp;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public TwitterPost setId(@NonNull String mId) {
        this.mId = mId;
        return this;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public TwitterPost setSenderName(String mSenderName) {
        this.mSenderName = mSenderName;
        return this;
    }

    public String getSenderSubName() {
        return mSenderSubName;
    }

    public TwitterPost setSenderSubName(String mSenderSubName) {
        this.mSenderSubName = mSenderSubName;
        return this;
    }

    public String getSenderImageUrl() {
        return mSenderImageUrl;
    }

    public TwitterPost setSenderImageUrl(String mSenderImageUrl) {
        this.mSenderImageUrl = mSenderImageUrl;
        return this;
    }

    public String getStatus() {
        return mStatus;
    }

    public TwitterPost setStatus(String mStatus) {
        this.mStatus = mStatus;
        return this;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public TwitterPost setPostUrl(String mPostUrl) {
        this.mPostUrl = mPostUrl;
        return this;
    }

    public String getAttachmentImageUrl() {
        return mAttachmentImageUrl;
    }

    public TwitterPost setAttachmentImageUrl(String mAttachmentImageUrl) {
        this.mAttachmentImageUrl = mAttachmentImageUrl;
        return this;
    }

    public String getText() {
        return mText;
    }

    public TwitterPost setText(String mText) {
        this.mText = mText;
        return this;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public TwitterPost setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
        return this;
    }
}
