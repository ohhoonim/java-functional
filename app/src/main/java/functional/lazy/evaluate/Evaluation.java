package functional.lazy.evaluate;

import java.util.function.Supplier;

public class Evaluation {
    public static boolean evaluate(final int value) {
        try {
            Thread.sleep(2000l); // 2초가 걸린다고 가정
        } catch(Exception e) {
            e.getMessage();
        }
        return value > 100;
    }

    public static boolean eagerEvaluator(boolean input1, boolean input2) {
        return input1 && input2;
    }

    public static boolean lazyEvaluator(
        Supplier<Boolean> input1, Supplier<Boolean> input2
    ) {
        return input1.get() && input2.get();
    }
}
