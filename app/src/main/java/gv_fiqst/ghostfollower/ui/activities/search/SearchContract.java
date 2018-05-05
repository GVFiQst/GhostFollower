package gv_fiqst.ghostfollower.ui.activities.search;

import java.util.List;

import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.internal.mvp.MvpPresenter;
import gv_fiqst.ghostfollower.internal.mvp.MvpView;


public class SearchContract {

    public interface View extends MvpView<List<SocialUser>> {
        void onUserAdded(SaveWrapper wrapper);
    }

    public interface Presenter extends MvpPresenter {
        void setType(int type);

        void search(String q);

        void save(SaveWrapper wrapper);
        void cancel();
    }

    private SearchContract() {
    }

    public static class SaveWrapper {
        private SocialUser mUser;
        private int mPosition;

        public SaveWrapper(SocialUser user, int position) {
            mUser = user;
            mPosition = position;
        }

        public SocialUser getUser() {
            return mUser;
        }

        public int getPosition() {
            return mPosition;
        }
    }
}
