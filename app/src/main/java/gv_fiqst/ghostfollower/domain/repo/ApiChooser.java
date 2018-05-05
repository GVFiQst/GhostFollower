package gv_fiqst.ghostfollower.domain.repo;


import gv_fiqst.ghostfollower.app.ManagerType;

public interface ApiChooser {

    ApiManager getManager(@ManagerType.Def int manager);
}
