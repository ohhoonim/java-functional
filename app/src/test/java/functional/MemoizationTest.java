package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import functional.memoization.RodCutterBasic;

public class MemoizationTest {
    // 최적화 문제를 풀기위해 알고리즘 분야의 다이내믹 프로그래밍이라는
    // 기술에서는 재귀를 사용하여 문제를 해결할 수 있다.
    // 다이내믹 프로그래밍이란 주어진 문제에 대한 최종적인 솔루션을
    // 구하기 위해 원래 하나였던 문제를 서브 문제로 분할해 해결하는 방법이다.


    // 로드-커팅 문제는 주어진 장작을 다양한 크기로 잘라 최대한의 이익에 나도록하는 문제이다.
    @Test
    public void 로드_커팅_문제() {
        List<Integer> pricesValues = List.of(2, 1, 1, 2, 2, 2, 1, 8, 9, 15);
        RodCutterBasic rodCutter = new RodCutterBasic(pricesValues);

        // int profit = rodCutter.maxProfit(5);
        // assertEquals(10, profit);
        int profit = rodCutter.maxProfit(21);
        assertEquals(42, profit); // 8.7초 걸렸다.
    }

    @Test
    public void 결과를_메모이제이션하기() {
// 동작안함. 
        // List<Integer> pricesValues = List.of(2, 1, 1, 2, 2, 2, 1, 8, 9, 15);
        // RodCutterBasic rodCutter = new RodCutterBasic(pricesValues);

        // int profit = rodCutter.maxProfitMemoiz(21);
        // assertEquals(42, profit); // 초 걸렸다.

    }
}
