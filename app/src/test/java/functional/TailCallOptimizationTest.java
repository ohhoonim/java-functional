package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import functional.tailCall.TailCall;
import functional.tailCall.TailCalls;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TailCallOptimizationTest {
    // 재귀를 사용할 때의 가장 큰 문제점은 입력 데이터가 매우 많은 경우
    // 스택 오버플로구가 발생할 위험이 있다는 것이다.
    // 이 문제를 해결하기위해 테일-콜 최적화(TCO)를 사용한다.
    // Tail-Call optimization
    @Test
    public void 재귀함수_팩토리얼_테스트() {
        int facto = factorial(10);
        assertEquals(3628800, facto);
    }

    private int factorial(int num) {
        // log.debug("{}", num);
        if (num == 1) {
            return num;
        } else {
            return num * factorial(num - 1);
        }
    }

    @Test
    public void 재귀함수_팩토리얼_테일콜_테스트() {
        int facto = factorialTailCall(1, 10);
        assertEquals(3628800, facto);
    }

    private int factorialTailCall(int factorial, int num) {
        log.debug("{} - {}", factorial, num);
        if (num == 1) {
            return factorial;
        } else {
            return factorialTailCall(factorial * num, num - 1);
        }
    }



    // 자바에서는 컴파일 레벨에서 TCO를 지원하지 않지만
    // 람다식을 사용하면 구현할 수 있다.
    @Test
    public void 람다식으로_테일_콜_최적화() {
        // 아래예제를 제대로 구현하려면 BigInteger로 구현해야한다.
        int result = factorialRec(1, 10).invoke();
        assertEquals(3628800, result);
    }

    private TailCall<Integer> factorialRec(int factorial, int num) {
        if (num == 1) {
            return TailCalls.done(factorial);
        } else {
            return TailCalls.call(() -> factorialRec(factorial * num, num - 1));
        }
    }

}
