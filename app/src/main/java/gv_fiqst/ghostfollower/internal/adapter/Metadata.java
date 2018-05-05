package gv_fiqst.ghostfollower.internal.adapter;


import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<Class, Object> mMetadata = new HashMap<>();

    Metadata(){}

    public <T> T putMeta(Class<T> cls, T obj) {
        return (T) mMetadata.put(cls, obj);
    }

    public <T> T getMeta(Class<T> cls) {
        return (T) mMetadata.get(cls);
    }

    public <T> T removeMeta(Class<T> cls) {
        return (T) mMetadata.remove(cls);
    }

    void release() {
        mMetadata.clear();
    }
}
