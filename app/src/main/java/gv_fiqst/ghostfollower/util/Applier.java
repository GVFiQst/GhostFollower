package gv_fiqst.ghostfollower.util;

import java.util.ArrayList;
import java.util.List;

public final class Applier {


    /* apply() */

    /**
     * Applies some action to T (IF IT IS NOT NULL) and returns it
     */
    public static <T> T apply(T t, ApplyFunc<T> apply, OnError onError) {
        try {
            if (t != null) {
                apply.apply(t);
            }

            return t;
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return t;
    }

    public static <T> T apply(T t, ApplyFunc<T> apply) {
        return apply(t, apply, ON_ERROR_DEFAULT);
    }


    /* release() */

    public static <T> T release(T t, ApplyFunc<T> apply, boolean returnNull, OnError onError) {
        if (t != null) {
            apply(t, apply, onError);
        }

        return returnNull ? null : t;
    }

    public static <T> T release(T t, ApplyFunc<T> apply, boolean returnNull) {
        return release(t, apply, returnNull, ON_ERROR_DEFAULT);
    }

    public static <T> T release(T t, ApplyFunc<T> apply) {
        return release(t, apply, ON_ERROR_DEFAULT);
    }

    public static <T> T release(T t, ApplyFunc<T> apply, OnError onError) {
        return release(t, apply, false, onError);
    }


    /* transform() */

    public static <P, R> R transform(P param, TransformFunc<P, R> func) {
        return transform(param, func, ON_ERROR_DEFAULT, false);
    }

    public static <P, R> R transform(P param, TransformFunc<P, R> func, boolean useNull) {
        return transform(param, func, ON_ERROR_DEFAULT, useNull);
    }

    public static <P, R> R transform(P param, TransformFunc<P, R> func, OnError onError) {
        return transform(param, func, onError, false);
    }

    public static <P, R> R transform(P param, TransformFunc<P, R> func, OnError onError, boolean useNull) {
        try {
            if (useNull || param != null) {
                return func.transform(param);
            }
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return null;
    }

    public static <P1, P2, R> R transform(P1 param1, P2 param2, TransformFunc2<P1, P2, R> func) {
        return transform(param1, param2, func, ON_ERROR_DEFAULT);
    }

    public static <P1, P2, R> R transform(P1 param1, P2 param2, TransformFunc2<P1, P2, R> func, OnError onError) {
        try {
            return func.transform(param1, param2);
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return null;
    }

    public static <P1, P2, P3, R> R transform(P1 param1, P2 param2, P3 param3, TransformFunc3<P1, P2, P3, R> func) {
        return transform(param1, param2, param3, func, ON_ERROR_DEFAULT);
    }

    public static <P1, P2, P3, R> R transform(P1 param1, P2 param2, P3 param3, TransformFunc3<P1, P2, P3, R> func, OnError onError) {
        try {
            return func.transform(param1, param2, param3);
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return null;
    }

    public static <P1, P2, P3, P4, R> R transform(P1 param1, P2 param2, P3 param3, P4 param4, TransformFunc4<P1, P2, P3, P4, R> func) {
        return transform(param1, param2, param3, param4, func, ON_ERROR_DEFAULT);
    }

    public static <P1, P2, P3, P4, R> R transform(P1 param1, P2 param2, P3 param3, P4 param4, TransformFunc4<P1, P2, P3, P4, R> func, OnError onError) {
        try {
            return func.transform(param1, param2, param3, param4);
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return null;
    }


    /* applyForEach() */

    public static <T> List<T> applyForEach(List<T> list, ApplyFunc<T> apply, OnError onError) {
        try {
            for (T t : list) {
                if (t != null) {
                    apply.apply(t);
                }
            }
        } catch (Throwable throwable) {
            onError.onError(throwable);
        }

        return list;
    }

    public static <T> List<T> applyForEach(List<T> list, ApplyFunc<T> apply) {
        return applyForEach(list, apply, ON_ERROR_DEFAULT);
    }

    /* findForEach() */

    /**
     * For each item in {@code list} checks with a function {@code forEach} and returns that item
     * if function returned true. If {@code true} was never returned returns {@code null}
     * and runs notFound Runnable if it was specified or runs OnError if it was specified
     * or throws {@code exception} or RuntimeException with a {@code message} if it was specified
     * or does nothing if none was specified.
     */
    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach, Runnable notFound, OnError onError) {
        try {
            for (T t : list) {
                if (forEach.transform(t)) {
                    return t;
                }
            }
        } catch (Throwable throwable) {
            onError.onError(new RuntimeException(throwable));
        }

        notFound.run();
        return null;
    }

    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach) {
        return findForEach(list, forEach, () -> {
        });
    }

    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach, String throwWithMessage) {
        return findForEach(list, forEach, () -> {
            throw new RuntimeException(throwWithMessage);
        });
    }

    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach, RuntimeException exception) {
        return findForEach(list, forEach, () -> {
            throw exception;
        });
    }

    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach, Runnable notFound) {
        return findForEach(list, forEach, notFound, ON_ERROR_DEFAULT);
    }

