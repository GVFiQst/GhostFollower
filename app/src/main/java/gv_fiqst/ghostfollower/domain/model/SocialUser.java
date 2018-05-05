package gv_fiqst.ghostfollower.domain.model;


public class SocialUser {
    private String mSocialId;
    private String mSocialName;
    private String mSocialImgUrl;

    public SocialUser(String socialId, String socialName, String socialImgUrl) {
        mSocialId = socialId;
        mSocialName = socialName;
        mSocialImgUrl = socialImgUrl;
    }

    public String getSocialId() {
        return mSocialId;
    }

    public String getSocialName() {
        return mSocialName;
    }

    public String getSocialImgUrl() {
        return mSocialImgUrl;
    }

    @Override
    public String toString() {
        return "SocialUser{" +
                "mSocialId='" + mSocialId + '\'' +
                ", mSocialName='" + mSocialName + '\'' +
                ", mSocialImgUrl='" + mSocialImgUrl + '\'' +
                '}';
    }
}
