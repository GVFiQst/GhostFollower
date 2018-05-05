package gv_fiqst.ghostfollower.ui.fragments.social.screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gv_fiqst.ghostfollower.R;
import gv_fiqst.ghostfollower.app.App;
import gv_fiqst.ghostfollower.app.ManagerType;
import gv_fiqst.ghostfollower.domain.model.SocialPost;
import gv_fiqst.ghostfollower.internal.FeedViewHelper;
import gv_fiqst.ghostfollower.internal.adapter.Metadata;
import gv_fiqst.ghostfollower.internal.adapter.ModelAdapter;
import gv_fiqst.ghostfollower.internal.adapter.ModelViewHolder;
import gv_fiqst.ghostfollower.internal.mvp.ModelResult;
import gv_fiqst.ghostfollower.ui.fragments.social.SocialFragmentContract;
import gv_fiqst.ghostfollower.ui.fragments.social.dagger.SocialModule;
import gv_fiqst.ghostfollower.util.Applier;
import gv_fiqst.ghostfollower.util.Util;


public class SocialFragment extends Fragment implements SocialFragmentContract.View, FeedViewHelper.Callback {

    @Inject
    SocialFragmentContract.Presenter mPresenter;

    @ManagerType.Def
    private int mType = -1;

    private Context mThemedContext;

    private FeedViewHelper mFeedHolder;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ModelAdapter<SocialPost> mAdapter;

    @Override
    public void onAttach(Context context) {
        Bundle args = getArguments();

        if (args != null) {
            mType = getArguments().getInt("type", -1);
        }

        super.onAttach(mThemedContext = new ContextThemeWrapper(context, getStyleInt()));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mThemedContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mType = getArguments().getInt("type", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int backgroundColor = Color.WHITE;
        if (getArguments() != null) {
            backgroundColor = getArguments().getInt("backColor");
        }

        mSwipeRefreshLayout = new SwipeRefreshLayout(mThemedContext);
        mSwipeRefreshLayout.addView(
                mRecyclerView = new RecyclerView(mThemedContext), new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        inflater = inflater.cloneInContext(mThemedContext);
        return (mFeedHolder = FeedViewHelper.forFragment(inflater, container, this))
                .setContentView(mSwipeRefreshLayout)
                .setBackground(backgroundColor)
                .getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFeedHolder.showLoading("Loading...");

        ViewCompat.setBackgroundTintList(
                mFeedHolder.getErrorButton(),
                Util.tint(Util.getThemeColor(mThemedContext.getTheme(), R.attr.colorButtonBack))
        );

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new ModelAdapter<SocialPost>((post, pos, action) -> {

        }) {
            @Override
            protected ModelViewHolder<SocialPost> createVH(ViewGroup viewGroup, int viewType) {
                return viewType == 0 ? new ViewHolder(
                        LayoutInflater.from(mThemedContext)
                                .inflate(R.layout.item_facebook_post, viewGroup, false)
                ) : new ModelViewHolder<SocialPost>(LayoutInflater.from(mThemedContext)
                        .inflate(R.layout.loading_more, viewGroup, false)) {
                    @Override
                    protected void onBindHolder(SocialPost post, @Nullable Metadata metadata) {
                    }
                };
            }

            @Override
            public void onBindViewHolder(ModelViewHolder<SocialPost> holder, int position) {
                if (position < getSuperItemCount()) {
                    super.onBindViewHolder(holder, position);
                }
            }

            @Override
            public int getItemViewType(int position) {
                return position < getSuperItemCount() ? 0 : -1;
            }

            private int getSuperItemCount() {
                return super.getItemCount();
            }

            @Override
            public int getItemCount() {
                return getSuperItemCount() + 1;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 1) {
                    addNext();
                }
            }
        });

        App.appComponent()
                .socialComponent(new SocialModule(this))
                .inject(this);

        mPresenter.setType(mType);
        mSwipeRefreshLayout.setOnRefreshListener(mPresenter::load);
    }

    private void addNext() {
        mPresenter.load(mAdapter.getItemCount() - 1, 5);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.start();
    }

    private int getStyleInt() {
        switch (mType) {
            case ManagerType.FACEBOOK:
                return R.style.FacebookTheme;

            case ManagerType.TWITTER:
                return R.style.TwitterTheme;

            case ManagerType.INSTAGRAM:
                return R.style.InstagramTheme;
        }

        return -1;
    }

    @Override
    public void updateView(ModelResult<List<SocialPost>> result) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (result.isSuccessful()) {
            List<SocialPost> list = result.getResult();
            if (list.isEmpty()) {
                mFeedHolder.showErrorMessage("Feed is empty!", "Retry", 0);
            } else {
                mFeedHolder.showContent();
                mAdapter.setList(list);
            }
        } else if (result.hasException()) {
            Throwable throwable = result.getException();

            Log.e("lox", "Error in SocialFragment: ", throwable);

            mFeedHolder.showErrorMessage(throwable.getMessage(), "Retry", 0);
        } else {
            Log.e("lox", "Unknown error");
            Toast.makeText(mThemedContext, "Unknown error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mThemedContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowContentView(View view) {

    }

    @Override
    public void onErrorButtonClick(int errorCode) {
        if (errorCode == 0) {
            mPresenter.load();
        }
    }

    @Override
    public void onLoadingStarted() {
        if (mFeedHolder.isContentShown()) {
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            mFeedHolder.showLoading("Loading...");
        }
    }

    @Override
    public void onLimitedData(ModelResult<List<SocialPost>> result) {
        if (result.isSuccessful()) {
            List<SocialPost> list = result.getResult();
            if (!list.isEmpty()) {
                mAdapter.addAll(list);
            }
        }
    }

    static class ViewHolder extends ModelViewHolder<SocialPost> {
        @BindView(R.id.imgAvatar)
        ImageView mAvatarView;

        @BindView(R.id.imgAttachment)
        ImageView mAttachmentView;

        @BindView(R.id.txtName)
        TextView mNameTextView;

        @BindView(R.id.txtTime)
        TextView mTimeTextView;

        @BindView(R.id.txtMessage)
        TextView mTextTextView;

        @BindView(R.id.txtStatus)
        TextView mStatusTextView;

        private Unbinder mUnbinder;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onCreate() {
            super.onCreate();

            mUnbinder = ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBindHolder(SocialPost post, @Nullable Metadata metadata) {
            Glide.with(itemView.getContext())
                    .load(post.getSenderImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mAvatarView);

            if (!TextUtils.isEmpty(post.getAttachment())) {
                mAttachmentView.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(post.getAttachment())
                        .apply(RequestOptions.centerCropTransform())
                        .into(mAttachmentView);
            } else {
                mAttachmentView.setVisibility(View.GONE);
            }

            mNameTextView.setText(post.getSenderName());
            mTimeTextView.setText(Util.format("HH:mm dd.MM.yyyy", post.getTimestamp()));
            mTextTextView.setText(post.getText());
            mStatusTextView.setText(post.getStatus());
        }

        @Override
        protected void onRelease() {
            super.onRelease();

            mUnbinder = Applier.release(mUnbinder, Unbinder::unbind, true);
        }
    }
}
