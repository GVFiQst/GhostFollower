package gv_fiqst.ghostfollower.api.model.twitter;


import gv_fiqst.ghostfollower.api.model.User;

public class TwitterUser implements User {

    private String mId;
    private String mName;
    private String mProfileUrl;

    public TwitterUser(String id, String name, String profileUrl) {
        mId = id;
        mName = name;
        mProfileUrl = profileUrl;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }
}
