package gv_fiqst.ghostfollower.ui.activities.search.screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
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
import gv_fiqst.ghostfollower.internal.adapter.Metadata;
import gv_fiqst.ghostfollower.internal.adapter.ModelAdapter;
import gv_fiqst.ghostfollower.internal.adapter.ModelViewHolder;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.ui.activities.search.SearchContract;
import gv_fiqst.ghostfollower.ui.activities.search.dagger.SearchModule;
import gv_fiqst.ghostfollower.util.Applier;
import gv_fiqst.ghostfollower.util.MyCompat;
import gv_fiqst.ghostfollower.util.Util;


public class SearchActivity extends AppCompatActivity implements SearchContract.View, FeedViewHelper.Callback {

    @BindView(R.id.edtQuery)
    TextView mQueryTextView;

    @BindView(R.id.result_card)
    View mResultCard;

    private FeedViewHelper mFeedViewHelper;
    private Unbinder mUnbinder;

    private RecyclerView mRecyclerView;

    private ModelAdapter<SocialUser> mAdapter;

    private int mType;

    @Inject
    SearchContract.Presenter mPresenter;

    private ProgressDialog mPendingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.appComponent()
                .searchComponent(new SearchModule(this))
                .inject(this);

        mType = getIntent().getIntExtra("type", -1);
        setTheme(mType == ManagerType.FACEBOOK
                ? R.style.FacebookTheme
                : mType == ManagerType.TWITTER
                ? R.style.TwitterTheme
                : mType == ManagerType.INSTAGRAM
                ? R.style.InstagramTheme
                : R.style.AppTheme);

        setContentView(R.layout.activity_fb_search);

        mFeedViewHelper = FeedViewHelper.forStandaloneView(
                findViewById(R.id.result_card), this
        ).setContentView(mRecyclerView = new RecyclerView(this));
        mUnbinder = ButterKnife.bind(this);

        mFeedViewHelper.showContent();
        mResultCard.setVisibility(View.GONE);

        MyCompat.get().applyStatusBarHeight(findViewById(R.id.root), false);

        mRecyclerView.setAdapter(mAdapter = new ModelAdapter<SocialUser>((socialUser, pos, action) -> {
            if (action == 1) {
                mPresenter.save(new SearchContract.SaveWrapper(socialUser, pos));

                showProgress("Adding...");
            }
        }) {
            @Override
            protected ModelViewHolder<SocialUser> createVH(ViewGroup viewGroup, int viewType) {
                return new SearchViewHolder(
                        LayoutInflater.from(SearchActivity.this)
                                .inflate(R.layout.item_facebook_user, viewGroup, false)
                );
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.root).setBackgroundColor(Util.getThemeColor(getTheme(), R.attr.colorButtonBack));

        mPresenter.setType(mType);
    }

    private void showProgress(String msg) {
        if (mPendingDialog != null) {
            if (mPendingDialog.isShowing()) {
                mPendingDialog.dismiss();
            }

            mPendingDialog = null;
        }

        mPendingDialog = ProgressDialog.show(this, "", msg);
        mPendingDialog.setCancelable(true);
        mPendingDialog.setOnDismissListener(dialog -> {
            dialog.dismiss();
            hideDialog();
        });
    }

    private void hideDialog() {
        mPresenter.cancel();

        if (mPendingDialog != null) {
            mPendingDialog.dismiss();
            mPendingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
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

    @OnClick(R.id.btnSearch)
    public void search() {
        String q = mQueryTextView.getText().toString();

        if (!q.isEmpty()) {
            mResultCard.setVisibility(View.VISIBLE);
            mFeedViewHelper.showLoading("Searching...");
            mPresenter.search(q);
        } else {
            Toast.makeText(this, "Search field is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowContentView(View view) {
    }

    @Override
    public void onErrorButtonClick(int errorCode) {

    }

    @Override
    public void updateView(ModelResult<List<SocialUser>> result) {
        hideDialog();
        if (result.isSuccessful()) {
            List<SocialUser> list = result.getResult();

            if (list.isEmpty()) {
                mFeedViewHelper.showErrorMessage("No results", "", -1);
            } else {
                mFeedViewHelper.showContent();
                mAdapter.setList(list);
            }
        } else if (result.hasException()) {
            mFeedViewHelper.showErrorMessage(result.getException().getLocalizedMessage(), "", -1);
        } else {
            mFeedViewHelper.showErrorMessage("", "", -1);
            mResultCard.setVisibility(View.GONE);

            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
            Log.d("lox", "Some error occurred");
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserAdded(SearchContract.SaveWrapper wrapper) {
        hideDialog();
        showMessage(wrapper.getUser().getSocialName() + " added");

        mAdapter.putMetadata(wrapper.getPosition(), Boolean.class, true);
    }

    static class SearchViewHolder extends ModelViewHolder<SocialUser> {

        @BindView(R.id.txtName)
        TextView mNameView;

        @BindView(R.id.txtType)
        TextView mIdView;

        @BindView(R.id.imgAvatar)
        ImageView mAvatarView;

        @BindView(R.id.btnAdd)
        ImageView mBtnAdd;

        private Unbinder mUnbinder;

        public SearchViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onCreate() {
            super.onCreate();

            mUnbinder = ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBindHolder(SocialUser socialUser, @Nullable Metadata metadata) {
            Applier.apply(metadata, meta -> {
                Boolean alreadyAdded = meta.getMeta(Boolean.class);

                if (alreadyAdded != null && alreadyAdded) {
                    mBtnAdd.setImageResource(R.drawable.ic_done);
                }
            });

            mNameView.setText(socialUser.getSocialName());
            mIdView.setText(socialUser.getSocialId());

            setTint(itemView.getContext(), mBtnAdd);

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
                    ResourcesCompat.getColor(context.getResources(), R.color.colorAdd, context.getTheme())
            ));
        }

        @OnClick(R.id.btnAdd)
        public void addUser() {
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
