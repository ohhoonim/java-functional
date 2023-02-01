package functional.filter;

import java.awt.Color;
import java.util.List;
import java.util.function.Function;

public class Camera {
    private Function<Color, Color> filter;

    public Camera() {
        setFilters(List.of());
    }

    public Color capture(final Color inputColor) {
        final Color processedColor = filter.apply(inputColor);

        return processedColor;
    }
    
    public void setFilters(List<Function<Color, Color>> filters) {
        filter = filters.stream() 
            .reduce((filter, next) -> filter.compose(next))
            .orElse(color -> color);
    }

}
