package gv_fiqst.ghostfollower.domain.repo;


import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import io.reactivex.Single;

public interface ApiManager {

    Single<List<SocialUser>> findUsers(String query);
    Single<List<SocialPost>> getFeed(List<SocialUser> users);
}
