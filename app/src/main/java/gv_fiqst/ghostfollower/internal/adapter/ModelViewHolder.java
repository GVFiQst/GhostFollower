package gv_fiqst.ghostfollower.internal.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ModelViewHolder<Model> extends RecyclerView.ViewHolder {

    private ViewHolderCallback mCallback;

    private boolean isReleased = false;
    private boolean isBound = false;


    public ModelViewHolder(View itemView) {
        super(itemView);
    }

    void bind(Model model, Metadata metadata) {
        onBindHolder(model, metadata);
        isBound = true;
    }

    protected abstract void onBindHolder(Model model, @Nullable Metadata metadata);

    public void create(ViewHolderCallback callback) {
        mCallback = callback;
        isReleased = false;

        onCreate();
    }

    public boolean isReleased() {
        return isReleased;
    }

    public boolean isBound() {
        return isBound;
    }

    void release() {
        isReleased = true;
        isBound = false;
        mCallback = null;

        onRelease();
    }

    protected void onCreate() {

    }

    protected void onRelease() {
    }

    protected void performAction(int action) {
        mCallback.onSelected(getAdapterPosition(), action);
    }

    interface ViewHolderCallback {
        void onSelected(int pos, int action);
    }
}
