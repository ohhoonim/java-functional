package functional.memoization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class RodCutterBasic {
    final List<Integer> prices;

    public RodCutterBasic(final List<Integer> pricesForLengh) {
        prices = pricesForLengh;
    }

    // 최대 수익을 얻는 로직
    public int maxProfit(final int length) {
        int profit = (length <= prices.size()) ? prices.get(length - 1) : 0;
        for (int i = 1; i < length; i++) {
            int priceWhenCut = maxProfit(i) + maxProfit(length - i);
            if (profit < priceWhenCut) {
                profit = priceWhenCut;
            }
        }
        return profit;
    }

    // 최대 수익을 얻는 로직
    public int maxProfitMemoiz(final int rodLength) {
        BiFunction<Function<Integer, Integer>, Integer, Integer> compute = (func, length) -> {
            int profit = (length <= prices.size()) ? prices.get(length - 1) : 0;
            for (int i = 1; i < length; i++) {
                int priceWhenCut = func.apply(i) + func.apply(length - i);
                if (profit < priceWhenCut) {
                    profit = priceWhenCut;
                }
            }
            return profit;
        };
        return Memoizer.callMemoized(compute, rodLength);
    }


}
