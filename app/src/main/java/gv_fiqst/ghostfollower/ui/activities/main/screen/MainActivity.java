package gv_fiqst.ghostfollower.ui.activities.main.screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;

import butterknife.BindView;
import gv_fiqst.ghostfollower.R;
import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.internal.activityinit.annotations.ActivityInit;
import gv_fiqst.ghostfollower.internal.colorreveal.PagerRevealUtil;
import gv_fiqst.ghostfollower.ui.activities.settings.screen.SettingsActivity;
import gv_fiqst.ghostfollower.util.MyCompat;

@ActivityInit(layout = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private PagerRevealUtil mPagerRevealUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyCompat.Compat impl = MyCompat.get();
        impl.setStatusBarColor(getWindow(), Color.parseColor("#55000000"));
        impl.applyStatusBarHeight(mAppBar, false);

        setSupportActionBar(mToolbar);
        mPagerRevealUtil = new PagerRevealUtil(mAppBar, mViewPager, mTabLayout);
        mPagerRevealUtil.setupPager(this, getSupportFragmentManager(), R.xml.pages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    @SuppressLint("PrivateApi")
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int type;

        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);

            case R.id.menu_facebook:
                type = ManagerType.FACEBOOK;
                break;
            case R.id.menu_twitter:
                type = ManagerType.TWITTER;
                break;
            case R.id.menu_instagram:
                type = ManagerType.INSTAGRAM;
                break;
        }

        startActivity(new Intent(this, SettingsActivity.class)
                .putExtra("type", type));
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPagerRevealUtil.release();
    }
}
