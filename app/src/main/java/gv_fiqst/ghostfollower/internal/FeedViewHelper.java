package gv_fiqst.ghostfollower.internal;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gv_fiqst.ghostfollower.R;

public class FeedViewHelper {
    public static final int STATE_NONE = 0;
    public static final int STATE_CONTENT = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_ERROR = 3;

    public static FeedViewHelper.Builder build() {
        return new Builder();
    }

    public static FeedViewHelper forActivity(Activity activity, Callback callback) {
        return new FeedViewHelper(activity, callback);
    }

    public static FeedViewHelper forFragment(LayoutInflater inflater, ViewGroup container, Callback callback) {
        return new FeedViewHelper(inflater, container, callback);
    }

    public static FeedViewHelper forStandaloneView(ViewGroup parent, Callback callback) {
        return new FeedViewHelper(parent, callback);
    }

    @BindView(R.id.layoutError)
    View mLayoutError;

    @BindView(R.id.textView)
    TextView mErrorTextView;

    @BindView(R.id.button)
    Button mErrorButton;

    @BindView(R.id.layoutLoading)
    View mLayoutLoading;

    @BindView(R.id.textLoading)
    TextView mLoadingTextView;

    @BindView(R.id.content)
    ViewGroup mContentView;

    private Unbinder mUnbinder;
    private View mInflated;
    private Callback mCallback;
    private int mErrorCode = -1;
    private boolean isReleased = false;

    @State
    private int mCurrentState = STATE_NONE;

    /* For standalone views */
    private FeedViewHelper(ViewGroup container, Callback callback) {
        this(LayoutInflater.from(container.getContext()), container, callback);

        container.addView(mInflated, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    /* For fragments */
    private FeedViewHelper(LayoutInflater inflater, ViewGroup container, Callback callback) {
        init(inflater.inflate(R.layout.feed_view, container, false), callback);
    }

    /* For activities */
    private FeedViewHelper(Activity activity, Callback callback) {
        activity.setContentView(R.layout.feed_view);
        init(activity.getWindow().getDecorView(), callback);
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public boolean isContentShown() {
        return mCurrentState == STATE_CONTENT;
    }

    public boolean isLoadingShown() {
        return mCurrentState == STATE_LOADING;
    }

    public boolean isErrorShown() {
        return mCurrentState == STATE_ERROR;
    }

    public boolean isNothingShown() {
        return mCurrentState == STATE_NONE;
    }

    private void init(View inflated, Callback callback) {
        mInflated = inflated;
        mUnbinder = ButterKnife.bind(this, mInflated);
        mCallback = callback;

        mLayoutError.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
    }

    public void showErrorMessage(@NonNull String msg, @Nullable String buttonText, int errorCode) {
        if (isReleased) return;

        mErrorCode = errorCode;
        mContentView.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);

        mErrorTextView.setText(msg);
        if (TextUtils.isEmpty(buttonText)) {
            mErrorButton.setVisibility(View.GONE);
        } else {
            mErrorButton.setVisibility(View.VISIBLE);
            mErrorButton.setText(buttonText);
        }

        mCurrentState = STATE_ERROR;
    }

    public void showLoading(String text) {
        if (isReleased) return;

        mContentView.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.VISIBLE);

        if (text != null && !text.isEmpty()) {
            mLoadingTextView.setVisibility(View.VISIBLE);
            mLoadingTextView.setText(text);
        } else {
            mLoadingTextView.setVisibility(View.GONE);
        }

        mCurrentState = STATE_LOADING;
    }

    public void showContent() {
        if (isReleased || isContentShown()) return;

        mErrorCode = -1;
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);

        if (mCallback != null) {
            mCallback.onShowContentView(mContentView);
        }

        mCurrentState = STATE_CONTENT;
    }

    public FeedViewHelper setContentView(Context context, @LayoutRes int layoutRes) {
        if (!isReleased) {
            LayoutInflater
                    .from(context)
                    .inflate(layoutRes, mContentView, true);
        }

        return this;
    }

    public FeedViewHelper setContentView(View view) {
        if (!isReleased) {
            mContentView.addView(view, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        return this;
    }

    public FeedViewHelper setBackground(Drawable background) {
        ViewCompat.setBackground(mInflated, background);

        return this;
    }

    public FeedViewHelper setBackground(@ColorInt int color) {
        mInflated.setBackgroundColor(color);

        return this;
    }

    public View getView() {
        return mInflated;
    }

    public View getContentView() {
        return mContentView;
    }

    public Button getErrorButton() {
        return mErrorButton;
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        if (mCallback != null) {
            mCallback.onErrorButtonClick(mErrorCode);
        }
    }

    public void release() {
        isReleased = true;

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }

        mInflated = null;
        mCallback = null;
    }

    public interface Callback {
        void onShowContentView(View view);
        void onErrorButtonClick(int errorCode);
    }

    public static class Builder {
        private Callback mCallback;

        public Builder callback(Callback callback) {
            mCallback = callback;

            return this;
        }

        private Callback callback() {
            Callback callback = mCallback;
            mCallback = null;
            return callback;
        }

        public FeedViewHelper forActivity(Activity activity) {
           return FeedViewHelper.forActivity(activity, callback());
        }

        public FeedViewHelper forFragment(LayoutInflater inflater, ViewGroup container) {
            return FeedViewHelper.forFragment(inflater, container, callback());
        }

        public FeedViewHelper forStandaloneView(ViewGroup parent) {
            return FeedViewHelper.forStandaloneView(parent, callback());
        }
    }

    @IntDef({STATE_NONE, STATE_CONTENT, STATE_LOADING, STATE_ERROR})
    public @interface State {}
}
