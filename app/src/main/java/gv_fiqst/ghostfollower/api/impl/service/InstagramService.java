package gv_fiqst.ghostfollower.api.impl.service;


import gv_fiqst.ghostfollower.api.model.inst.InstagramSearchResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InstagramService {

    @GET("/web/search/topsearch/")
    Single<InstagramSearchResponse> fetchSearch(@Query("query") String q);
}
