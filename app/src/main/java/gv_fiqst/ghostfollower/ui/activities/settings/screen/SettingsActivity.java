package gv_fiqst.ghostfollower.ui.activities.settings.screen;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gv_fiqst.ghostfollower.R;
import gv_fiqst.ghostfollower.app.App;
import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.domain.model.SocialUser;
import gv_fiqst.ghostfollower.internal.FeedViewHelper;
import gv_fiqst.ghostfollower.internal.activityinit.PresenterOwner;
import gv_fiqst.ghostfollower.internal.activityinit.annotations.ActivityInit;
import gv_fiqst.ghostfollower.internal.adapter.Metadata;
import gv_fiqst.ghostfollower.internal.adapter.ModelAdapter;
import gv_fiqst.ghostfollower.internal.adapter.ModelViewHolder;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.internal.mvp.MvpPresenter;
import gv_fiqst.ghostfollower.ui.activities.search.screen.SearchActivity;
import gv_fiqst.ghostfollower.ui.activities.settings.SettingsContract;
import gv_fiqst.ghostfollower.ui.activities.settings.dagger.SettingsModule;
import gv_fiqst.ghostfollower.util.MyCompat;
import gv_fiqst.ghostfollower.util.Util;

@ActivityInit(hasPresenter = true)
public class SettingsActivity
        extends AppCompatActivity
        implements SettingsContract.View, PresenterOwner, FeedViewHelper.Callback {

    private static final String ACTION_RECEIVE_LOGIN = "com.gvfiqst.ghostfollower.FacebookSettingsActivity.ACTION_RECEIVE_LOGIN";
    public static final String ACTION_LOG_IN = "com.gvfiqst.ghostfollower.LOG_IN_WITH_FACEBOOK";

    @BindView(R.id.layoutFollowed)
    ViewGroup mFollowedLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private int mType;

    private Unbinder mUnbinder;
//    private SocialManager mSocialManager;

    private FeedViewHelper mFeedViewHelper;
    private FeedViewHelper mFollowedViewHelper;

    private ModelAdapter<SocialUser> mAdapter;

    @Inject
    SettingsContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.appComponent()
                .settingsComponent(new SettingsModule(this))
                .inject(this);

        mType = getIntent().getIntExtra("type", -1);
        setTheme(mType == ManagerType.FACEBOOK
                ? R.style.FacebookTheme
                : mType == ManagerType.TWITTER
                ? R.style.TwitterTheme
                : mType == ManagerType.INSTAGRAM
                ? R.style.InstagramTheme
                : R.style.AppTheme);

        mFeedViewHelper = FeedViewHelper.forActivity(this, this)
                .setContentView(this, R.layout.activity_fb_settings);

        mFollowedViewHelper = FeedViewHelper.forStandaloneView(
                findViewById(R.id.layoutFollowed), this
        ).setContentView(this, R.layout.fb_settings_followed);

        mFeedViewHelper.showContent();
        mFollowedViewHelper.showLoading("Loading...");

        mUnbinder = ButterKnife.bind(this, mFeedViewHelper.getContentView());

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        ColorStateList tintColor = Util.tint(Util.getThemeColor(getTheme(), R.attr.colorButtonBack));
        ViewCompat.setBackgroundTintList(mFeedViewHelper.getErrorButton(), tintColor);
        ViewCompat.setBackgroundTintList(mFollowedViewHelper.getErrorButton(), tintColor);
        ViewCompat.setBackgroundTintList(fab, tintColor);
        ImageViewCompat.setImageTintList(fab, Util.tint(Util.getThemeColor(getTheme(), R.attr.colorContrast)));

        MyCompat.get().setStatusBarColor(getWindow(), Color.parseColor("#55000000"));
        MyCompat.get().applyStatusBarHeight(mFeedViewHelper.getView(), false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ModelAdapter<SocialUser>((socialUser, pos, action) -> {
            if (action == 1) {
                Util.showDialog(this, "Delete " + socialUser.getSocialName() + "?", (dialog, which) -> {
                    dialog.dismiss();
                    mPresenter.remove(socialUser);
                });
            }
        }) {
            @Override
            public ModelViewHolder<SocialUser> createVH(ViewGroup parent, int viewType) {
                return new UserViewHolder(
                        LayoutInflater.from(SettingsActivity.this).inflate(
                                R.layout.item_facebook_user, parent, false
                        )
                );
            }
        });

        mPresenter.setType(mType);
    }

    @Override
    protected void onDestroy() {
        mRecyclerView.setAdapter(null);

        if (mFollowedViewHelper != null) {
            mFollowedViewHelper.release();
            mFollowedViewHelper = null;
        }

        if (mFeedViewHelper != null) {
            mFeedViewHelper.release();
            mFeedViewHelper = null;
        }

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onShowContentView(View view) {

    }

    @Override
    public void onErrorButtonClick(int errorCode) {
        switch (errorCode) {
            case 1:
                break;

            case 2:
                addFollowed();
                break;
        }
    }


    @OnClick(R.id.fabAdd)
    public void addFollowed() {
        startActivity(new Intent(this, SearchActivity.class)
                .putExtra("type", mType));
    }

    @Override
    public void updateView(ModelResult<List<SocialUser>> result) {
        if (result.isSuccessful()) {
            List<SocialUser> list = result.getResult();

            if (list.isEmpty()) {
                mFollowedViewHelper.showErrorMessage("You have no followings", "", -1);
            } else {
                mFollowedViewHelper.showContent();
                mAdapter.setList(list);
            }
        } else if (result.hasException()) {
            mFollowedViewHelper.showErrorMessage(result.getException().getLocalizedMessage(), "", -1);
        } else {
            mFollowedViewHelper.showErrorMessage("Unknown error", "", -1);

            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
            Log.d("lox", "Some error occurred");
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public MvpPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void removed(SocialUser user) {
        if (mAdapter.remove(user)) {
            mFollowedViewHelper.showErrorMessage("You have no followings", "", -1);
        }
    }

    static class UserViewHolder extends ModelViewHolder<SocialUser> {
        @BindView(R.id.imgAvatar)
        ImageView mAvatarView;

        @BindView(R.id.txtName)
        TextView mNameTextView;

        @BindView(R.id.btnAdd)
        ImageView mAddedButton;

        @BindView(R.id.txtType)
        TextView mTypeTextView;

        private Unbinder mUnbinder;

        public UserViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onCreate() {
            super.onCreate();

            mUnbinder = ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBindHolder(SocialUser socialUser, @Nullable Metadata metadata) {
            mNameTextView.setText(socialUser.getSocialName());
            mTypeTextView.setText(socialUser.getSocialId());

            setTint(itemView.getContext(), mAddedButton);
            mAddedButton.setImageResource(R.drawable.ic_delete);

            String url = socialUser.getSocialImgUrl();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mAvatarView);
            }
        }

        private static void setTint(Context context, ImageView view) {
            ImageViewCompat.setImageTintList(view, Util.tint(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorRemove, context.getTheme())
            ));
        }

        @OnClick(R.id.btnAdd)
        public void delete() {
            performAction(1);
        }

        @Override
        protected void onRelease() {
            super.onRelease();

            if (mUnbinder != null) {
                mUnbinder.unbind();
                mUnbinder = null;
            }
        }
    }
}