package gv_fiqst.ghostfollower.data.impl;


import java.util.List;

import gv_fiqst.ghostfollower.data.model.TwitterPost;
import gv_fiqst.ghostfollower.data.model.TwitterUser;
import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.domain.repo.DbRepository;
import gv_fiqst.ghostfollower.util.Applier;
import io.reactivex.Completable;
import io.reactivex.Single;

public class TwitterRepository implements DbRepository {

    private GfDatabase mDatabase;

    public TwitterRepository(GfDatabase database) {
        mDatabase = database;
    }

    @Override
    public Completable subscribe(SocialUser user) {
        return createCompletable(() ->
                mDatabase.twitterUserDao()
                        .insert(new TwitterUser(
                                user.getSocialName(),
                                user.getSocialId(),
                                user.getSocialImgUrl()
                        ))
        );
    }

    @Override
    public Completable unsubscribe(SocialUser user) {
        return createCompletable(() ->
                mDatabase.twitterUserDao()
                        .delete(new TwitterUser(
                                user.getSocialName(),
                                user.getSocialId(),
                                user.getSocialImgUrl()
                        ))
        );
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

    @Override
    public Single<List<SocialUser>> getAllSubscribed() {
        return mDatabase.twitterUserDao().getAll()
                .map(list -> Applier.transformList(list, tu -> new SocialUser(
                        tu.getTwitterId(), tu.getName(), tu.getImageUrl()
                )));
    }

    @Override
    public Completable update(List<SocialPost> list) {
        return createCompletable(() -> mDatabase.twitterPostDao()
                .insert(Applier.transformList(
                        list, item -> new TwitterPost(
                                item.getId(), item.getSenderName(),
                                item.getSenderSubName(), item.getSenderImageUrl(),
                                item.getStatus(), item.getPostUrl(), item.getAttachment(),
                                item.getText(), item.getTimestamp()
                        )
                )));
    }

    @Override
    public Single<List<SocialPost>> getForUser(String id) {
        return mDatabase.twitterPostDao()
                .getAllForUser(id)
                .map(twitterPosts -> Applier.transformList(
                        twitterPosts,
                        item -> new SocialPost(
                                item.getId(), item.getSenderName(), item.getSenderSubName(),
                                item.getSenderImageUrl(), item.getPostUrl(), item.getText(),
                                item.getStatus(), item.getAttachmentImageUrl(), item.getTimestamp()
                        )
                ));
    }

    @Override
    public Single<List<SocialPost>> getAllPosts() {
        return mDatabase.twitterPostDao().getAll()
                .map(list -> Applier.transformList(list, item -> new SocialPost(
                        item.getId(), item.getSenderName(), item.getSenderSubName(),
                        item.getSenderImageUrl(), item.getPostUrl(), item.getText(),
                        item.getStatus(), item.getAttachmentImageUrl(), item.getTimestamp()
                )));
    }

    @Override
    public Single<List<SocialPost>> getAllPosts(int offset, int limit) {
        return mDatabase.twitterPostDao().getAll(offset, limit)
                .map(list -> Applier.transformList(list, item -> new SocialPost(
                        item.getId(), item.getSenderName(), item.getSenderSubName(),
                        item.getSenderImageUrl(), item.getPostUrl(), item.getText(),
                        item.getStatus(), item.getAttachmentImageUrl(), item.getTimestamp()
                )));
    }
}
