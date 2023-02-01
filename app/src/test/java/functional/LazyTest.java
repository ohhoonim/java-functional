package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import functional.lazy.evaluate.Evaluation;

public class LazyTest {

    // 코드에서 호출되는 순간 실행되도록 하는 것을 eager 라 한다.
    // 코드 실행 순서가 되었을 때 약간 지연시키는 것을 lazy 라고 한다.
    // eager는 간단하지만 lazy는 효율적이다.
    @Test
    public void AND연산을_eager방식으로_평가하기() {
        boolean result =
                Evaluation.eagerEvaluator(Evaluation.evaluate(200), Evaluation.evaluate(300));
        assertTrue(result); // 4초가 걸린다.
    }

    @Test
    public void AND연산은_첫번째_파라미터가_false면_두번째_파라미터를_평가할_필요가_없다() {
        // 이런한 것을 short-circuiting evaluation 이라고 한다. 
        boolean result =
                Evaluation.eagerEvaluator(Evaluation.evaluate(50), Evaluation.evaluate(300));
        assertFalse(result); // eager방식은 역시나 4초가 걸린다.
    }

    @Test
    public void AND연산을_lazy하게_평가하기() {
        boolean result = Evaluation.lazyEvaluator(() -> Evaluation.evaluate(50),
                () -> Evaluation.evaluate(300));
        assertFalse(result); // 2초밖에 걸리지 않는다.
    }


    // short-circuiting terminal operation
    @Test
    public void 스트림의_두_가지_종류의_메서드() {
        // 중간(intermediate) 오퍼레이션 : map, filter
        // 종단(terminal) 오퍼레이션 : reduce, collect 

        // 중간 오퍼레이션은 본질적으로 레이지 속성을 가진다.
        // 종단 오퍼레이션이 호출되었을때 중간 오퍼레이션의 람다식을 실행한다.
        Stream<String> names =
                Stream.of("dean", "jack", "sam", "matthew", "alison", "pi", "dragon");

        String result = names.filter(name -> {
            System.out.println(name);
            return name.length() > 5;
        }).findFirst().get();
        assertEquals("matthew", result);
        // findFirst()호출 되기 전까지 filter의 람다식은 실행되지 않는다.
        // findFirst() 는 종단 오퍼레이션이면사, short-circuiting 이다. 
        // findFirst는 첫번째 결과 값을 찾는 것이므로 "matthew"를 찾은 후 결과가 만족되었으므로
        // 리스트의 뒷부분 "alison", "pi", "dragon"을 검사하지 않는다.

    }

    // short-circuiting intermediate operation
    @Test
    public void 스트림의_interate를_활용해보자() {

        int size = 10000;
        // 3씩 증가하는 리스트를 만들어줌
        List<Integer> count = Stream.iterate(1, (x) -> x + 3).limit(size).toList();
        System.out.println(count);
        assertEquals(size, count.size()); 
        // Stream.interate()는 무한 스트림을 만들었다. 
        // limit는 중간(intermediate) 오퍼레이터 이면서 short-circuiting 이다. 
        // limit의 파라미터 size로 범위를 제한 할 수 있다. 
    }
    @Test
    public void for문을사용하면() {
        int size = 10000;
        List<Integer> count = new ArrayList<>();
        for(int i = 0; i < size; i = i + 3) {
            count.add(i);
        }
        System.out.println(count);
        assertEquals(3334, count.size()); // 1000개를 만드려면 계산해야한다. ㅠㅠ  
    }
}
