package gv_fiqst.ghostfollower.domain.repo;


import gv_fiqst.ghostfollower.app.ManagerType;

public interface DbChooser {

    DbRepository getDbRepository(@ManagerType.Def int repoType);
}
