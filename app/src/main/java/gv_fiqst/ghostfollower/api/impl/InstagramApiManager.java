package gv_fiqst.ghostfollower.api.impl;

import android.util.Log;

import java.util.List;

import gv_fiqst.ghostfollower.api.impl.service.InstagramService;
import gv_fiqst.ghostfollower.api.model.inst.InstagramSearchResponse;
import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.ApiManager;
import gv_fiqst.ghostfollower.util.Applier;
import io.reactivex.Single;



public class InstagramApiManager implements ApiManager {
    private InstagramService mService;

    public InstagramApiManager(InstagramService service) {
        mService = service;
    }

    @Override
    public Single<List<SocialUser>> findUsers(String query) {
        return mService.fetchSearch(query)
                .doOnSuccess(response -> Log.d("lox", response + ""))
                .map(InstagramSearchResponse::getUsers)
                .map(results -> Applier.transformList(results, InstagramSearchResponse.UserResult::getUser))
                .map(users -> Applier.transformList(users, u -> u.isPrivate() ? null : u))
                .map(users -> Applier.transformList(users, u -> new SocialUser(
                        u.getUsername(), u.getFullName(), u.getProfilePicUrl()
                )));
    }

    @Override
    public Single<List<SocialPost>> getFeed(List<SocialUser> users) {
        return Single.never();
    }
}
