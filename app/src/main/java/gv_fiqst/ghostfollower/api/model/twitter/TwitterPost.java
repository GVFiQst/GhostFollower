package gv_fiqst.ghostfollower.api.model.twitter;



public class TwitterPost {

    private String mSenderName;

    private String mSenderSubName;

    private String mSenderImageUrl;

    private String mText;

    private String mAttachment;

    private long mTimestamp;

    public TwitterPost(String senderName, String senderSubName,
                       String senderImageUrl, String text,
                       String attachment, long timestamp) {
        mSenderName = senderName;
        mSenderSubName = senderSubName;
        mSenderImageUrl = senderImageUrl;
        mText = text;
        mAttachment = attachment;
        mTimestamp = timestamp;
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

    public String getText() {
        return mText;
    }

    public String getAttachment() {
        return mAttachment;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
