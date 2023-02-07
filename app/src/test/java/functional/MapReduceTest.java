package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.PurchaseItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapReduceTest {

    public static Stream<Arguments> dataFactory() {
        List<PurchaseItem> purchaseList =
                List.of(new PurchaseItem("cookie", BigDecimal.valueOf(1500)),
                        new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                        new PurchaseItem("hambuger", BigDecimal.valueOf(1530)),
                        new PurchaseItem("milk", BigDecimal.valueOf(1200)),
                        new PurchaseItem("bread", BigDecimal.valueOf(4500)),
                        new PurchaseItem("bread", BigDecimal.valueOf(4500)),
                        new PurchaseItem("cookie", BigDecimal.valueOf(1500)),
                        new PurchaseItem("bread", BigDecimal.valueOf(4500)),
                        new PurchaseItem("bread", BigDecimal.valueOf(4500)),
                        new PurchaseItem("milk", BigDecimal.valueOf(1200)));
        return Stream.of(arguments(purchaseList, Optional.empty(), BigDecimal.valueOf(27430)),
                arguments(purchaseList, Optional.of("cookie"), BigDecimal.valueOf(3000)),
                arguments(purchaseList, Optional.of("bread"), BigDecimal.valueOf(18000))

        );
    }
    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 기존에_하던데로_코딩해보자(List<PurchaseItem> list, Optional<String> itemName,
            BigDecimal expectedPrice) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (PurchaseItem item : list) {
            if (itemName.isEmpty()) {
                totalPrice = totalPrice.add(item.getValue());
            } else {
                if (item.getItem().equals(itemName.get())) {
                    totalPrice = totalPrice.add(item.getValue());
                }
            }
        }
        log.debug("{}", totalPrice);
        assertEquals(expectedPrice, totalPrice);
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 문제를_해결하는_생각의_순서(List<PurchaseItem> list, Optional<String> itemName,
            BigDecimal expectedPrice) {
        // 1. 필터링을 한다.
        List<PurchaseItem> filteredList = new ArrayList<>();
        for (PurchaseItem item : list) {
            if (itemName.isPresent()) {
                if (item.getItem().equals(itemName.get())) {
                    filteredList.add(item);
                }
            } else {
                filteredList.add(item);
            }
        }
        // 2. 필터된 것들의 가격만을 가져온다.  (맵)
        List<BigDecimal> prices = new ArrayList<>();
        for(PurchaseItem item : filteredList) {
            prices.add(item.getValue());
        }
        
        // 2. 합계를 구한다. (리듀스)
        BigDecimal totalPriceByItemName = BigDecimal.ZERO;
        for (BigDecimal price : prices) {
            totalPriceByItemName = totalPriceByItemName.add(price);
        }
        log.debug("{}", totalPriceByItemName);
        assertEquals(expectedPrice, totalPriceByItemName);
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 함수형_스타일로_작성해보자(List<PurchaseItem> list, Optional<String> itemName,
            BigDecimal expectedPrice) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (itemName.isEmpty()) {
            totalPrice = list.stream().map(p -> p.getValue()).reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        } else {
            totalPrice = list.stream().filter(p -> p.getItem().equals(itemName.get()))
                    .map(p -> p.getValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        log.debug("{}", totalPrice);
        assertEquals(expectedPrice, totalPrice);
        // oop에 함수형 스타일을 가미하면 문제풀이의 생각대로 코딩하기가 쉬워진다.
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 리팩토링(List<PurchaseItem> list, Optional<String> itemName,
            BigDecimal expectedPrice) {
        Predicate<PurchaseItem> filter = p -> {
            if (itemName.isEmpty()) {
                return true;
            } else {
                return p.getItem().equals(itemName.get());
            }
        };
        BigDecimal totalPrice = list.stream()
                .filter(filter)
                .map(p -> p.getValue())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        log.debug("{}", totalPrice);
        assertEquals(expectedPrice, totalPrice);
    }
}
