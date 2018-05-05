package gv_fiqst.ghostfollower.ui.fragments.social;


import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.internal.mvp.MvpPresenter;
import gv_fiqst.ghostfollower.internal.mvp.MvpView;

public final class SocialFragmentContract {

    public interface View extends MvpView<List<SocialPost>> {
        void onLoadingStarted();
        void onLimitedData(ModelResult<List<SocialPost>> result);
    }

    public interface Presenter extends MvpPresenter {
        void setType(int type);
        void load();
        void load(int offset, int limit);
    }

    private SocialFragmentContract() {
    }
}
