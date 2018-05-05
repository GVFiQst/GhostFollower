package gv_fiqst.ghostfollower.domain.model;

/**
 * Created by GV_FiQst on 02.05.2018.
 */

public class SocialPost {

    private String mId;

    private String mSenderName;

    private String mSenderSubName;

    private String mSenderImageUrl;

    private String mStatus;

    private String mPostUrl;

    private String mAttachmentImageUrl;

    private String mText;

    private long mTimestamp;

    public SocialPost(String id, String senderName, String senderSubName,
                       String senderImageUrl, String postUrl, String text,
                       String status, String attachment, long timestamp) {
        mId = id;
        mSenderName = senderName;
        mSenderSubName = senderSubName;
        mSenderImageUrl = senderImageUrl;
        mPostUrl = postUrl;
        mText = text;
        mStatus = status;
        mAttachmentImageUrl = attachment;
        mTimestamp = timestamp;
    }

    public String getId() {
        return mId;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public String getSenderSubName() {
        return mSenderSubName;
    }

    public String getSenderImageUrl() {
        return mSenderImageUrl;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public String getText() {
        return mText;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getAttachment() {
        return mAttachmentImageUrl;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
