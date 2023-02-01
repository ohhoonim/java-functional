package functional.lazy.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class LazyMain {
    public static void main(String[] args) {
        BiFunction<String, Long, Runnable> runner = (threadName, sleepTime) -> {
            return () -> {
                System.out.println("start: " + threadName);
                HolderNative holder = new HolderNative();
                for (int i = 0; i < 100; i++) {
                    Heavy heavy = holder.getHeavy();
                    System.out.println("[" + threadName + " " + i + "] " + heavy);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end " + threadName);
            };
        };

        List<Runnable> runners = new ArrayList<>();

        for(int i = 100; i < 300; i++) {
            runners.add(runner.apply("thread-" + i, (long)i));
        }

        for (Runnable run: runners) {
            Thread thread = new Thread(run);
            thread.start();
        }
    }
}
