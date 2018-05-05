package gv_fiqst.ghostfollower.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gv_fiqst.ghostfollower.api.impl.service.TwitterService;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterPost;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterUser;
import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.ApiManager;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import io.reactivex.Observable;
import io.reactivex.Single;


public class TwitterApiManager implements ApiManager {
    private static final String MOBILE_URL = "https://mobile.twitter.com/";
    private static final String NORMAL_URL = "https://twitter.com/";
    private static final String SEARCH_METHOD = "search?f=users";

    private TwitterService mService;

    public TwitterApiManager(TwitterService service, DbChooser chooser) {
        mService = service;
    }

    @Override
    public Single<List<SocialUser>> findUsers(String query) {
        return mService.searchFor(NORMAL_URL + SEARCH_METHOD, query)
                .map(this::parse);
    }

    @Override
    public Single<List<SocialPost>> getFeed(List<SocialUser> users) {
        return Observable.fromIterable(users)
                .flatMap(user -> {
                    return mService.getPostsUrl(MOBILE_URL + user.getSocialId().substring(1))
                            .flatMap(Observable::fromIterable)
                            .flatMap(link -> {
                                return mService.getFeed(NORMAL_URL + link.getLink())
                                        .map(post -> {
                                            return new PostWrapper(
                                                    link.getLink(), user.getSocialId(), post
                                            );
                                        });
                            });
                })
                .map(this::parse)
                .toList(5)
                .doOnSuccess(list -> Collections.sort(list, (o1, o2) ->
                        Long.compare(o1.getTimestamp(), o2.getTimestamp()) * -1
                ));
    }

    private SocialPost parse(PostWrapper wrapper) {
        TwitterPost post = wrapper.getPost();
        String status = wrapper.getSearchedName().equalsIgnoreCase(post.getSenderSubName())
                ? "" : wrapper.getSearchedName() + " retweeted";

        return new SocialPost(wrapper.getLink(),
                post.getSenderName(), post.getSenderSubName(), post.getSenderImageUrl(),
                NORMAL_URL + wrapper.getLink().substring(1), post.getText(), status,
                post.getAttachment(), post.getTimestamp()
        );
    }

    private List<SocialUser> parse(List<TwitterUser> list) {
        List<SocialUser> result = new ArrayList<>();

        for (TwitterUser user : list) {
            result.add(new SocialUser(
                    user.getId(), user.getName(), user.getProfileUrl()
            ));
        }

        return result;
    }



    private static class PostWrapper {
        private String mLink;
        private String mSearchedName;
        private TwitterPost mPost;

        public PostWrapper(String link, String searchedName, TwitterPost post) {
            mLink = link;
            mSearchedName = searchedName;
            mPost = post;
        }

        public String getLink() {
            return mLink;
        }

        public String getSearchedName() {
            return mSearchedName;
        }

        public TwitterPost getPost() {
            return mPost;
        }
    }
}
