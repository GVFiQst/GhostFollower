package gv_fiqst.ghostfollower.api.impl;

import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.domain.repo.ApiChooser;
import gv_fiqst.ghostfollower.domain.repo.ApiManager;



public class ApiChooserImpl implements ApiChooser {
    private TwitterApiManager mTwitterApiManager;
    private InstagramApiManager mInstagramApiManager;

    public ApiChooserImpl(TwitterApiManager twitter, InstagramApiManager instagram) {
        mTwitterApiManager = twitter;
        mInstagramApiManager = instagram;
    }

    @Override
    public ApiManager getManager(@ManagerType.Def int manager) {
        switch (manager) {
            case ManagerType.FACEBOOK:
                return null;
            case ManagerType.INSTAGRAM:
                return mInstagramApiManager;
            case ManagerType.TWITTER:
                return mTwitterApiManager;
        }

        return null;
    }
}
