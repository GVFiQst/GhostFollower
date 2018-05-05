
package gv_fiqst.ghostfollower.api.model.inst;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstagramSearchResponse {

    @SerializedName("users")
    private List<UserResult> mUsers;

    @SerializedName("status")
    private String mStatus;

    public InstagramSearchResponse() {
    }

    public InstagramSearchResponse(List<UserResult> users, String status) {
        mUsers = users;
        mStatus = status;
    }

    public List<UserResult> getUsers() {
        return mUsers;
    }

    public void setUsers(List<UserResult> users) {
        mUsers = users;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "InstagramSearchResponse{" +
                "mUsers=" + mUsers +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }

    public static class UserResult {

        @SerializedName("position")
        private int mPosition;

        @SerializedName("user")
        private User mUser;

        public UserResult() {
        }

        public UserResult(int position, User user) {
            mPosition = position;
            mUser = user;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public User getUser() {
            return mUser;
        }

        public void setUser(User user) {
            mUser = user;
        }

        @Override
        public String toString() {
            return "UserResult{" +
                    "mPosition=" + mPosition +
                    ", mUser=" + mUser +
                    '}';
        }
    }

}
