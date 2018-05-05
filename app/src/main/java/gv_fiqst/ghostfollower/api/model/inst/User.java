
package gv_fiqst.ghostfollower.api.model.inst;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String username;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("is_private")
    private boolean isPrivate;

    @SerializedName("profile_pic_url")
    private String profilePicUrl;

    @SerializedName("profile_pic_id")
    private String profilePicId;

    @SerializedName("follower_count")
    private int followerCount;

    @SerializedName("byline")
    private String byline;

    public User() {
    }

    public User(String username, String fullName, boolean isPrivate,
                String profilePicUrl, String profilePicId,
                int followerCount, String byline) {
        this.username = username;
        this.fullName = fullName;
        this.isPrivate = isPrivate;
        this.profilePicUrl = profilePicUrl;
        this.profilePicId = profilePicId;
        this.followerCount = followerCount;
        this.byline = byline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(String profilePicId) {
        this.profilePicId = profilePicId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isPrivate=" + isPrivate +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", profilePicId='" + profilePicId + '\'' +
                ", followerCount=" + followerCount +
                ", byline='" + byline + '\'' +
                '}';
    }
}