    public static <T> T findForEach(List<T> list, TransformFunc<T, Boolean> forEach, OnError onError) {
        return findForEach(list, forEach, () -> {
        }, onError);
    }


    /* transformList() */

    /**
     * For each item in {@code list} applies function {@code forEach} and transformed item puts to
     * {@code resultList} which is then returned.
     *
     * @param onError      Callback if some error occurred.
     * @param applyOnError Callback that let's user decide what to do if something gone wrong. Default
     *                     implementation does nothing to the result list.
     */
    public static <P, R, L extends List<R>> L transformList(Iterable<P> iterable, L resultList, TransformFunc<P, R> forEach, OnError onError, ApplyFunc2<List<R>, L> applyOnError) {
        // We don't want to corrupt result list if something goes wrong!
        List<R> cache = new ArrayList<>();

        try {
            for (P param : iterable) {
                R el = forEach.transform(param);

                // null indicates deletion
                if (el != null) {
                    cache.add(el);
                }
            }
        } catch (Throwable t) {
            onError.onError(t);
            applyOnError.apply(cache, resultList);
            return resultList;
        }

        resultList.addAll(cache);
        return resultList;
    }

    public static <P, R, L extends List<R>> L transformList(Iterable<P> iterable, L resultList, TransformFunc<P, R> forEach, ApplyFunc2<List<R>, L> applyOnError) {
        return transformList(iterable, resultList, forEach, ON_ERROR_DEFAULT, applyOnError);
    }

    public static <P, R, L extends List<R>> L transformList(Iterable<P> iterable, L resultList, TransformFunc<P, R> forEach) {
        return transformList(iterable, resultList, forEach, ON_ERROR_DEFAULT, (rs, rs2) -> {
        });
    }

    public static <P, R> ArrayList<R> transformList(Iterable<P> iterable, TransformFunc<P, R> forEach, OnError onError) {
        return transformList(iterable, new ArrayList<>(), forEach, onError, (rs, rs2) -> {
        });
    }

    public static <P, R> ArrayList<R> transformList(Iterable<P> iterable, TransformFunc<P, R> forEach, ApplyFunc2<List<R>, ArrayList<R>> applyOnError) {
        return transformList(iterable, new ArrayList<>(), forEach, applyOnError);
    }

    public static <P, R> ArrayList<R> transformList(Iterable<P> iterable, TransformFunc<P, R> forEach) {
        return transformList(iterable, forEach, (rs, rs2) -> {
        });
    }


    /* builder() */

    public static <T> ObjectBuilder<T> builder(T object, OnError onError) {
        return new ObjectBuilderImpl<>(object, onError);
    }

    public static <T> ObjectBuilder<T> builder(T object) {
        return builder(object, ON_ERROR_DEFAULT);
    }


    /* --------- END --------- */

    private Applier() {
    }

    public interface ApplyFunc<T> {
        void apply(T t);
    }

    public interface ApplyFunc2<T1, T2> {
        void apply(T1 t1, T2 t2);
    }

    public interface TransformFunc<Param, Result> {
        Result transform(Param t);
    }

    public interface TransformFunc2<P1, P2, R> {
        R transform(P1 param1, P2 param2);
    }

    public interface TransformFunc3<P1, P2, P3, R> {
        R transform(P1 param1, P2 param2, P3 param3);
    }

    public interface TransformFunc4<P1, P2, P3, P4, R> {
        R transform(P1 param1, P2 param2, P3 param3, P4 param4);
    }

    public interface OnError {
        void onError(Throwable t);
    }

    public interface ObjectBuilder<T> {
        ObjectBuilder<T> apply(ApplyFunc<T> apply);
        T build();
    }

    static final OnError ON_ERROR_DEFAULT = t -> {
        throw new ErrorNotCaught(t);
    };

    private static class ErrorNotCaught extends RuntimeException {
        public ErrorNotCaught() {
        }

        public ErrorNotCaught(String message) {
            super(message);
        }

        public ErrorNotCaught(String message, Throwable cause) {
            super(message, cause);
        }

        public ErrorNotCaught(Throwable cause) {
            super(cause);
        }
    }

    private static class ObjectBuilderImpl<T> implements ObjectBuilder<T> {
        private T mObject;
        private OnError mOnError;
        private boolean isReleased = false;

        public ObjectBuilderImpl(T object, OnError onError) {
            mObject = object;
            mOnError = onError;
        }

        @Override
        public ObjectBuilder<T> apply(ApplyFunc<T> apply) {
            assertReleased(isReleased);

            Applier.apply(mObject, apply, mOnError);
            return this;
        }

        @Override
        public T build() {
            assertReleased(isReleased);

            return release();
        }

        private T release() {
            T obj = mObject;

            isReleased = true;
            mObject = null;
            mOnError = null;

            return obj;
        }

        private static void assertReleased(boolean released) {
            if (released) {
                throw new IllegalStateException("ObjectBuilder is released");
            }
        }
    }
}
