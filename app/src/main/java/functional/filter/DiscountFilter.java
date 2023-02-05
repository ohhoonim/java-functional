package functional.filter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import functional.entity.PurchaseItem;

public class DiscountFilter {
    public static Function<PurchaseItem, PurchaseItem> discount (BigDecimal discount) {
        return p -> {
            BigDecimal itemValue = p.getValue();
            BigDecimal discountedValue = itemValue.subtract(itemValue.multiply(discount));
            return PurchaseItem.builder()
                .item(p.getItem()).value(discountedValue).build();
        }; 
    } 

    public static <T extends PurchaseItem> Function<T, T> totalDiscount(List<Function<T, T>> funcs) {
        return funcs.stream() 
        .reduce((acc, next) -> acc.compose(next)).orElse(item -> item);
    }
}
