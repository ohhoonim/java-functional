package functional.tailCall;

public class TailCalls {
    public static <T> TailCall<T> call(final TailCall<T> nextCall) {
        return nextCall;
    }

    public static <T> TailCall<T> done(T value) {
        return new TailCall<T>() {
            @Override
            public TailCall<T> apply() {
                throw new Error("not implemented");
            }

            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public T result() {
                return value;
            }

        };
    }
}
