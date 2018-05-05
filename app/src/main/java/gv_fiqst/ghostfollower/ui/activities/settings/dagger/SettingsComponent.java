package gv_fiqst.ghostfollower.ui.activities.settings.dagger;

import dagger.Subcomponent;
import gv_fiqst.ghostfollower.internal.mvp.Injectable;
import gv_fiqst.ghostfollower.ui.activities.settings.screen.SettingsActivity;


@SettingsScope
@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent extends Injectable<SettingsActivity> {
}
