package functional;

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
    }

    @Test
    public void supplierTest() {
        // get
    }

    @Test
    public void functionTest() {
        // apply
    }

    @Test
    public void predicateTest() {
        // test
    }
}
