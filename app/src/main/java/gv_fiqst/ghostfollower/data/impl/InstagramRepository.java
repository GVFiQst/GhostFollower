package gv_fiqst.ghostfollower.data.impl;


import java.util.List;

import gv_fiqst.ghostfollower.data.model.InstagramUser;
import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.DbRepository;
import gv_fiqst.ghostfollower.util.Applier;
import io.reactivex.Completable;
import io.reactivex.Single;

public class InstagramRepository implements DbRepository {
    private GfDatabase mDatabase;

    public InstagramRepository(GfDatabase gfDatabase) {
        mDatabase = gfDatabase;
    }

    @Override
    public Completable subscribe(SocialUser user) {
        return createCompletable(() ->
                mDatabase.instagramUserDao()
                        .insert(new InstagramUser(
                                user.getSocialName(),
                                user.getSocialId(),
                                user.getSocialImgUrl()
                        ))
        );
    }

    @Override
    public Completable unsubscribe(SocialUser user) {
        return createCompletable(() ->
                mDatabase.instagramUserDao()
                        .delete(new InstagramUser(
                                user.getSocialName(),
                                user.getSocialId(),
                                user.getSocialImgUrl()
                        ))
        );
    }

    @Override
    public Single<List<SocialUser>> getAllSubscribed() {
        return mDatabase.instagramUserDao().getAll()
                .map(list -> Applier.transformList(list, tu -> new SocialUser(
                        tu.getId(), tu.getName(), tu.getImageUrl()
                )));
    }

    @Override
    public Completable update(List<SocialPost> list) {
        return Completable.complete();
    }

    @Override
    public Single<List<SocialPost>> getAllPosts() {
        return Single.never();
    }

    @Override
    public Single<List<SocialPost>> getAllPosts(int offset, int limit) {
        return Single.never();
    }

    private static Completable createCompletable(Runnable runnable) {
        return Completable.create(emitter -> {
            try {
                runnable.run();

                if (emitter != null && !emitter.isDisposed()) {
                    emitter.onComplete();
                }
            } catch (Throwable throwable) {
                if (emitter != null && !emitter.isDisposed()) {
                    emitter.onError(throwable);
                }
            }
        });
    }
}
