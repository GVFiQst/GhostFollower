package gv_fiqst.ghostfollower.ui.fragments.social.dagger;

import dagger.Subcomponent;
import gv_fiqst.ghostfollower.internal.mvp.Injectable;
import gv_fiqst.ghostfollower.ui.fragments.social.screen.SocialFragment;

@SocialScope
@Subcomponent(modules = SocialModule.class)
public interface SocialComponent extends Injectable<SocialFragment> {
}
