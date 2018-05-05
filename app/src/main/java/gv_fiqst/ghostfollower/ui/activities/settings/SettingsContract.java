package gv_fiqst.ghostfollower.ui.activities.settings;


import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.internal.mvp.MvpPresenter;
import gv_fiqst.ghostfollower.internal.mvp.MvpView;

public class SettingsContract {

    public interface View extends MvpView<List<SocialUser>> {
        void removed(SocialUser user);
    }

    public interface Presenter extends MvpPresenter {
        void setType(int type);
        void remove(SocialUser user);
    }

    private SettingsContract() {
    }
}
