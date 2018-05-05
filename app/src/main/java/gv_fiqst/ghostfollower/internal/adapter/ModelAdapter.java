package gv_fiqst.ghostfollower.internal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gv_fiqst.ghostfollower.util.Applier;
import gv_fiqst.ghostfollower.util.Util;

public abstract class ModelAdapter<Model> extends RecyclerView.Adapter<ModelViewHolder<Model>> implements ModelViewHolder.ViewHolderCallback {
    private final List<ModelViewHolder> mReleasable = new ArrayList<>();
    private final List<Model> mList = new ArrayList<>();
    private final List<Metadata> mMeta = new ArrayList<>();
    private OnModelSelected<Model> mOnModelSelected;

    public ModelAdapter(OnModelSelected<Model> onModelSelected) {
        mOnModelSelected = onModelSelected;
    }

    public void addAll(Collection<Model> list) {
        mList.addAll(list);
        Util.fillList(mMeta, null, mList.size());

        notifyDataSetChanged();
    }

    public void setList(Collection<Model> list) {
        clear();

        addAll(list);
    }

    public void append(Model model) {
        addAt(mList.size(), model);
    }

    public void prepend(Model model) {
        addAt(0, model);
    }

    private void addAt(int index, Model model) {
        mList.add(index, model);
        mMeta.add(index, null);

        notifyItemInserted(index);
        notifyItemRangeChanged(index, 1);
    }

    public <T> void putMetadata(int pos, Class<T> cls, T obj) {
        Metadata metadata = mMeta.get(pos);
        if (metadata == null) {
            mMeta.add(pos, metadata = new Metadata());
        }

        metadata.putMeta(cls, obj);

        notifyItemChanged(pos);
    }

    public boolean remove(Model model) {
        int index = mList.indexOf(model);

        if (index >= 0) {
            return removeAt(index);
        }

        return mList.isEmpty();
    }

    public boolean removeAt(int index) {
        if (index < 0 || index >= mList.size()) {
            throw new IndexOutOfBoundsException("index " + index + " < 0 or > size (" + mList.size());
        }

        mList.remove(index);
        mMeta.remove(index);

        notifyItemRemoved(index);
        notifyItemRangeChanged(index, 1);

        return mList.isEmpty();
    }

    @Override
    public void onSelected(int pos, int action) {
        if (mOnModelSelected != null) {
            mOnModelSelected.onSelected(mList.get(pos), pos, action);
        }
    }

    @Override
    public final ModelViewHolder<Model> onCreateViewHolder(ViewGroup parent, int viewType) {
        ModelViewHolder<Model> vh = createVH(parent, viewType);

        addReleasable(vh);
        return vh;
    }

    private void addReleasable(ModelViewHolder<Model> vh) {
        mReleasable.add(vh);
        vh.create(this);
    }

    protected abstract ModelViewHolder<Model> createVH(ViewGroup viewGroup, int viewType);

    @Override
    public void onBindViewHolder(ModelViewHolder<Model> holder, int position) {
        // mReleasable.containts(holder) takes to much operations for just binding
        if (holder.isReleased()) {
            addReleasable(holder);
        }

        holder.bind(mList.get(position), mMeta.get(position));
    }

    public void release() {
        clear();

        mOnModelSelected = null;
    }

    private void clear() {
        mList.clear();
        Applier.applyForEach(mMeta, Metadata::release)
                .clear();

        // We never know which holder will be reused and which will be deleted
        // So we just release all and ones that are reused will be bound again
        // in onBindViewHolderMethod
        Applier.applyForEach(mReleasable, ModelViewHolder::release)
                .clear();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
