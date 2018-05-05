package gv_fiqst.ghostfollower.internal.mvp;

public interface ModelResult<Model> {
    Model getResult();
    Throwable getException();

    default boolean isSuccessful() {
        return getResult() != null;
    }

    default boolean hasException() {
        return getException() != null;
    }

    class Success<Model> implements ModelResult<Model> {
        private Model mResult;

        public Success(Model result) {
            mResult = result;
        }

        @Override
        public boolean hasException() {
            // No need to check for null
            return false;
        }

        @Override
        public Model getResult() {
            return mResult;
        }

        @Override
        public Throwable getException() {
            return null;
        }
    }

    class Error<Model> implements ModelResult<Model> {
        private Throwable mThrowable;

        public Error(Throwable throwable) {
            mThrowable = throwable;
        }

        @Override
        public Model getResult() {
            return null;
        }

        @Override
        public boolean isSuccessful() {
            // No need to check for null
            return false;
        }

        @Override
        public Throwable getException() {
            return mThrowable;
        }
    }
}
