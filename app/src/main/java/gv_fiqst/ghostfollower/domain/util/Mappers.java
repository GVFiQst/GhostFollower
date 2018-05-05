package gv_fiqst.ghostfollower.domain.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gv_fiqst.ghostfollower.domain.repo.ModelMapper;

public class Mappers {
    public static Builder build() {
        return new Builder();
    }

    private final Map<Class, ModelMapper> mModelMappers = new HashMap<>();

    private Mappers(Builder b) {
        for (int i = 0; i < b.mClasses.size(); i++) {
            mModelMappers.put(b.mClasses.get(i), b.mMappers.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public <In, Out> ModelMapper<In, Out> getMapper(Class<Out> cls) {
        return mModelMappers.get(cls);
    }

    public static class Builder {
        List<Class> mClasses;
        List<ModelMapper> mMappers;

        public Builder() {
            mClasses = new ArrayList<>();
            mMappers = new ArrayList<>();
        }

        public <T> Builder mapper(Class<T> cls, ModelMapper<?, T> mapper) {
            mClasses.add(cls);
            mMappers.add(mapper);

            return this;
        }

        public Mappers build() {
            return new Mappers(this);
        }
    }
}
