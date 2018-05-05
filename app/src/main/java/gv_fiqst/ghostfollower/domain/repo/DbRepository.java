package gv_fiqst.ghostfollower.domain.repo;


import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DbRepository {
    Completable subscribe(SocialUser user);
    Completable unsubscribe(SocialUser user);
    Single<List<SocialUser>> getAllSubscribed();

    Completable update(List<SocialPost> list);
    Single<List<SocialPost>> getForUser(String id);
    Single<List<SocialPost>> getAllPosts();
    Single<List<SocialPost>> getAllPosts(int offset, int limit);
}
