package gv_fiqst.ghostfollower.api.impl.service;


import java.util.List;

import gv_fiqst.ghostfollower.api.model.twitter.TwitterPost;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterPostLink;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterUser;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TwitterService {
    @GET
    Single<List<TwitterUser>> searchFor(@Url String url, @Query("q") String query);

    @GET
    Observable<TwitterPost> getFeed(@Url String url);

    @GET
    Observable<List<TwitterPostLink>> getPostsUrl(@Url String url);
}
