package functional;

import java.util.function.Predicate;
import functional.entity.PurchaseItem;

public class PurchaseItemUtil {
    public static Predicate<PurchaseItem> itemSelector(String itemName) {
        return p -> itemName.equals(p.getItem());

    }
}
