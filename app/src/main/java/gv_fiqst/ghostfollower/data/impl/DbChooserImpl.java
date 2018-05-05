package gv_fiqst.ghostfollower.data.impl;


import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.domain.repo.DbRepository;

public class DbChooserImpl implements DbChooser {

    private TwitterRepository mTwitterDatabase;
    private InstagramRepository mInstagramRepository;

    public DbChooserImpl(TwitterRepository twitter, InstagramRepository instagram) {
        mTwitterDatabase = twitter;
        mInstagramRepository = instagram;
    }


    @Override
    public DbRepository getDbRepository(@ManagerType.Def int repoType) {
        switch (repoType) {
            case ManagerType.FACEBOOK:
                return null;
            case ManagerType.INSTAGRAM:
                return mInstagramRepository;
            case ManagerType.TWITTER:
                return mTwitterDatabase;
        }

        return null;
    }
}
