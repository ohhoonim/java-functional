package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class LambdaTypeTest {
    @Test
    public void 람다식의_대표유형_4_가지() {
        // Cunsumer : input 만 있는 경우 accept
        // Supplier : output 만 있는 경우 get
        // Function : input, output 둘 다 있는 경우 apply
        // Predicate : input 있고, output이 불린인 경우 test
        // 그럼 둘다 없는 경우는 ?
        // Runnable : 둘다 없는 경우 run
    }

    @Test
    public void consumerTest() {
        // accept
        Consumer<String> consumer = (name) -> System.out.println(name);
        consumer.accept("matthew"); // console -> "matthew"

        Consumer<String> consumer2 = new Consumer<String>() {
            @Override
            public void accept(String name) {
                System.out.println(name);
            }
        };
        consumer2.accept("alison");
    }

    @Test
    public void supplierTest() {
        // get
        Supplier<String> supplier = () -> "matthew";
        assertEquals("matthew", supplier.get());
    }

    @Test
    public void functionTest() {
        // apply
        Function<Integer, String> function = (num) -> "--" + num + "--";
        assertEquals("--10--", function.apply(10));
    }

    private String stringDecorator(Function<Integer, String> deco, int num) {
        return deco.apply(num);
    }

    @Test
    public void functionParamTest() {
        Function<Integer, String> decoAstar = (num) -> "**" + num + "**";
        String result = stringDecorator(decoAstar, 23);
        assertEquals("**23**", result);

        String result2 = stringDecorator((num) -> "--" + num + "--", 23);
        assertEquals("--23--", result2);
    }

    @Test
    public void predicateTest() {
        // test
        Predicate<String> predicate = (string) -> string.equals("matthew") ? true : false;
        assertTrue(predicate.test("matthew"));
    }
}
