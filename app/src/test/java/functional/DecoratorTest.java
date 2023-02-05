package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.PurchaseItem;
import functional.filter.Camera;
import functional.filter.DiscountFilter;

@DisplayName("데코레이터패턴구현 or 람다식체인")
public class DecoratorTest {

    @Test
    public void Function_함수형인터페이스의_compose알아보기() {
        Function<String, String> stepOne = one -> "one:" + one;
        Function<String, String> stepTwo = two -> "two:" + two ;
        // 코드상 순서대로 funciton이 호출된다. 
        String result = stepOne.compose(stepTwo).apply("matthew"); // decorator로 동작한다. 
        assertEquals("one:two:matthew", result);

        Function<String, String> filters = Stream.of(stepOne, stepTwo)
                .reduce((filter, next) -> filter.compose(next))  
                .orElse(filter -> filter)
                ;
        assertEquals("one:two:matthew", filters.apply("matthew"));
    }

    @ParameterizedTest(name="filterd: {0}")
    @MethodSource("filterDataFactory")
    public void 카메라에_여러개의_필터적용하기(Color expectedColor, List<Function<Color, Color>> filters) {
        Camera sonyA7R5 = new Camera();
        sonyA7R5.setFilters(filters);
        Color capturedColor = sonyA7R5.capture(new Color(10,12,14));

        assertEquals(expectedColor, capturedColor);
    }

    private static Stream<Arguments> filterDataFactory() {
        Function<Color, Color> brighter = color -> color.brighter();
        Function<Color, Color> darker = color -> color.darker();
        return Stream.of(
            arguments(
                /* expected */ new Color(14, 15, 17),
                /* filters */ List.of(brighter, brighter, darker) 
            ),
            arguments(
                /* expected */ new Color(10, 11, 12),
                /* filters */ List.of(brighter,  darker) 
            )
        );
    }

    public static Stream<Arguments> dataFactory() {
        List<PurchaseItem> purchaseList = List.of(
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
            );
        return Stream.of(
            arguments(purchaseList, true, true, BigDecimal.valueOf(23452.65).setScale(2)) // vip 
            , arguments(purchaseList, true, false, BigDecimal.valueOf(24687.00).setScale(2)) // 회원
            , arguments(purchaseList, false, false, BigDecimal.valueOf(27430.00).setScale(2)) // 비회원
        );
    }
    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 계산을_해보자(List<PurchaseItem> list) {
        BigDecimal totalPrice = list.stream()
            .map(PurchaseItem::getValue)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        // 비회원
        assertEquals(BigDecimal.valueOf(27430.00).setScale(2), totalPrice.setScale(2));
        // 회원 : 10% 할인
        BigDecimal memberPrice10 = totalPrice.subtract(totalPrice.multiply(BigDecimal.valueOf(0.1)));
        assertEquals(BigDecimal.valueOf(24687.00).setScale(2), memberPrice10.setScale(2));
        // 15% 할인 
        BigDecimal memberPrice15 = totalPrice.subtract(totalPrice.multiply(BigDecimal.valueOf(0.15)));
        assertEquals(BigDecimal.valueOf(23315.50).setScale(2), memberPrice15.setScale(2));
        // vip : 회원할인 + 추가 5% 할인 
        BigDecimal vipPrice = memberPrice10.subtract(memberPrice10.multiply(BigDecimal.valueOf(0.05)));
        assertEquals(BigDecimal.valueOf(23452.65).setScale(2), vipPrice.setScale(2));
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void compose를_이용하여_데코레이팅하기(List<PurchaseItem> list, boolean isMember, boolean isVip, BigDecimal expected) {
        Function<PurchaseItem, PurchaseItem> memberDiscount = DiscountFilter.discount(BigDecimal.valueOf(0.1)); 
        Function<PurchaseItem, PurchaseItem> vipDiscount = DiscountFilter.discount(BigDecimal.valueOf(0.05));
        List<Function<PurchaseItem, PurchaseItem>> funcs = new ArrayList<>();
        if (isMember) funcs.add(memberDiscount);
        if (isVip) funcs.add(vipDiscount);
        Function<PurchaseItem, PurchaseItem> totalDiscount = DiscountFilter.totalDiscount(funcs); 

        BigDecimal totalPrice = list.stream()
            .map(totalDiscount)
            .map(PurchaseItem::getValue)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        assertEquals(expected, totalPrice.setScale(2));
    }

    

}













