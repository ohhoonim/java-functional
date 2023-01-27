package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.PurchaseItem;

public class StrategyTest {
    
    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 구매내역_총지불금액_구하기(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal) {
        BigDecimal total = purchaseItemList.stream().map(p -> p.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add) ;
        assertEquals(expectedTotal, total);
    }

    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 아이템별로_구매내역_총지불금액_구하기_필터전략추가(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        BigDecimal total = purchaseItemList.stream()
            .filter(p -> item.equals(p.getItem())) 
            .map(p -> p.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add) ;
        assertEquals(expectedTotal, total);
    }

    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 위_로직을_메서드로_리팩터링하고_필터부분을_파라미터로_받기(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        String itemName = item;
        Predicate<PurchaseItem> itemSelector = (p) -> itemName.equals(p.getItem());
        BigDecimal total = totalCalcuratorByItem(purchaseItemList, itemSelector); 
        assertEquals(expectedTotal, total);
    }

    private <T extends PurchaseItem> BigDecimal totalCalcuratorByItem(List<T> list, Predicate<T> itemSelector) {
        return list.stream()
            .filter(itemSelector)
            .map(p -> p.getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add) ;
    }
    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 이렇게까지해야하나_싶지만(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        BigDecimal total = totalCalcuratorByItem(purchaseItemList, PurchaseItemUtil.itemSelector(item)); 
        assertEquals(expectedTotal, total);
    }


    public static Stream<Arguments> dataFactory() {
        return Stream.of(
            arguments(
                List.of(
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                    new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                    ),
                    BigDecimal.valueOf(11230)
            ),
            arguments(
                List.of(
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                    new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                    ),
                    BigDecimal.valueOf(27430)
            )
        );
    }
    public static Stream<Arguments> dataByItemFactory() {
        return Stream.of(
            arguments(
                List.of(
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                    new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                    ),
                    BigDecimal.valueOf(1500), "cookie"
            ),
            arguments(
                List.of(
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                    new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                    new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                    ),
                    BigDecimal.valueOf(18000), "bread"
            )
        );
    }
}
