package gv_fiqst.ghostfollower.util;


import java.lang.reflect.*;

public class TypeToken<T> {
    private Class<? super T> cls;
    private Type type;

    public TypeToken() {
        type = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> cls) {
        Type superclass = cls.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
